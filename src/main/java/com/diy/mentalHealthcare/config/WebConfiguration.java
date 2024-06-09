package com.diy.mentalHealthcare.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.diy.mentalHealthcare.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebConfiguration {
	
	@Autowired
	private UserService userService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception{
		return http
			.csrf(CsrfConfigurer<HttpSecurity>::disable)
			.cors(CorsConfigurer<HttpSecurity>::disable)
			.authorizeHttpRequests(
					registry -> registry.requestMatchers("/login", "/register").permitAll().
					anyRequest().authenticated()
					).formLogin( configurer -> {
						configurer.permitAll();
						configurer.successHandler(authenticationSuccessHandler());
						configurer.failureHandler(authenticationFailureHandler());
					})
			.build();
	}
	
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService((email) -> {
			System.out.println("Entered email " + email);
			Optional<com.diy.mentalHealthcare.model.User> dbUser = userService.getUserByEmail(email);
			System.out.println("retrived dbUser " + dbUser);
			if(dbUser.isEmpty()) {
				throw new UsernameNotFoundException("No user avaliable with email " + email);
			}
			com.diy.mentalHealthcare.model.User loggedInUser = dbUser.get();
			 return User.builder().username(loggedInUser.getEmail())
					 .password(loggedInUser.getPassword()).build();
		});
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpStatus.ACCEPTED.value());
				response.setHeader("Authenticated", request.getParameter("username"));
				
			}
		};
		
	}
	
	
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write("Authentiaction failed");
			}
		};
		
	}
}

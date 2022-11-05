package com.tts.OAuth;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@SpringBootApplication
@RestController
public class OAuthApplication extends WebSecurityConfigurerAdapter {

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal){
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(a -> a
					// "/" allow some content to be visible to unauthenticated users
					// "/error" Spring Boot endpoint for displaying errors
					// "/webjars/**" allows javascript to run for all visitors
					.antMatchers("/", "/error", "/webjars/**").permitAll()
					.anyRequest().authenticated()
			)
			.exceptionHandling(e -> e
					// authenticationEntryPoint allows endpoints to respond with a
					// 401 rather than the default behavior of redirecting to a
					// login page.
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			)
			.logout(l -> l
					.logoutSuccessUrl("/").permitAll()
			)
			
			.csrf(c -> c
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			)	
			.oauth2Login();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(OAuthApplication.class, args);
	}

}

package com.binarystudio.academy.springsecurity.security;

import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import com.binarystudio.academy.springsecurity.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private void applyRouteRestrictions(HttpSecurity http) throws Exception {
		http
				// Set permissions on endpoints
				.authorizeRequests()
				// Our public endpoints
				.antMatchers("/auth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/hotels/*").permitAll()
				// Our private endpoints
				.antMatchers("/hotels/**").hasAnyRole(UserRole.ADMIN.toString(), UserRole.USER.toString())
				.antMatchers("/users/all").hasRole(UserRole.ADMIN.toString())
				.anyRequest().authenticated();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// Enable CORS
				.cors().and()
				// Disable CSRF
				.csrf().disable()
				// Disable basic HTTP auth
				.httpBasic().disable()
				// Disable form login
				.formLogin().disable()
				.exceptionHandling(rejectAsUnauthorized())

				// Set session management to stateless
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		applyRouteRestrictions(http);

		http.addFilterBefore(filterChainExceptionHandler(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	private Customizer<ExceptionHandlingConfigurer<HttpSecurity>> rejectAsUnauthorized() {
		return exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> response.sendError(403));
	}

	@Bean
	public JwtFilter tokenFilter() {
		return new JwtFilter();
	}

	@Bean
	public FilterChainExceptionHandler filterChainExceptionHandler() {
		return new FilterChainExceptionHandler();
	}
}

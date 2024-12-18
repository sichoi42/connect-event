package io.connectevent.connectevent.auth.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class ExceptionHandlerConfig {

	@Bean
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					AuthResponseMessages.JWT_AUTH_FAILURE);
		};
	}

	@Bean
	public AccessDeniedHandler jwtAccessDeniedHandler() {
		return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) -> {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,
					AuthResponseMessages.JWT_ACCESS_FAILURE);
		};
	}
}

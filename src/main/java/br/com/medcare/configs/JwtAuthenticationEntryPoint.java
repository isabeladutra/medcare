package br.com.medcare.configs;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	/**
	 * Essa classe  é usada para lidar com exceções de autenticação e fornecer uma resposta consistente ao cliente quando a autenticação falha (Quando ocorre uma falha na autenticação de um usuário, seja por falta de credenciais ou credenciais inválidas)
	 */
	private static final long serialVersionUID = -1954546581609569773L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}

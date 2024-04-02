package com.prototype.security.domain.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prototype.security.api.dto.request.AuthenticationRequest;
import com.prototype.security.api.dto.request.RegisterRequest;
import com.prototype.security.api.dto.response.CredencialUsuarioResponse;
import com.prototype.security.api.dto.response.RoleRespose;
import com.prototype.security.domain.exception.ObjectNotFoundException;
import com.prototype.security.domain.exception.TokenExceprion;
import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.model.Token;
import com.prototype.security.domain.model.enuns.TokenType;
import com.prototype.security.domain.repository.CredencialUsuarioRepository;
import com.prototype.security.domain.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final CredencialUsuarioRepository repository;
	private final CredencialUsuarioService credencialUsuarioService;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Transactional
	public CredencialUsuarioResponse salvarCredencial(RegisterRequest request) {
		validarDuplicidadeCredencial(request);
		CredencialUsuario credencial = credencialUsuarioService.save(request);
		String jwtToken = jwtService.generateToken(credencial);
		var refreshToken = jwtService.generateRefreshToken(credencial);
		salvarToken(credencial, jwtToken);
		return CredencialUsuarioResponse.builder().id(credencial.getId()).accessToken(jwtToken)
				.refreshToken(refreshToken).build();
	}

	private void validarDuplicidadeCredencial(RegisterRequest credential) {
		if (repository.findByCpf(credential.getCpf()).isPresent()) {
			throw new ObjectNotFoundException("CPF já cadastrado");
		} else if (repository.findByEmail(credential.getEmail()).isPresent()) {
			throw new ObjectNotFoundException("Email já cadastrado");
		}
	}

	public CredencialUsuarioResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
		CredencialUsuario credencial = credencialUsuarioService.buscarUsuarioPeloLogin(request.getLogin());
		var jwtToken = jwtService.generateToken(credencial);
		var refreshToken = jwtService.generateRefreshToken(credencial);
		revogarTokensUsuario(credencial);
		salvarToken(credencial, jwtToken);
		return CredencialUsuarioResponse.builder().id(credencial.getId()).accessToken(jwtToken)
				.refreshToken(refreshToken).build();
	}

	private void salvarToken(CredencialUsuario user, String jwtToken) {
		var token = bindToken(user, jwtToken);
		tokenRepository.save(token);
	}

	private Token bindToken(CredencialUsuario credencial, String jwtToken) {
		return Token.builder()
				.credencial(credencial)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
	}

	private void revogarTokensUsuario(CredencialUsuario credencial) {
		var tokensUsuario = tokenRepository.obterTokensUsuario(credencial.getId());
		if (tokensUsuario.isEmpty())
			return;
		tokensUsuario.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(tokensUsuario);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user.getUsername())) {
				var accessToken = jwtService.generateToken(user);
				revogarTokensUsuario(user);
				salvarToken(user, accessToken);
				var authResponse = CredencialUsuarioResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

	public boolean validarToken(String token) {
		String username = jwtService.extractUsername(token);
		CredencialUsuario credencial = credencialUsuarioService.buscarUsuarioPeloLogin(username);

		boolean isValid = jwtService.isTokenValid(token, credencial.getUsername());
		if (Boolean.FALSE.equals(isValid)) {
			throw new TokenExceprion("Login expirado");
		}

		return jwtService.isTokenValid(token, credencial.getUsername());
	}

	public List<RoleRespose> getRoles(String token) {
		return jwtService.findRoles(token);
	}

  public void resetPassword(Long idCredencial) {
		credencialUsuarioService.resetPassword(idCredencial);
  }
}

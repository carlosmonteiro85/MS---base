package com.prototype.security.api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prototype.security.api.dto.request.AuthenticationRequest;
import com.prototype.security.api.dto.request.RegisterRequest;
import com.prototype.security.api.dto.response.CredencialUsuarioResponse;
import com.prototype.security.api.dto.response.RoleRespose;
import com.prototype.security.domain.service.AuthenticationService;
import com.prototype.security.domain.service.LogoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;
	private final LogoutService logoutService;

	@PostMapping("/register")
	public ResponseEntity<CredencialUsuarioResponse> register(@RequestBody @Valid RegisterRequest request) {
		return ResponseEntity.ok(service.salvarCredencial(request));
	}

	@PostMapping
	public ResponseEntity<CredencialUsuarioResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@GetMapping
	public ResponseEntity<Void> validarToken(@RequestParam(required = true) String token) {
		service.validarToken(token);
		return ResponseEntity.ok().build();
	}

	@GetMapping("logout")
	public ResponseEntity<Void> logOut(HttpServletRequest request, HttpServletResponse response) {
		logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/roles")
	public ResponseEntity<List<RoleRespose>> getRoles(@RequestParam(required = true) String token) {
		List<RoleRespose> roles = service.getRoles(token);

		if(roles.isEmpty()){
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok().body(roles);
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}
}
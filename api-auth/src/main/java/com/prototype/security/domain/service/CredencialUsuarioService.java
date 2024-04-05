package com.prototype.security.domain.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prototype.security.api.dto.UsuarioResquest;
import com.prototype.security.api.dto.request.RegisterRequest;
import com.prototype.security.api.dto.response.CredencialResponse;
import com.prototype.security.api.dto.response.PerfilResponse;
import com.prototype.security.api.dto.response.SampleItemRequest;
import com.prototype.security.domain.exception.ObjectNotFoundException;
import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.model.Perfil;
import com.prototype.security.domain.repository.CredencialUsuarioRepository;
import com.prototype.security.domain.repository.PerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredencialUsuarioService {

  private final CredencialUsuarioRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final PerfilRepository perfilRepository;

  public CredencialUsuario save(RegisterRequest request) {
    CredencialUsuario credencialUsuario = build(request);
    return repository.save(credencialUsuario);
  }

  private CredencialUsuario build(RegisterRequest request) {
    return CredencialUsuario.builder()
        .username(gerarUsername(request))
        .cpf(request.getCpf())
        .email(request.getEmail())
        .perfils(request.getPerfils().stream()
            .map(p -> perfilRepository.findById(p).get()).collect(Collectors.toSet()))
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
  }

  public CredencialUsuario findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado uma credencial com o id: " + id));
  }

  private String gerarUsername(RegisterRequest request) {
    return new StringBuilder()
        .append(request.getCpf())
        .append("-")
        .append(request.getNome()).toString();
  }

  public CredencialUsuario buscarUsuarioPeloLogin(String login) {
    return findByLogin(login)
        .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado com estas credenciais de login."));
  }

  public Optional<CredencialUsuario> findByLogin(String login) {
    Optional<CredencialUsuario> credencial = repository.findByUsername(login);

    if (credencial.isEmpty()) {
      credencial = repository.findByEmail(login);
    }

    if (credencial.isEmpty()) {
      credencial = repository.findByCpf(login);
    }

    return credencial;
  }

  public CredencialResponse bindCredencialRespose(CredencialUsuario credencial) {
    Optional<Perfil> perfil = credencial.getPerfils().stream().findFirst();
    return CredencialResponse.builder()
        .id(credencial.getId())
        .username(credencial.getUsername())
        .cpf(credencial.getCpf())
        .ativo(credencial.isEnabled())
        .email(credencial.getEmail())
        .perfils(Arrays.asList(bindPerfilResponse(perfil.get())))
        .build();
  }

  public PerfilResponse bindPerfilResponse(Perfil perfil) {
    return PerfilResponse.builder()
        .id(perfil.getId())
        .descricao(perfil.getDescricao())
        .permissoes(perfil.getPermisoes().stream()
            .map(p -> new SampleItemRequest(p.getId(), p.getTipoPermisao().getDescricao())).toList())
        .build();
  }

  public void resetPassword(Long idCredencial) {
    CredencialUsuario credencial = findById(idCredencial);
    credencial.setPassword(passwordEncoder.encode(credencial.getCpf()));
    repository.save(credencial);
  }

  public void setErrorTentativaPassword(CredencialUsuario credencial, int tentativas) {
    credencial.setQtPasswordError(tentativas);
    repository.save(credencial);
  }

  public void updateCredencial(CredencialUsuario credencial, UsuarioResquest request) {
    credencial.setCpf(request.getCpf());
    credencial.setEmail(request.getEmail());
    updatePerfils(credencial, request);
  }

  public void updatePerfils(CredencialUsuario credencial, UsuarioResquest request) {
    boolean contemPerfil = credencial.getPerfils().stream().map(Perfil::getId).toList().contains(request.getPerfil());
    if (Boolean.FALSE.equals(contemPerfil)) {
      credencial.getPerfils().clear();
      Perfil novoPerfil = findPerfilById(request.getPerfil());
      credencial.getPerfils().add(novoPerfil);
    }
  }

  public Perfil findPerfilById(Long idPerfil) {
    return perfilRepository.findById(idPerfil)
        .orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado um perfil com este id: " + idPerfil));
  }
}

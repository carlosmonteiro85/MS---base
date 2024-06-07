package com.prototype.security.domain.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prototype.security.api.dto.UsuarioResquest;
import com.prototype.security.api.dto.request.FiltrosRequest;
import com.prototype.security.api.dto.request.RegisterRequest;
import com.prototype.security.api.dto.request.UserProfile;
import com.prototype.security.api.dto.response.CredencialUsuarioResponse;
import com.prototype.security.api.dto.response.SampleItemRequest;
import com.prototype.security.api.dto.response.UsuarioResponse;
import com.prototype.security.api.dto.response.UsuarioResponseFilter;
import com.prototype.security.domain.exception.ObjectNotFoundException;
import com.prototype.security.domain.model.CredencialUsuario;
import com.prototype.security.domain.model.Usuario;
import com.prototype.security.domain.model.util.Utilitarios;
import com.prototype.security.domain.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final EspecialidadeService especialidadeService;
  private final CredencialUsuarioService credencialService;
  private final JwtService jwtService;

  private final AuthenticationService authService;

  public Page<UsuarioResponse> findAll(PageRequest pageRequest) {
    List<UsuarioResponse> usuarios = usuarioRepository.findAll().stream().map(this::bindUsuarioResponse).toList();
    return Utilitarios.getPagina(usuarios, pageRequest.getPageNumber(), pageRequest.getPageSize());
  }

  @Transactional
  public Usuario save(UsuarioResquest usuario) {
    CredencialUsuarioResponse credencial = criarCredencial(usuario);
    Usuario usuarioIn = bindUsuario(usuario);
    usuarioIn.setCredencial(new CredencialUsuario(credencial.getId()));

    if (Objects.nonNull(usuario.getEspecialidade())) {
      usuarioIn.setEspecialidade(especialidadeService.findById(usuario.getEspecialidade()));
    }
    usuarioRepository.save(usuarioIn);
    return usuarioIn;
  }

  public Usuario bindUsuario(UsuarioResquest usuario) {
    return Usuario.builder()
        .nome(usuario.getNome())
        .celular(usuario.getCelular())
        .telefone(usuario.getTelefone())
        .especialidade(Objects.nonNull(usuario.getEspecialidade())
            ? especialidadeService.findById(usuario.getEspecialidade())
            : null)
        .dataNacimento(usuario.getDataNacimento())
        .build();
  }

  public UsuarioResponse getById(Long id) {
    Usuario usuario = findById(id);
    return bindUsuarioResponse(usuario);
  }

  private Usuario findById(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("Usuario não encontrado"));
  }

  public UsuarioResponse bindUsuarioResponse(Usuario usuario) {
    CredencialUsuario creddencial = credencialService.findById(usuario.getCredencial().getId());
    return UsuarioResponse.builder()
        .id(usuario.getId())
        .nome(usuario.getNome())
        .celular(usuario.getCelular())
        .telefone(usuario.getTelefone())
        .especialidade(Objects.nonNull(usuario.getEspecialidade())
            ? new SampleItemRequest(usuario.getEspecialidade().getId(), usuario.getEspecialidade().getDescricao())
            : null)
        .dataNacimento(usuario.getDataNacimento())
        .credencial(credencialService.bindCredencialRespose(creddencial))
        .avatar(usuario.getAvatar())
        .build();
  }

  public void updateUser(Long id, UsuarioResquest request) {
    Usuario usuario = findById(id);
    usuario.setNome(request.getNome());
    usuario.setCelular(request.getCelular());
    usuario.setTelefone(request.getTelefone());
    usuario.setDataNacimento(request.getDataNacimento());
    usuario.setAvatar(request.getAvatar());

    credencialService.updateCredencial(usuario.getCredencial(), request);

    if(usuario.getCredencial().getPerfils().stream().anyMatch(p -> p.getDescricao().equals("MEDICO"))){
      usuario.setEspecialidade(especialidadeService.findById(request.getEspecialidade()));
    }else{
      usuario.setEspecialidade(null);
    }
    usuarioRepository.save(usuario);
  }

  @Transactional
  public void deleteUser(Long id) {
    Usuario usuario = findById(id);
    CredencialUsuario credencial = usuario.getCredencial();
    usuarioRepository.deleteById(usuario.getId());
    credencialService.deletar(credencial);
  }

  private CredencialUsuarioResponse criarCredencial(UsuarioResquest usuario) {
    RegisterRequest credencial = bindRegisterRequest(usuario);
    return authService.salvarCredencial(credencial);

  }

  private RegisterRequest bindRegisterRequest(UsuarioResquest usuario) {
    return RegisterRequest.builder()
        .nome(usuario.getNome())
        .cpf(usuario.getCpf())
        .email(usuario.getEmail())
        .password(usuario.getCpf())
        .perfils(Arrays.asList(usuario.getPerfil()))
        .build();
  }

  public UserProfile getUsuaProfile(String token) {
    String username = jwtService.extractUsername(token);
    return finByUsernome(username);
  }

  public UserProfile finByUsernome(String userName) {
    Optional<Usuario> optionalUsuario = usuarioRepository.finByUsernome(userName);

    if (!optionalUsuario.isPresent()) {
      optionalUsuario = usuarioRepository.finByCpf(userName);
    }

    if (!optionalUsuario.isPresent()) {
      optionalUsuario = usuarioRepository.finByEmail(userName);
    }

    if (!optionalUsuario.isPresent()) {
      throw new ObjectNotFoundException("Não foi encontrado usuario com o username" + userName);
    }

    return bindUserProfile(optionalUsuario.get());
  }

  public UserProfile bindUserProfile(Usuario usuario) {
    return UserProfile.builder()
        .id(usuario.getId())
        .nome(usuario.getNome())
        .avatar(usuario.getAvatar())
        .build();
  }

  public Page<UsuarioResponseFilter> findAllFilter(PageRequest pageRequest, @Valid FiltrosRequest filtros) {
   return usuarioRepository.findAllFilters(
      filtros.getNome(), 
      filtros.getCpf(), 
      filtros.getDataInicio(), 
      filtros.getDataFim(), 
      filtros.getEspecialidades(), 
      filtros.getPerfils(), 
      pageRequest) ;
  }
}

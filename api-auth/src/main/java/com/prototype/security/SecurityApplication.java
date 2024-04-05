package com.prototype.security;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.prototype.security.api.dto.UsuarioResquest;
import com.prototype.security.api.dto.request.AuthenticationRequest;
import com.prototype.security.api.dto.response.CredencialUsuarioResponse;
import com.prototype.security.domain.model.Especialidades;
import com.prototype.security.domain.model.Perfil;
import com.prototype.security.domain.model.Permissao;
import com.prototype.security.domain.model.enuns.TipoPermissaoEnum;
import com.prototype.security.domain.repository.EspecialidadeRepository;
import com.prototype.security.domain.repository.PerfilRepository;
import com.prototype.security.domain.service.AuthenticationService;
import com.prototype.security.domain.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @EnableJpaAuditing
// @EnableEnversRepositories
@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	// @Bean
	// public CommandLineRunner commandLineRunner(AuthenticationService service, UsuarioService usuarioService, PerfilRepository perfilRepository, EspecialidadeRepository especialidadeRepository) {
	// 	return args -> {

	// 		especialidadeRepository.saveAll(List.of(
	// 				new Especialidades(1L, "Clinico Geral"),
	// 				new Especialidades(2L, "Pediatra"),
	// 				new Especialidades(3L, "Cirurgião"),
	// 				new Especialidades(4L, "Nutricionista"),
	// 				new Especialidades(5L, "Psiquiatra")));

	// 		Permissao create = Permissao.builder().tipoPermisao(TipoPermissaoEnum.CREATE).build();
	// 		Permissao read = Permissao.builder().tipoPermisao(TipoPermissaoEnum.READ).build();
	// 		Permissao update = Permissao.builder().tipoPermisao(TipoPermissaoEnum.UPDATE).build();
	// 		Permissao delete = Permissao.builder().tipoPermisao(TipoPermissaoEnum.DELETE).build();

	// 		Perfil perfilAdm = Perfil.builder()
	// 				.id(1L)
	// 				.descricao("ADMIN")
	// 				.permisoes(Set.of(create, read, update, delete))
	// 				.build();

	// 		Perfil perfilRoot = Perfil.builder()
	// 				.id(2L)
	// 				.descricao("ROOT")
	// 				.permisoes(Set.of(create, read, update, delete))
	// 				.build();

	// 		Perfil perfilCliente = Perfil.builder()
	// 		    .id(3L)
	// 				.descricao("CLIENTE")
	// 				.permisoes(Set.of(read))
	// 				.build();

	// 		Perfil perfilMedico = Perfil.builder()
	// 		    .id(4L)
	// 				.descricao("MEDICO")
	// 				.permisoes(Set.of(read))
	// 				.build();

	// 		perfilRepository.saveAll(List.of(perfilRoot, perfilAdm, perfilCliente, perfilMedico));

	// 		var userRequest = UsuarioResquest.builder()
	// 					.nome("Nome Usuario")
	// 					.cpf("12345678910")
	// 					.celular("999999999")
	// 					.telefone("8888888888")
	// 					.perfil(perfilRoot.getId())
	// 					// .permissoes(perfilRoot.getPermisoes().stream().map(p -> p.getId()).toList())
	// 					.avatar("avatar/avatar1.png")
	// 					.dataNacimento(LocalDate.of(1985, 9 , 27))
	// 					.email("admin@mail.com")
	// 				.build();

	// 				usuarioService.save(userRequest);

	// 				CredencialUsuarioResponse authenticate = 
	// 					service.authenticate(new AuthenticationRequest(userRequest.getCpf(), userRequest.getCpf()));

	// 		log.info(authenticate.getAccessToken());			
	// 	};
	// }
}

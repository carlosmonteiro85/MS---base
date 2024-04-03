package com.prototype.security.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credenciais")
public class CredencialUsuario implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "cpf", unique = true, nullable = false)
	private String cpf;

	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	private String password;

	@Builder.Default
	@Column(name = "qt_password_error")
	private Integer qtPasswordError = 0;
	
	@Builder.Default
	@Column(name = "blocked")
	private Boolean isBlocked = Boolean.FALSE; 

	@Builder.Default
	@Column(name = "fist_accesse")
	private Boolean fistAccesse = Boolean.TRUE; 

	@Builder.Default
	@Column(name = "ativo")
	private Boolean ativo = Boolean.TRUE; 

	@Builder.Default
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})@JoinTable(name = "credencial_perfil", 
    joinColumns = @JoinColumn(name = "id_credencial"), inverseJoinColumns = @JoinColumn(name = "id_perfil"))
  private Set<Perfil> perfils = new HashSet<>();

	@OneToMany(mappedBy = "credencial")
	private List<Token> tokens;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
			List<GrantedAuthority> list = new ArrayList<>();
			for (Perfil perfil : perfils) {
					list.addAll(perfil.getAuthorities());
			}
			return list;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {

		if (Objects.nonNull(username)) {
			return username;
		}

		if (Objects.nonNull(cpf)) {
			return cpf;
		}

		if (Objects.nonNull(email)) {
			return email;
		}
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.isBlocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.ativo;
	}

	public CredencialUsuario(Long id){
		this.id = id;
	}
}

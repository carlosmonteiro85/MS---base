package com.prototype.security.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
@Table(name = "perfils")
public class Perfil {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "descricao", unique = true, nullable = false)
  private String descricao;

  @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
  @JoinTable(name = "perfils_permisao", joinColumns = @JoinColumn(name = "id_perfil"), inverseJoinColumns = @JoinColumn(name = "id_permissao"))
  private Set<Permissao> permisoes;

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Permissao permissao : permisoes) {
      authorities.add(new SimpleGrantedAuthority(permissao.getTipoPermisao().toString()));
    }
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.descricao.toUpperCase()));
    return authorities;
  }
}

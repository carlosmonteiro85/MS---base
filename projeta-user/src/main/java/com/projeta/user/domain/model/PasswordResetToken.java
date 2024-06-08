package com.projeta.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.projeta.user.domain.model.util.AppConstants;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "password_reset")
public class PasswordResetToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "credencial_id", nullable = false)
  private CredencialUsuario credencial;

  private String token;

  private LocalDateTime expirationTime;

  public PasswordResetToken() {
    this.token = UUID.randomUUID().toString();
    this.expirationTime = LocalDateTime.now().plusMinutes(AppConstants.MINUTOS_VALIDADE_RESET_TOKEN);
  }

  public PasswordResetToken(CredencialUsuario credencial) {
    this.credencial = credencial;
    this.token = UUID.randomUUID().toString();
    this.expirationTime = LocalDateTime.now().plusMinutes(AppConstants.MINUTOS_VALIDADE_RESET_TOKEN);
  }
}

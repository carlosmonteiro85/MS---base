package com.projeta.user.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.projeta.user.api.dto.EmailDTO;
import com.projeta.user.domain.exception.NegocioException;
import com.projeta.user.domain.model.CredencialUsuario;
import com.projeta.user.domain.model.PasswordResetToken;
import com.projeta.user.domain.model.util.AppConstants;
import com.projeta.user.domain.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

  private final CredencialUsuarioService credenciaService;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final EmailService emailService;

  @Value("${url.base}")
  private String urlBase = "";

  public void restaurarPassword(String email) {
    Optional<CredencialUsuario> credencial = credenciaService.buscarCredencialPeloEmail(email);

    if (credencial.isPresent()) {
      PasswordResetToken passwordResetToken = new PasswordResetToken(credencial.get());
      passwordResetTokenRepository.save(passwordResetToken);

      String linkRestore = urlBase + "restaurar-senha/" + passwordResetToken.getToken();
      EmailDTO emailSend = emailService.gerarEmailRestauracaoSenha(linkRestore, email);
      emailService.enviarEmailComAnexo(emailSend, AppConstants.ENVIO_MANUAL);
    }
  }

  public PasswordResetToken findByToken(String token) {
    PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
        .orElseThrow(() -> new NegocioException("Não foi encontrado uma solicitação de restauração de senha."));

    if (isExpirado(resetToken)) {
      throw new NegocioException("O link de validação de senha está expirado, favor crie uma nova solicitação");
    }
    return resetToken;
  }

  private boolean isExpirado(PasswordResetToken resetToken) {
    return  LocalDateTime.now().isAfter(resetToken.getExpirationTime());
  }

  public boolean isValid(String token) {
    return passwordResetTokenRepository.findByToken(token)
        .map(t -> !isExpirado(t))
        .orElse(false);
  }
}

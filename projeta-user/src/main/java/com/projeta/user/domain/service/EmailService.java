package com.projeta.user.domain.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.projeta.user.api.dto.EmailAnexoDTO;
import com.projeta.user.api.dto.EmailDTO;
import com.projeta.user.config.mail.JavaMailConfigImpl;
import com.projeta.user.domain.exception.EmailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    private JavaMailConfigImpl javaMailConfig;
    private TemplateEngine templateEngine;

    public EmailService(JavaMailConfigImpl javaMailConfig, TemplateEngine templateEngine){
        this.javaMailConfig = javaMailConfig;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private Integer porta;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.email-remetente}")
    private String remetente;

    public void enviarEmailComAnexo(EmailDTO email, String tipoMensagem) {

        JavaMailSender javaMail = obterConfigEmail();

        try {
            MimeMessage mimeMessage = javaMail.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            if (email.getEmailDe().isBlank() || Objects.isNull(email.getEmailDe())) {
                helper.setFrom(remetente);
            } else {
                helper.setFrom(new InternetAddress(remetente, email.getEmailDe()));
            }

            String[] to = email.getEmailPara().stream().filter(str -> !str.isEmpty()).toArray(String[]::new);
            helper.setTo(to);
            
            if(email.getEmailParaCC() != null && !email.getEmailParaCC().isEmpty()) {
            	String[] cc = email.getEmailParaCC().stream().filter(str -> !str.isEmpty()).toArray(String[]::new);
                helper.setCc(cc);
            }
            
            helper.setSubject(email.getAssunto());

            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("tipoMensagem", tipoMensagem);

            String content = templateEngine.process("email-template", context);

            helper.setText(content, true);

            gerarAnexos(email, helper);

            javaMail.send(mimeMessage);

        } catch (MessagingException | MailException | IOException e) {
            log.error(e.getMessage());
            throw new EmailException(
                    "Ocorreu um erro ao tentar enviar o email. Por favor, aguarde um momento e tente novamente.");
        }
        log.info("Email enviado com sucesso.");
    }

    private void gerarAnexos(EmailDTO email, MimeMessageHelper helper) throws MessagingException {
        if (Objects.nonNull(email.getAnexos()) && !email.getAnexos().isEmpty()) {
            for (EmailAnexoDTO anexo : email.getAnexos()) {
                InputStreamSource inputStreamSource = new ByteArrayResource(anexo.getConteudo());
                helper.addAttachment(anexo.getNome(), inputStreamSource, anexo.getExtensao());
            }
        }
    }

    private JavaMailSender obterConfigEmail() {
        return javaMailConfig.getJavaMailSender(
                smtpHost,
                porta,
                userName,
                password);
    }

    public EmailDTO gerarEmailRestauracaoSenha(String linkRestore, String email){
        return EmailDTO.builder()
                    .assunto("Restauração de senha")
                    .emailPara(List.of(email))
                    .emailDe("nao-responda@projeta.com")
                    .textoCorpoEmail(linkRestore)
               .build();
    }
}

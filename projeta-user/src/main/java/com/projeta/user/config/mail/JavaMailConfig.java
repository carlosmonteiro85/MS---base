package com.projeta.user.config.mail;

import org.springframework.mail.javamail.JavaMailSender;

public interface JavaMailConfig {
    JavaMailSender getJavaMailSender(String host, Integer port, String userName, String userPassword);
}

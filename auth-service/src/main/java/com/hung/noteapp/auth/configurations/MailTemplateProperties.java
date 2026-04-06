package com.hung.noteapp.auth.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail.template.register")
public class MailTemplateProperties {

    private String subject;
    private String body;
}

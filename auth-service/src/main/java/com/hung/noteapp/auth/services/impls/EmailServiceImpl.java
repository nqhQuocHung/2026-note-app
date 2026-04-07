package com.hung.noteapp.auth.services.impls;

import com.hung.noteapp.auth.services.EmailService;
import com.hung.noteapp.auth.services.MessageService;
import lombok.RequiredArgsConstructor;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MessageService messageService;

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException(messageService.get("email.recipient_required"));
        }

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException(messageService.get("email.subject_required"));
        }

        if (htmlBody == null || htmlBody.isBlank()) {
            throw new IllegalArgumentException(messageService.get("email.body_required"));
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(to);
            mimeMessage.setSubject(subject, StandardCharsets.UTF_8.name());
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(
                    messageService.get("email.send_failed") + ": " + to,
                    e
            );
        }
    }
}
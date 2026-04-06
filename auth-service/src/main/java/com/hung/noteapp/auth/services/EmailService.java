package com.hung.noteapp.auth.services;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlBody);
}

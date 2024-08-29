package com.rooms.rooms.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    String getVerificationEmailTemplate(String email, String tokenValue);
    String getResetEmailTemplate(String tokenValue, String email);

}

package com.rooms.rooms.email;

import com.rooms.rooms.properties.entity.Properties;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    String getVerificationEmailTemplate(String email, String tokenValue);
    String getResetEmailTemplate(String tokenValue, String email);
    String getConfirmationEmailTemplate(String email, String name, String bookingCode, Properties properties, String firstName, String lastName);
}

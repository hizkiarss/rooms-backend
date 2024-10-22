package com.rooms.rooms.email.impl;

import com.rooms.rooms.email.EmailService;
import com.rooms.rooms.properties.entity.Properties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${FE_URL}")
    private String feUrl;

    @Override
    public String getVerificationEmailTemplate(String email, String tokenValue) {
        String verificationUrl = feUrl+ "verify-email?email=" + email  + "&tokenValue=" + tokenValue ;
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Verification</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #3367D6;\n" +
                "            margin: 0;\n" +
                "            padding: 30px;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            border: 3px solid #94dea5;\n" +
                "            border-radius: 15px; /* Rounded corners */\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);\n" +
                "            max-width: 500px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .email-container img {\n" +
                "            width: 80px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .email-container h2 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 10px;\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .email-container p {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 30px;\n" +
                "            color: #555555;\n" +
                "        }\n" +
                "        .email-container a.button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            font-size: 16px;\n" +
                "            color: #ffffff;\n" +
                "            background-color: #94dea5;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "        .email-container a.button:hover {\n" +
                "            background-color: #2c8769;\n" +
                "        }\n" +
                "        .email-container .link {\n" +
                "            margin-top: 40px;\n" +
                "            font-size: 14px;\n" +
                "            color: #888888;\n" +
                "            word-break: break-all;\n" +
                "            overflow-wrap: break-word;\n" +
                "        }\n" +
                "        .email-container .link a {\n" +
                "            color: #3367D6;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        .email-container .link a:hover {\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "       .email-container img {\n" +
                "            width: 300px;\n" +
                "            height: auto;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <img src=\"https://www.imghost.net/ib/13kWVP9g0mNyTnt_1724735887.png\" alt=\"Verify Email\">\n" +
                "        <h2>Verify your email address</h2>\n" +
                "        <p>You’ve entered <strong>" + email + " </strong> as the email address for your account.<br>\n" +
                "        Please verify this email address by clicking the button below.</p>\n" +
                "        <a class=\"button\" href=\"" + verificationUrl + "\">Verify your email</a>\n" +
                "        <div class=\"link\">\n" +
                "            Or copy and paste this link into your browser:<br>\n" +
                "            <a href=\"" + verificationUrl + "\">" + verificationUrl + "</a>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }


    @Override
    public String getResetEmailTemplate(String tokenValue, String email) {
        String resetEmailUrl = feUrl+"reset-password?token=" + tokenValue + "&email=" + email;
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Password Reset</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            display:flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "            text-align: center;\n" +
                "            justify-content: center;\n" +
                "            background-color: #E0F5F4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            text-align: center;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            background-color: #FFFFFF;\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            border-radius: 8px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            margin: 20px 0;\n" +
                "            display: block;\n" +
                "            margin-left: auto;\n" +
                "            margin-right: auto;\n" +
                "        }\n" +
                "        .illustration {\n" +
                "            width: 100%;\n" +
                "            max-width: 400px;\n" +
                "            height: auto;\n" +
                "            margin: 20px auto;\n" +
                "            display: block;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 16px;\n" +
                "            color: #555555;\n" +
                "            margin: 20px 0;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .reset-button {\n" +
                "            padding: 15px 25px;\n" +
                "            font-size: 16px;\n" +
                "            color: #fffff;\n" +
                "            background-color: #94dea5;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .reset-button:hover {\n" +
                "            background-color: #555555;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <img src=\"https://www.imghost.net/ib/RgR3PvIYZeC1Lsr_1724689026.png\" class=\"logo\" width=\"200\">\n" +
                "        <img src=\"https://www.imghost.net/ib/4zKIhrAVaFOj7gP_1724686728.png\" alt=\"Password Reset Illustration\" class=\"illustration\">\n" +
                "        <p class=\"message\">\n" +
                "            We received a request to reset your password. <br>\n" +
                "            If you didn't make this request, simply ignore this email.\n" +
                "        </p>\n" +
                "        <a href=\"" + resetEmailUrl + "\" style=\"color: #ffffff\" class=\"reset-button\" >RESET</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }


    @Override
    public String getForgetPaswordEmailTemplate(String tokenValue, String email) {
        String resetEmailUrl = feUrl+"forget-password?token=" + tokenValue + "&email=" + email;
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Password Reset</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            display:flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "            text-align: center;\n" +
                "            justify-content: center;\n" +
                "            background-color: #E0F5F4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            text-align: center;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            background-color: #FFFFFF;\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            border-radius: 8px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            margin: 20px 0;\n" +
                "            display: block;\n" +
                "            margin-left: auto;\n" +
                "            margin-right: auto;\n" +
                "        }\n" +
                "        .illustration {\n" +
                "            width: 100%;\n" +
                "            max-width: 400px;\n" +
                "            height: auto;\n" +
                "            margin: 20px auto;\n" +
                "            display: block;\n" +
                "        }\n" +
                "        .message {\n" +
                "            font-size: 16px;\n" +
                "            color: #555555;\n" +
                "            margin: 20px 0;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .reset-button {\n" +
                "            padding: 15px 25px;\n" +
                "            font-size: 16px;\n" +
                "            color: #fffff;\n" +
                "            background-color: #94dea5;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        .reset-button:hover {\n" +
                "            background-color: #555555;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <img src=\"https://www.imghost.net/ib/RgR3PvIYZeC1Lsr_1724689026.png\" class=\"logo\" width=\"200\">\n" +
                "        <img src=\"https://www.imghost.net/ib/4zKIhrAVaFOj7gP_1724686728.png\" alt=\"Password Reset Illustration\" class=\"illustration\">\n" +
                "        <p class=\"message\">\n" +
                "            We received a request to reset your password. <br>\n" +
                "            If you didn't make this request, simply ignore this email.\n" +
                "        </p>\n" +
                "        <a href=\"" + resetEmailUrl + "\" style=\"color: #ffffff\" class=\"reset-button\" >RESET</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @Override
    public String getConfirmationEmailTemplate(String email, String name, String bookingCode, Properties properties, String firstName, String lastName, Integer adult, Integer children, Integer totalNight, String roomName) {
        String url = feUrl + "transaction-detail/"+ bookingCode;
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Booking Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: left;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            max-width: 150px;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            height: auto;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 18px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .booking-details {\n" +
                "            background-color: #f9f9f9;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 15px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .booking-details h2 {\n" +
                "            margin-top: 0;\n" +
                "        }\n" +
                "        .hotel-info img {\n" +
                "            width: 80px;\n" +
                "            height: 80px;\n" +
                "            object-fit: cover;\n" +
                "            float: left;\n" +
                "            margin-right: 15px;\n" +
                "        }\n" +
                "        .hotel-info::after {\n" +
                "            content: \"\";\n" +
                "            display: table;\n" +
                "            clear: both;\n" +
                "        }\n" +
                "        .cta-button {\n" +
                "            display: block;\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            background-color: #4285f4;\n" +
                "            color: white;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <img src=\"https://res.cloudinary.com/dwrm5t649/image/upload/v1726600222/upload/image/mowawhud0zb91b8bxvwf.png\" alt=\"Company Logo\" class=\"logo\">\n" +
                "    </div>\n" +
                "    \n" +
                "    <img src=\"https://res.cloudinary.com/dwrm5t649/image/upload/v1726601608/upload/image/e7iikfhviaqdb7fvxu6y.jpg\" alt=\"Booking Banner\" class=\"banner\">\n" +
                "    \n" +
                "    <div class=\"greeting\">\n" +
                "        Hello " + name + ",\n" +
                "    </div>\n" +
                "    \n" +
                "    <p>Great news! Your booking has been successfully confirmed. Your reservation details are listed below. We look forward to your arrival and a wonderful stay.</p>\n" +
                "    \n" +
                "    <div class=\"booking-details\">\n" +
                "        <h2>Booking Details</h2>\n" +
                "        <p><strong>Booking Code:</strong> #" + bookingCode + "</p>\n" +
                "        \n" +
                "        <div class=\"hotel-info\">\n" +
                "            <h3>" + properties.getName() + "</h3>\n" +
                "            <p>" + properties.getAddress() + "</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <h3>Reservation Information</h3>\n" +
                "        <p>" + 1 + " Room • " + adult + " Adult •  " + children + " Children" +  totalNight +" Nights</p>\n" +
                "        \n" +
                "        <h3>Room Type</h3>\n" +
                "        <p>" + roomName +"</p>\n" +
                "        \n" +
                "        <h3>Guest Name</h3>\n" +
                "        <p>" + firstName + " " + lastName + "</p>\n" +
                "        \n" +
                "    </div>\n" +
                "    \n" +
                "    <a href=\"" + url + "\" class=\"cta-button\">View Your Order</a>\n" +
                "</body>\n" +
                "</html>";
    }

    @Override
    public String getReminderEmailTemplate(String email, String name, String bookingCode, Properties properties, String firstName, String lastName, Integer adult, Integer children, Integer totalNight) {
        String url = feUrl + "transaction-detail/"+ bookingCode;
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Check-in Reminder</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: left;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            max-width: 150px;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "        .banner {\n" +
                "            width: 100%;\n" +
                "            max-width: 600px;\n" +
                "            height: auto;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 18px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .reminder-text {\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .booking-details {\n" +
                "            background-color: #f9f9f9;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 15px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .cta-button {\n" +
                "            display: block;\n" +
                "            width: 100%;\n" +
                "            padding: 10px;\n" +
                "            background-color: #4285f4;\n" +
                "            color: white;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <img src=\"https://res.cloudinary.com/dwrm5t649/image/upload/v1726600222/upload/image/mowawhud0zb91b8bxvwf.png\" alt=\"Company Logo\" class=\"logo\">\n" +
                "    </div>\n" +
                "    <img src=\"https://res.cloudinary.com/dwrm5t649/image/upload/v1726601608/upload/image/e7iikfhviaqdb7fvxu6y.jpg\" alt=\"Booking Banner\" class=\"banner\">\n" +
                "    <div class=\"greeting\">\n" +
                "        Hello " + name + ",\n" +
                "    </div>\n" +
                "    <div class=\"reminder-text\">\n" +
                "        Just a friendly reminder that your amazing stay at " + properties.getName() + " is just around the corner! We can't wait to welcome you tomorrow. Below are your check-in details to get you ready for an unforgettable experience!\n" +
                "    </div>\n" +
                "    <div class=\"booking-details\">\n" +
                "        <h2>Booking Details</h2>\n" +
                "        <p><strong>Booking Code:</strong> #" + bookingCode + "</p>\n" +
                "        <h3>Reservation Information</h3>\n" +
                "        <p>1 Room • " + adult +"  Adult " +  children  + " Children • " + totalNight +" Night</p>\n" +
                "        <h3>Hotel Information</h3>\n" +
                "        <div class=\"hotel-info\">\n" +
                "            <p><strong>" + properties.getName() + "</strong><br />" + properties.getAddress() + "</p>\n" +
                "        </div>\n" +
                "        <h3>Guest Name</h3>\n" +
                "        <p>" + firstName + " " + lastName + "</p>\n" +
                "    </div>\n" +
                "    <a href=\"" + url + "\" class=\"cta-button\">View Your Order</a>\n" +
                "    <p>See you tomorrow! 🎉</p>\n" +
                "</body>\n" +
                "</html>";
    }

//    private String getAmenitiesList(List<String> amenities) {
//        StringBuilder sb = new StringBuilder();
//        for (String amenity : amenities) {
//            sb.append("<li>").append(amenity).append("</li>\n");
//        }
//        return sb.toString();
//    }
}

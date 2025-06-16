package checkinn.controller.mail;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SMTPSMailSender {
    private static final String FROM_EMAIL = "rishavdevtiwari01@gmail.com";
    private static final String FROM_PASSWORD = "ghme jkls rbzr ijpz"; // Use Gmail App Password

    public static boolean sendOtpEmail(String toEmail, String otp) {
        String subject = "Your OTP for Password Reset";
        String body = "Your OTP for password reset is: " + otp;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
package com.LoginPage.login.MailSection;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your Verification Code");

            String htmlMsg = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; background: #f4f6f9;">
                        <div style="max-width: 500px; margin: auto; background: white;
                            padding: 25px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">

                            <h2 style="color:#4f46e5; text-align:center;">🔐 Email Verification</h2>

                            <p style="font-size: 15px; color:#333;">
                                Hello, <br><br>
                                Your verification code is:
                            </p>

                            <div style="text-align:center; margin: 20px 0;">
                                <div style="display:inline-block; padding:15px 25px;
                                    background:#4f46e5; color:white; font-size:28px;
                                    border-radius:8px; letter-spacing:4px;">
                                    """ + code + """
                                </div>
                            </div>

                            <p style="font-size: 14px; color:#555;">
                                This code will expire in <b>5 minutes</b>.
                            </p>

                            <p style="margin-top: 25px; font-size: 13px; color:#999; text-align:center;">
                                Thanks for using our service ✨
                            </p>
                        </div>
                    </div>
                    """;

            helper.setText(htmlMsg, true); // true = HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

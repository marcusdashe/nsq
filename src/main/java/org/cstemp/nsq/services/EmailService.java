package org.cstemp.nsq.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 *
 * @author chibuezeharry & MarcusDashe
 *
 */

@Service
@Slf4j
public class EmailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    TemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@e-limi.africa");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendHTMLMail(String template, Context context, Map<String, String> props) throws MessagingException {

        String messageString = templateEngine.process(template, context);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject(props.get("subject"));
        message.setText(messageString, true);
        message.setFrom(props.get("from"));
        message.setTo(props.get("to"));

        emailSender.send(mimeMessage);
    }
}

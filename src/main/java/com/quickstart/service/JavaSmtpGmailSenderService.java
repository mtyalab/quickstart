package com.quickstart.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class JavaSmtpGmailSenderService {

  private final JavaMailSender emailSender;

  public JavaSmtpGmailSenderService(JavaMailSender emailSender) {
      this.emailSender = emailSender;
  }

  public void sendEmail(String toEmail, String subject, String body) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("UG Christian Rap <" + System.getenv("USER_EMAIL") + ">");
      message.setTo(toEmail);
      message.setSubject(subject);
      message.setText(body);

      emailSender.send(message);

      System.out.println("Message sent successfully");
  }


}

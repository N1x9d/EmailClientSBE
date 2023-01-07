package com.example.demo.controller;

import com.example.demo.ReadEmailPOP3Impl;
import com.example.demo.models.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Properties;
import java.security.GeneralSecurityException;
import java.util.*;

@Controller
public class home {

  private List<Email> emailsBuffer;
  private String _hostPop = "pop.yandex.ru";
  private String _hostSmtp = "smtp.yandex.ru";
  private int _popPort = 995;
  private String _login = "shatalovnick@yandex.ru";//заменить
  private String _password = "**************";//заменить

  @GetMapping("/")
  public String home(Model model) throws MessagingException, GeneralSecurityException {
    var url = "pop3://nsh19021999:Dns102Lg400@pop.yandex.ru:995/INBOX";
    ReadEmailPOP3Impl f = new ReadEmailPOP3Impl(_hostPop);
    emailsBuffer = f.getMails(_login,_password);
    model.addAttribute("Emails", emailsBuffer);
    return "index";
  }

  @GetMapping("/{id}")
  public String OpenMail(@PathVariable("id")String emailId,Model model) throws MessagingException, GeneralSecurityException {
    var email = emailsBuffer.stream()
      .filter((mail) -> emailId.contains(mail.getID()))
      .findFirst()
      .orElse(Email.DEFAULT);
    model.addAttribute("email", email);
    return "EmailView";
  }

  @GetMapping("/write")
  public String OpenMailWriter(Model model) throws MessagingException, GeneralSecurityException {
    return "WriteEmail";
  }

  @PostMapping(value = "/SendEmail")
  public String SendEmail(@RequestBody String form,Model model) throws MessagingException, GeneralSecurityException, JsonProcessingException {
    var r = form;
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> map = mapper.readValue(form, Map.class);
    SendEmail(_login, _password, map);
    return "index";
  }

  private Session setCollectPropertiesForSMTP(String login, String password) throws GeneralSecurityException {
    Properties props = new Properties();
    props.setProperty ("mail.smtp.auth", "true");

    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    props.put("mail.smtp.user", login);
    props.put("mail.smtp.pass", password);
    props.put("mail.smtp.host", _hostSmtp);
    props.setProperty ("mail.smtp.port", "465");

    final Authenticator authenticator = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
          login,
          password
        );
      }
    };
    return Session.getInstance(props, authenticator);
  }

  public void SendEmail(String login, String password, Map MailInfo) {
    try {
      String to = MailInfo.get("Addres").toString();
      var from = login;
      // Create a default MimeMessage object.
      MimeMessage message = new MimeMessage(setCollectPropertiesForSMTP(login, password));

      message.setFrom(new InternetAddress(from));

      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse(to));

      message.setSubject(MailInfo.get("Subject").toString());

      // Put your HTML content using HTML markup
      message.setContent(
        "<div>"+MailInfo.get("Message").toString()+"</div>", "text/html");


      message.setSentDate(new Date());
      // Send message
      Transport.send(message);

      System.out.println("Sent message successfully....");

    } catch (MessagingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }
}

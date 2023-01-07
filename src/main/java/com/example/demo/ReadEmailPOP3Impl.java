package com.example.demo;

import com.example.demo.models.Email;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.GeneralSecurityException;
import java.util.*;

public class ReadEmailPOP3Impl {
  private static Logger logger = LogManager.getLogger(ReadEmailPOP3Impl.class);

  private String host;

  public ReadEmailPOP3Impl(String host) {
    this.host = host;
  }


  public List<Email> getMails(String login, String password) throws GeneralSecurityException, MessagingException {
    POP3Store pop3Store = null;
    List<Email> emailList = null;
    try {
      Session session = setCollectPropertiesForPOP3();
      pop3Store = (POP3Store)session.getStore("pop3");
      pop3Store.connect(host, 995, login, password);
      POP3Folder pop3Folder = (POP3Folder) pop3Store.getFolder("INBOX");
      pop3Folder.open(Folder.READ_WRITE); // Turn on the inbox
      FetchProfile fetchProfile = new FetchProfile();
      fetchProfile.add(FetchProfile.Item.ENVELOPE);
      Message[] messages = pop3Folder.getMessages();
      int length = messages.length;
      System.out.println ("Number of Inboxes:" + length);
      Folder folder = pop3Folder.getStore().getDefaultFolder();
      Folder[] folders = folder.list();
      emailList = new ArrayList<>();

      for (int i = 0; i < length; i++) {
        var email = new Email(messages[i]);
        email.setMessage(messages[i].getContent().toString());
        email.setFrom(messages[i].getFrom());
        email.setReceiveDate(messages[i].getSentDate());
        email.setTitle(messages[i].getSubject());
        emailList.add(email);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if(pop3Store != null){
        pop3Store.close();
      }
    }
    return emailList;
  }
    private Session setCollectPropertiesForPOP3() throws GeneralSecurityException {
      Properties props = new Properties();
      props.setProperty ("mail.popstore.protocol", "POP3"); // use the POP3 protocol
      props.setProperty ("mail.pop3.port", "995"); // port

      MailSSLSocketFactory sf = new MailSSLSocketFactory();
      sf.setTrustAllHosts(true);
      props.put("mail.pop3.ssl.enable",true);
      props.put("mail.pop3.ssl.socketFactory",sf);
      props.setProperty("mail.pop3.host", host);


      return Session.getInstance(props);
    }


}

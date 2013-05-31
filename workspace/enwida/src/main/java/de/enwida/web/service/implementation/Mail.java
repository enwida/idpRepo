package de.enwida.web.service.implementation;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail
{
   public static void SendEmail(String to, String subject,String textMessage) throws AddressException, MessagingException
   {  
      // Sender's email ID needs to be mentioned
      String from = "olcay.tarazan@gmail.com";

      // Assuming you are sending email from localhost
      String host = "smtp.gmail.com";

      String pass = "Safak!123";
      Properties props = System.getProperties();
      props.put("mail.smtp.starttls.enable", "true"); // added this line
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.user", from);
      props.put("mail.smtp.password", pass);
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", "true");

      Session session = Session.getDefaultInstance(props, null);
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));

      message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
  
      message.setSubject(subject);
      message.setText(textMessage);
      Transport transport = session.getTransport("smtp");
      transport.connect(host, from, pass);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
   }
}
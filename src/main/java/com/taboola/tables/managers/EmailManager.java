package com.taboola.tables.managers;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
// import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.google.common.collect.Lists;
import com.taboola.tables.config.TablesConfig;
import com.taboola.tables.db.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by eyal.s on 12/01/2017.
 */
// @Controller
public class EmailManager {

    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    @Autowired
    Gmail gmailService;

    private static MimeMessage createEmail(Collection<User> to,
                                   String from,
                                   String subject,
                                   String bodyText)
            throws MessagingException, javax.mail.MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipients(javax.mail.Message.RecipientType.TO, buildInternetAddresses(to));
        email.setSubject(subject);
        email.setContent(bodyText, "text/html; charset=utf-8");
        return email;
    }

    private static InternetAddress[] buildInternetAddresses(Collection<User> users) {
        List<InternetAddress> internetAddresses = new ArrayList<>(users.size());
        for (User currUser : users) {
            try {
                internetAddresses.add(new InternetAddress(currUser.getMail()));
            } catch (Exception ex) {
                logger.error("Error occurred while resolving email [{}]", currUser.getMail());
            }
        }
        return internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
    }

    private static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException, javax.mail.MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private Message sendMessage(MimeMessage emailContent)
            throws MessagingException, IOException, javax.mail.MessagingException {
        Message message = createMessageWithEmail(emailContent);
        message = gmailService.users().messages().send("me", message).execute();

        return message;
    }

    public void sendEmail(Collection<User> users, String emailHtmlBody) throws MessagingException, javax.mail.MessagingException, IOException {
        String emailBody = buildEmailBodyForTest(users);
        MimeMessage message = createEmail(users, TablesConfig.OWNER_EMAIL, "Lunch by Taboola Tables", emailBody);
        sendMessage(message);
    }

    // @RequestMapping(name = "/sendEmails")
    public void sendEmails() throws MessagingException, IOException, javax.mail.MessagingException {
        Collection<User> users  = Lists.newArrayList(new User("Eyal Segal", "234234", "eyal.s@taboola.com"), new User("Shiran Shwartz", "234234", "shiran.s@taboola.com"),
                new User("Boaz Yaniv", "234234", "boaz.y@taboola.com"), new User("Rami Stern", "234234", "rami.s@taboola.com"));
        sendEmail(users, buildEmailBodyForTest(users));
    }

    private static String buildEmailBodyForTest(Collection<User> users) {
        return "<html><body>" +
                "<h3>You are scheduled to lunch with: </h3>" +
                users.stream().map(user -> "<div>" + user.getName() + "</div>").collect(Collectors.joining()) +
                "</body></html>";

    }
}

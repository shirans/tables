package com.taboola.tables.config;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.common.collect.Lists;
import com.taboola.tables.controllers.LoginController;
import com.taboola.tables.managers.CalendarManager;
import com.taboola.tables.managers.EmailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Configuration
public class TablesConfig {
    public static final String APPLICATION_NAME = "Taboola Tables";
    public static final String OWNER_EMAIL = "tables@taboola.com";

    private static final Logger logger = LoggerFactory.getLogger(TablesConfig.class);

    @Bean
    public GoogleIdTokenVerifier googleTokenVerifier (){

        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport.Builder().build(), new JacksonFactory())
                .setAudience(Collections.singletonList("78184266687-p3205d9dedscipo29ghj0953fdvfddlc.apps.googleusercontent.com"))
                .build();
    }

    @Bean
    public JsonFactory jsonFactory() {
        return new JacksonFactory();
    }

    @Bean
    public HttpTransport transport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public com.google.api.services.calendar.Calendar calendarService(JsonFactory jsonFactory, HttpTransport transport) throws IOException, GeneralSecurityException {
        // Load client secrets.
        /*final InputStream resourceAsStream = TablesConfig.class.getClassLoader().getResourceAsStream("client_secret.json");
        GoogleCredential credential = GoogleCredential.fromStream(resourceAsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();*/
        try {
            InputStream in =
                    TablesConfig.class.getClassLoader().getResourceAsStream("client_secret_calender.json");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
            String path = new ClassPathResource("tokens-calendar").getURL().getFile();
            File tokens;
            if (path.contains("jar")) {
                tokens = new File("/tokens-calendar");
            } else {
                tokens = new File(path);
            }
            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            transport, jsonFactory, clientSecrets, Lists.newArrayList(CalendarScopes.CALENDAR))
                            .setDataStoreFactory(new FileDataStoreFactory(tokens))
                            .setAccessType("offline")
                            .build();
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");

            return new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception ex) {
            logger.error("An error occurred while initializing gmail service", ex);
        }
        return null;
    }

    @Bean
    public Gmail gmailService(JsonFactory jsonFactory, HttpTransport transport) throws IOException {
        try {
            InputStream in =
                    TablesConfig.class.getClassLoader().getResourceAsStream("client_secret_gmail.json");
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
            String path = new ClassPathResource("tokens-gmail").getURL().getFile();
            File tokens;
            if (path.contains("jar")) {
                tokens = new File("/tokens-gmail");
            } else {
                tokens = new File(path);
            }

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            transport, jsonFactory, clientSecrets, Lists.newArrayList(GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND))
                            .setDataStoreFactory(new FileDataStoreFactory(tokens))
                            .setAccessType("offline")
                            .build();
            Credential credential = new AuthorizationCodeInstalledApp(
                    flow, new LocalServerReceiver()).authorize("user");

            return new Gmail.Builder(transport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (Exception ex) {
            logger.error("An error occurred while initializing gmail service", ex);
        }
        return null;
    }

    @Bean
    public CalendarManager calendarManager() {
        return new CalendarManager();
    }

    @Bean
    public EmailManager emailManager() {
        return new EmailManager();
    }


}

package com.taboola.tables.config;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.common.collect.Lists;
import com.taboola.tables.managers.CalendarManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        final InputStream resourceAsStream = CalendarManager.class.getClassLoader().getResourceAsStream("client_secret.json");
        GoogleCredential credential = GoogleCredential.fromStream(resourceAsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public Gmail gmailService(JsonFactory jsonFactory, HttpTransport transport) throws IOException {
        /*final InputStream resourceAsStream = CalendarManager.class.getClassLoader().getResourceAsStream("client_secret.json");
        GoogleCredential credential = GoogleCredential.fromStream(resourceAsStream)
                 .createScoped(Lists.newArrayList(GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND));
                //.createScoped(GmailScopes.all());
        return new Gmail.Builder(transport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        /*InputStream in =
                CalendarManager.class.getClassLoader().getResourceAsStream("client_secret_gmail.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        transport, jsonFactory, clientSecrets, Lists.newArrayList(GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND))
                        .setDataStoreFactory(new MemoryDataStoreFactory())
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");*/
        return null;
    }

    @Bean
    public CalendarManager calendarManager() {
        return new CalendarManager();
    }

}

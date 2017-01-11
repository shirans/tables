package com.taboola.tables.config;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.taboola.tables.managers.CalendarManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Configuration
public class TablesConfig {
    public static final String APPLICATION_NAME = "Taboola Tables";

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
    public com.google.api.services.calendar.Calendar tablesCalendar(JsonFactory jsonFactory) throws IOException, GeneralSecurityException {
        // Load client secrets.
        final InputStream resourceAsStream = CalendarManager.class.getClassLoader().getResourceAsStream("client_secret.json");
        GoogleCredential credential = GoogleCredential.fromStream(resourceAsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new com.google.api.services.calendar.Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public CalendarManager calendarManager() {
        return new CalendarManager();
    }

}

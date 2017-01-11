package com.taboola.tables.config;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Configuration
public class TablesConfig {
    @Bean
    public GoogleIdTokenVerifier googleTokenVerifier (){

        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport.Builder().build(), new JacksonFactory())
                .setAudience(Collections.singletonList("78184266687-jl0emgs5mqnlo0ivo5trn0q93bmq2els.apps.googleusercontent.com"))
                .build();
    }

}

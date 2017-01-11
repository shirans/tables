package com.taboola.tables.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.taboola.tables.db.User;
import com.taboola.tables.db.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by eyal.s on 11/01/2017.
 */
@Controller
public class LoginController {

    @Autowired
    GoogleIdTokenVerifier googleTokenVerifier;

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    public void login(@RequestParam(name = "token") String token) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = googleTokenVerifier.verify(token);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            /*String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");*/

            // Use or store profile information
            // ...
            registerUserIfNeeded(payload);

        } else {
            System.out.println("Invalid ID token.");
        }
    }

    private void registerUserIfNeeded(GoogleIdToken.Payload payload) {
        User user = userRepo.findByGmailId(payload.getEmail());
        if (user == null) {
            User newUser = new User((String) payload.get("name"), payload.getEmail());
            userRepo.save(newUser);
        }
    }
}

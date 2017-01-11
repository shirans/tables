package com.taboola.tables.managers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.common.collect.Lists;
import com.taboola.tables.config.TablesConfig;
import com.taboola.tables.db.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by eyal.s on 11/01/2017.
 */
// @Controller
public class CalendarManager {
    private static final Logger logger = LoggerFactory.getLogger(CalendarManager.class);

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    private static final String CLIENT_ID = "78184266687-jl0emgs5mqnlo0ivo5trn0q93bmq2els.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "t0kcZvjoVRgXCjI_oYOyUGBm";
    private static final String API_TOKEN = "AIzaSyDbDLoQVHnp1SVjcomxm5LTr7j4YYsxzWs";
    private static final String INVITATION_SUMMARY = "Taboola Tables Lunch";
    private static final String INVITATION_LOCATION = "Wherever you want!";
    private static final String INVITATION_COLOR = "Green";
    private static final String OWNER_DISPLAY_NAME = TablesConfig.APPLICATION_NAME;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);


    @Autowired
    com.google.api.services.calendar.Calendar calendarService;

    /*@Value(value="classpath:resources/client_secret.json")
    Resource clientSecret;*/


    public void scheduleLunch(DateTime startTime, int durationInMinutes, List<User> users, TimeZone timezone) throws IOException {
        logger.info("Sending lunch appointment to [{}] on [{}]", users, startTime);
        Event event = new Event()
                .setSummary(INVITATION_SUMMARY)
                .setLocation(INVITATION_LOCATION)
                .setColorId("1");

        EventDateTime start = new EventDateTime()
                .setDateTime(startTime)
                .setTimeZone(timezone.getID());
        event.setStart(start);

        DateTime endDateTime = new DateTime(startTime.getValue() + TimeUnit.MINUTES.toMillis(durationInMinutes));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(timezone.getID());
        event.setEnd(end);

        List<EventAttendee> attendees = users.stream().map(u -> new EventAttendee().setEmail(u.getMail())).collect(Collectors.toList());
        event.setAttendees(attendees);

        String calendarId = "primary";
        event.setOrganizer(new Event.Organizer().setDisplayName(OWNER_DISPLAY_NAME).setEmail(TablesConfig.OWNER_EMAIL));
        event.setCreator(new Event.Creator().setDisplayName(OWNER_DISPLAY_NAME).setEmail(TablesConfig.OWNER_EMAIL));

        event = calendarService.events().insert(calendarId, event).execute();
    }

    /*
    @RequestMapping("/schedule")
    public void schedule() throws IOException {
        User user1 = new User("Eyal Segal", "324234", "eyal.s@taboola.com");
      //  User user2 = new User("Boaz Yaniv", "3242341", "boaz.y@taboola.com");
        scheduleLunch(new DateTime("2017-01-12T06:00:00"), 60, Lists.newArrayList(user1), TimeZone.getTimeZone("Israel"));
    }*/
}

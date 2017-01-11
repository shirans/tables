package com.taboola.tables.json;

import java.util.List;

import com.taboola.tables.db.User;

/**
 * Created by boaz.y on 11/01/2017.
 */
public class Appointment {

    private final long id;
    private final User user;
    private final List<User> participates;
    private final String location;
    private final long appointmentTimestamp;

    public Appointment(long id, User user, List<User> participates, String location, long appointmentTimestamp) {
        this.id = id;
        this.user = user;
        this.participates = participates;
        this.location = location;
        this.appointmentTimestamp = appointmentTimestamp;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getLocation() {
        return location;
    }

    public long getAppointmentTimestamp() {
        return appointmentTimestamp;
    }

    public List<User> getParticipates() {
        return participates;
    }
}

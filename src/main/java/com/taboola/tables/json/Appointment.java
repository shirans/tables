package com.taboola.tables.json;

import java.util.List;

/**
 * Created by boaz.y on 11/01/2017.
 */
public class Appointment {

    private final long id;
    private final String userName;
    private final List<String> participates;
    private final String location;
    private final long appointmentTimestamp;

    public Appointment(long id, String userName, List<String> participates, String location, long appointmentTimestamp) {
        this.id = id;
        this.userName = userName;
        this.participates = participates;
        this.location = location;
        this.appointmentTimestamp = appointmentTimestamp;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getLocation() {
        return location;
    }

    public long getAppointmentTimestamp() {
        return appointmentTimestamp;
    }

    public List<String> getParticipates() {
        return participates;
    }
}

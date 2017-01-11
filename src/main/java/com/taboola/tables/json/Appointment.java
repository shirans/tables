package com.taboola.tables.json;

/**
 * Created by boaz.y on 11/01/2017.
 */
public class Appointment {

    private final long id;
    private final String userName;
    private final String location;
    private final long appointmentTimestamp;

    public Appointment(long id, String userName, String location, long appointmentTimestamp) {
        this.id = id;
        this.userName = userName;
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
}

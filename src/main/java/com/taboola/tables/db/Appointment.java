package com.taboola.tables.db;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * @author shiran.s on 1/11/17.
 */
@Entity
public class Appointment {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private Date appointmentDate;
    private String location;
    private Date createTime = new Date();

    @ManyToMany
    @JoinTable(
            name = "USER_APPOITNEMNT",
            joinColumns = @JoinColumn(name = "APPOINTMENT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    private List<User> users;


    public Appointment(String location, Date appointmentDate, List<User> userList) {
        this.location = location;
        this.appointmentDate = appointmentDate;
        this.users = userList;
    }

    public List<User> getUsers() {
        return users;
    }


    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getLocation() {
        return location;
    }


    public Date getCreateTime() {
        return createTime;
    }
}

package com.taboola.tables.db;

/**
 * @author shiran.s on 1/11/17.
 */

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

    private static final Double NO_SCORE = 0.0;
    private static final String NO_TID = "NO_TID";
    private static final String DEFAULT_PIC = "http://ichef.bbci.co.uk/news/624/media/images/77911000/jpg/_77911764_taboola_headshots_websize-5.jpg";

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String gmailId;
    private String name;
    private Double score = NO_SCORE;
    private String tid = NO_TID;
    private Date creationDate;
    private Date updateDate;
    private long nextAppointmentId;
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Appointment> appointments;

    protected User() {
        Date now = new Date();
        this.creationDate = now;
        this.updateDate = now;
    }

    public User(String name, String gmailId) {
        this();
        this.name = name;
        this.gmailId = gmailId;
        this.picture = DEFAULT_PIC;
    }

    public User(String name, String email, String picture) {
        this();
        this.name = name;
        this.gmailId = email;
        this.picture = picture;
    }


    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String picture;

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setName(String name) {
        this.name = name;
        this.updateDate = new Date();
    }

    public void setTid(String tid) {
        this.tid = tid;
        this.updateDate = new Date();
    }

    public void setScore(Double score) {
        this.score = score;
        this.updateDate = new Date();
    }


    public void setNextAppointmentId(long nextAppointmentId) {
        this.nextAppointmentId = nextAppointmentId;
        this.updateDate = new Date();
    }

    public long getNextAppointmentId() {
        return nextAppointmentId;
    }

    public String getTid() {
        return tid;
    }

    public String getGmailId() {
        return gmailId;
    }

    public Long getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setGmailId(String gmailId) {
        this.gmailId = gmailId;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }


}

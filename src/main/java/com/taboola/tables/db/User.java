package com.taboola.tables.db;

/**
 * @author shiran.s on 1/11/17.
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    private static final Double NO_SCORE = 0.0;
    private static final String NO_TID = "NO_TID";

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Id
    private Long id;
    private String gmailId;
    private String name;
    private Double score = NO_SCORE;
    private String tid = NO_TID;


    public User(String name,String gmailId) {
        this.name = name;
        this.gmailId = gmailId;
    }



    public String getTid() {
        return tid;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setNextAppointmentId(long nextAppointmentId) {
        this.nextAppointmentId = nextAppointmentId;
    }


    public long getNextAppointmentId() {
        return nextAppointmentId;
    }

    private long nextAppointmentId;

    protected User() {}



    public String getGmailId() {
        return gmailId;
    }

    public Long getId() {
        return id;
    }



    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void setGmailId(String gmailId) {
        this.gmailId = gmailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (nextAppointmentId != user.nextAppointmentId) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (gmailId != null ? !gmailId.equals(user.gmailId) : user.gmailId != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (score != null ? !score.equals(user.score) : user.score != null) return false;
        return tid != null ? tid.equals(user.tid) : user.tid == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gmailId != null ? gmailId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (tid != null ? tid.hashCode() : 0);
        result = 31 * result + (int) (nextAppointmentId ^ (nextAppointmentId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", gmailId='" + gmailId + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", tid='" + tid + '\'' +
                ", nextAppointmentId=" + nextAppointmentId +
                '}';
    }
}

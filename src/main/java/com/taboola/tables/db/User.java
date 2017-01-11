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
    private Date creationDate;
    private Date updateDate;
    private long nextAppointmentId;
    @ManyToMany(mappedBy="users")
    private List<Appointment> appointments;

    protected User() {
        Date now = new Date();
        this.creationDate = now;
        this.updateDate = now;
    }

    public User(String name,String gmailId) {
        this();
        this.name = name;
        this.gmailId = gmailId;
    }


    /* set */
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
        if (tid != null ? !tid.equals(user.tid) : user.tid != null) return false;
        if (creationDate != null ? !creationDate.equals(user.creationDate) : user.creationDate != null) return false;
        return updateDate != null ? updateDate.equals(user.updateDate) : user.updateDate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gmailId != null ? gmailId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (tid != null ? tid.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
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
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", nextAppointmentId=" + nextAppointmentId +
                '}';
    }
}

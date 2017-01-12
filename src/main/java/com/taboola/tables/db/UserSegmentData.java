package com.taboola.tables.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author shiran.s on 1/12/17.
 */
@Entity
public class UserSegmentData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tid;
    private String segment;

    public void setTid(String tid) {
        this.tid = tid;
    }

    public UserSegmentData(String tid, String segment) {
        this.tid = tid;
        this.segment = segment;
    }

    public String getTid() {
        return tid;
    }

    public String getSegment() {
        return segment;
    }

    UserSegmentData() {}

    public void setSegment(String segment) {
        this.segment = segment;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSegmentData)) return false;

        UserSegmentData that = (UserSegmentData) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (tid != null ? !tid.equals(that.tid) : that.tid != null) return false;
        return segment != null ? segment.equals(that.segment) : that.segment == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tid != null ? tid.hashCode() : 0);
        result = 31 * result + (segment != null ? segment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserSegmentData{" +
                "id=" + id +
                ", tid='" + tid + '\'' +
                ", segments='" + segment + '\'' +
                '}';
    }
}

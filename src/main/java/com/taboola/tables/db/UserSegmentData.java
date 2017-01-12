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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tid;
    private String segments;

    public String getSegments() {
        return segments;
    }


    public void setTid(String tid) {
        this.tid = tid;
    }

    public UserSegmentData(String tid, String segments) {
        this.tid = tid;
        this.segments = segments;
    }

    public String getTid() {
        return tid;
    }

    public String getSegment() {
        return segments;
    }


    public void setSegments(String segments) {
        this.segments = segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSegmentData)) return false;

        UserSegmentData that = (UserSegmentData) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (tid != null ? !tid.equals(that.tid) : that.tid != null) return false;
        return segments != null ? segments.equals(that.segments) : that.segments == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tid != null ? tid.hashCode() : 0);
        result = 31 * result + (segments != null ? segments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserSegmentData{" +
                "id=" + id +
                ", tid='" + tid + '\'' +
                ", segments='" + segments + '\'' +
                '}';
    }
}

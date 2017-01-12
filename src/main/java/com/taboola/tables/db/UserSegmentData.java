package com.taboola.tables.db;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author shiran.s on 1/12/17.
 */
@Entity
public class UserSegmentData {
    public UserSegmentData(String tid, List<String> segments) {
        this.tid = tid;
        this.segments = segments;
    }

    @Id
    private String tid;
    private List<String> segments;


    public String getTid() {
        return tid;
    }

    public List<String> getSegments() {
        return segments;
    }

    UserSegmentData() {}




    public void setSegments(List<String> segments) {
        this.segments = segments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSegmentData)) return false;

        UserSegmentData that = (UserSegmentData) o;

        if (!tid.equals(that.tid)) return false;
        return segments.equals(that.segments);

    }

    @Override
    public int hashCode() {
        int result = tid.hashCode();
        result = 31 * result + segments.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserSegmentData{" +
                "tid='" + tid + '\'' +
                ", segments=" + segments +
                '}';
    }
}

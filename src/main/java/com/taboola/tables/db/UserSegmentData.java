package com.taboola.tables.db;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author shiran.s on 1/12/17.
 */
@Entity
public class UserSegmentData {
    @Id
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

    UserSegmentData() {
    }


    public void setSegments(String segments) {
        this.segments = segments;
    }

}

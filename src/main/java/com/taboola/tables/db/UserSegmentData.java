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

    UserSegmentData() {
    }

    public void setSegment(String segments) {
        this.segment = segment;
    }

}

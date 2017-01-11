package com.taboola.tables.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author shiran.s on 1/11/17.
 */
@Entity
public class Appoitment {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;
    private Date appDate;
    private String appLocation;
    private String tableEventId;
    private Date createTime = new Date();

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public void setTableEventId(String tableEventId) {
        this.tableEventId = tableEventId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public Date getAppDate() {
        return appDate;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public String getTableEventId() {
        return tableEventId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appoitment)) return false;

        Appoitment that = (Appoitment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (appDate != null ? !appDate.equals(that.appDate) : that.appDate != null) return false;
        if (appLocation != null ? !appLocation.equals(that.appLocation) : that.appLocation != null) return false;
        if (tableEventId != null ? !tableEventId.equals(that.tableEventId) : that.tableEventId != null) return false;
        return createTime != null ? createTime.equals(that.createTime) : that.createTime == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (appDate != null ? appDate.hashCode() : 0);
        result = 31 * result + (appLocation != null ? appLocation.hashCode() : 0);
        result = 31 * result + (tableEventId != null ? tableEventId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}

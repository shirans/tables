package com.taboola.tables.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by boaz.y on 12/01/2017.
 */
@Entity
public class UserDataDirectory {

    @Id
    private Long id;
    private String segmentId;
    private String segmentName;
    private String description;


    public UserDataDirectory(Long id, String segmentId, String segmentName, String description) {
        this.id = id;
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UserDataDirectory{" +
                "id=" + id +
                ", segmentId='" + segmentId + '\'' +
                ", segmentName='" + segmentName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataDirectory that = (UserDataDirectory) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (segmentId != null ? !segmentId.equals(that.segmentId) : that.segmentId != null) return false;
        if (segmentName != null ? !segmentName.equals(that.segmentName) : that.segmentName != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (segmentId != null ? segmentId.hashCode() : 0);
        result = 31 * result + (segmentName != null ? segmentName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}

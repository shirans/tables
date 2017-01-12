package com.taboola.tables.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author shiran.s on 1/12/17.
 */
public interface UserSegmentRepo extends CrudRepository<UserSegmentData, Long> {

    List<UserSegmentData> getLiverampSegemnts(@Param("tid") String tid);


}

package com.taboola.tables.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by boaz.y on 12/01/2017.
 */
@Repository
public interface UserDataDirectoryRepo extends CrudRepository<UserDataDirectory, Long> {

    UserDataDirectory findBySegmentId(String segmentId);

}

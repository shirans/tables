package com.taboola.tables.db;

/**
 * @author shiran.s on 1/11/17.
 */
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    List<User> findByGmailId(String gmailId);
}
package com.taboola.tables.db;

/**
 * @author shiran.s on 1/11/17.
 */

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    User findByGmailId(String gmailId);

    User findByMail(String mail);

    List<User> findById(Long id);

}
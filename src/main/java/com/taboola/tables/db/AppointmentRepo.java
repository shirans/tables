package com.taboola.tables.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shiran.s on 1/11/17.
 */
@Repository
public interface AppointmentRepo extends CrudRepository<Appointment, Long> {

}

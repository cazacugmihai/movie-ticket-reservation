package com.orange.mtr.repository;

import com.orange.mtr.model.Reservation;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link Reservation}s.
 */
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

}

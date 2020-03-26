package com.orange.mtr.service;

import com.orange.mtr.exception.MovieNotFoundException;
import com.orange.mtr.exception.NotEnoughSeatsException;
import com.orange.mtr.exception.ReservationNotFoundException;
import com.orange.mtr.model.Reservation;

/**
 * A service that manages the {@link Reservation}s.
 */
public interface ReservationService {

    /**
     * Creates a reservation based on the input data.
     * <p>
     * The request is validated (the existence of the specified movie, the availability of the requested number of
     * seats, etc.) and, if there are no errors, the reservation is persisted and the available number of seats is
     * updated accordingly.
     *
     * @param movieId the movie ID
     * @param seats   the number of seats
     * @return the reservation
     * @throws MovieNotFoundException  if the movie is not found
     * @throws NotEnoughSeatsException if the requested seats are not available
     */
    Reservation createReservation(String email, long movieId, int seats)
            throws MovieNotFoundException, NotEnoughSeatsException;

    /**
     * Deletes a reservation based on a reservation code and updates accordingly the available number of seats.
     *
     * @param reservationId the reservation code
     * @throws ReservationNotFoundException if the reservation is not found
     */
    void deleteReservation(Long reservationId) throws ReservationNotFoundException;

}

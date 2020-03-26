package com.orange.mtr.service;

import com.orange.mtr.exception.MovieNotFoundException;
import com.orange.mtr.exception.NotEnoughSeatsException;
import com.orange.mtr.exception.ReservationNotFoundException;
import com.orange.mtr.model.Movie;
import com.orange.mtr.model.Reservation;
import com.orange.mtr.repository.MovieRepository;
import com.orange.mtr.repository.ReservationRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * An implementation of {@link ReservationService} that stores the data into a database.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final MovieRepository movieRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(MovieRepository movieRepository,
                                  ReservationRepository reservationRepository) {
        this.movieRepository = movieRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public Reservation createReservation(String email, long movieId, int seats)
            throws MovieNotFoundException, NotEnoughSeatsException {
        // Optimization (2 queries instead of 3):
        // 1. update the availableSeats in one query. If there is no updated records, the reservation cannot be done.
        // 2. create the reservation
        val availableSeats = movieRepository.getAvailableSeats(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        if ((availableSeats == 0) || (seats > availableSeats)) {
            throw new NotEnoughSeatsException(availableSeats);
        }

        movieRepository.addToAvailableSeats(movieId, -seats);

        val reservation = new Reservation()
                .setEmail(email)
                .setMovie(new Movie().setId(movieId))
                .setSeats(seats);
        reservationRepository.save(reservation);

        return reservation;
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) throws ReservationNotFoundException {
        // Optimization (retrieve only the needed data):
        // 1. retrieve from DB only the movie ID and the availableSeats by the reservation ID (by using Projections)
        // 2. execute a procedure that adds the seats to the availableSeats
        // 3. delete the reservation by its ID
        val reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        movieRepository.addToAvailableSeats(reservation.getMovie().getId(), reservation.getSeats());

        reservationRepository.deleteById(id);
    }

}

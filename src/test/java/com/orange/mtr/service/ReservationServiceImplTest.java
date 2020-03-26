package com.orange.mtr.service;

import com.orange.mtr.exception.MovieNotFoundException;
import com.orange.mtr.exception.NotEnoughSeatsException;
import com.orange.mtr.exception.ReservationNotFoundException;
import com.orange.mtr.model.Movie;
import com.orange.mtr.model.Reservation;
import com.orange.mtr.repository.MovieRepository;
import com.orange.mtr.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * The test suite for {@link ReservationServiceImpl}.
 */
@SpringBootTest
public class ReservationServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;

    @BeforeEach
    public void beforeTest() {
        when(movieRepository.save(any()))
                .thenAnswer(invocation -> {
                    Movie movie = invocation.getArgument(0);
                    movie.setId(1L);
                    return movie;
                });

        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> {
                    Reservation reservation = invocation.getArgument(0);
                    reservation.setId(1L);
                    return reservation;
                });

        reservationService = new ReservationServiceImpl(movieRepository, reservationRepository);
    }

    // createReservation

    @Test
    public void giveANonExistingMovie_whenCreateReservation_throwMovieNotFoundException() {
        when(movieRepository.getAvailableSeats(anyLong())).thenThrow(new MovieNotFoundException(-1L));

        assertThrows(MovieNotFoundException.class, () ->
                reservationService.createReservation("mia@email.com", -1L, 2)
        );
    }

    @Test
    public void giveAnExistingMovie_whenCreateReservationWithNoAvailableSeats_throwNotEnoughSeatsException() {
        when(movieRepository.getAvailableSeats(anyLong())).thenReturn(Optional.of(10));

        assertThrows(NotEnoughSeatsException.class, () ->
                reservationService.createReservation("mia@email.com", 1L, 11)
        );
    }

    @Test
    public void giveAnExistingMovie_whenCreateReservation_thenRegistrationIsCreated() {
        when(movieRepository.getAvailableSeats(anyLong()))
                .thenReturn(Optional.of(10));

        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(10);
        movie = movieRepository.save(movie);

        // when
        Reservation reservation = reservationService.createReservation("mia@email.com", movie.getId(), 2);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isGreaterThan(0L);
    }

    @Test
    public void giveAnExistingMovie_whenCreateReservation_thenAvailableSeatsMustBeUpdated() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(10);
        when(movieRepository.getAvailableSeats(anyLong()))
                .thenAnswer(invocation -> Optional.of(movie.getAvailableSeats()));
        when(movieRepository.addToAvailableSeats(anyLong(), anyInt()))
                .thenAnswer(invocation -> {
                    int seats = invocation.getArgument(1);
                    movie.setAvailableSeats(movie.getAvailableSeats() + seats);
                    return 1;
                });
        movieRepository.save(movie);

        // when
        reservationService.createReservation("mia@email.com", movie.getId(), 2);
        Optional<Integer> availableSeats = movieRepository.getAvailableSeats(movie.getId());

        // then
        assertThat(availableSeats).isNotNull();
        assertThat(availableSeats.isPresent()).isTrue();
        assertThat(availableSeats.get()).isEqualTo(8L);
    }

    // deleteReservation

    @Test
    public void giveAnExistingReservation_whenDeleteReservation_thenAvailableSeatsMustBeUpdated() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(10);
        when(movieRepository.getAvailableSeats(anyLong()))
                .thenAnswer(invocation -> Optional.of(movie.getAvailableSeats()));
        when(movieRepository.addToAvailableSeats(anyLong(), anyInt()))
                .thenAnswer(invocation -> {
                    int seats = invocation.getArgument(1);
                    movie.setAvailableSeats(movie.getAvailableSeats() + seats);
                    return 1;
                });
        movieRepository.save(movie);

        Reservation reservation = reservationService.createReservation("mia@email.com", movie.getId(), 2);
        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));

        // when
        reservationService.deleteReservation(reservation.getId());
        Optional<Integer> availableSeats = movieRepository.getAvailableSeats(movie.getId());

        // then
        assertThat(availableSeats).isNotNull();
        assertThat(availableSeats.isPresent()).isTrue();
        assertThat(availableSeats.get()).isEqualTo(10L);
    }

    @Test
    public void giveAnInvalidReservation_whenDeleteReservation_throwReservationNotFoundException() {
        assertThrows(ReservationNotFoundException.class, () ->
                reservationService.deleteReservation(-1L)
        );
    }

}
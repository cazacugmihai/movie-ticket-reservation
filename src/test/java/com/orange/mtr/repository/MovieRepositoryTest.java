package com.orange.mtr.repository;

import com.orange.mtr.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test suite for {@link MovieRepository}.
 */
@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    // getAvailableSeats

    @Test
    public void givenAnExistingMovie_whenGetAvailableSeats_thenReturnAvailableSeats() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(2);
        movieRepository.save(movie);

        // when
        Optional<Integer> availableSeats = movieRepository.getAvailableSeats(movie.getId());

        // then
        assertThat(availableSeats).isNotNull();
        assertThat(availableSeats.isPresent()).isTrue();
        assertThat(availableSeats.get()).isEqualTo(2);
    }

    @Test
    public void giveANonExistingMovie_whenGetAvailableSeats_thenReturnNoAvailableSeats() {
        // when
        Optional<Integer> availableSeats = movieRepository.getAvailableSeats(-1);

        // then
        assertThat(availableSeats).isNotNull();
        assertThat(availableSeats.isPresent()).isFalse();
    }

    // addToAvailableSeats

    @Test
    public void givenAnExistingMovie_whenAddToAvailableSeatsAPositiveNUmber_thenAvailableSeatsMustBeModified() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(5);
        movieRepository.save(movie);

        // when
        int updatedRecords = movieRepository.addToAvailableSeats(movie.getId(), 3);
        Optional<Movie> updatedMovie = movieRepository.findById(movie.getId());

        // then
        assertThat(updatedRecords).isEqualTo(1);
        assertThat(updatedMovie).isNotNull();
        assertThat(updatedMovie.isPresent()).isTrue();
        assertThat(updatedMovie.get().getAvailableSeats()).isEqualTo(8);
    }

    @Test
    public void givenAnExistingMovie_whenAddToAvailableSeatsANegativeNumber_thenAvailableSeatsMustBeModified() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(5);
        movieRepository.save(movie);

        // when
        int updatedRecords = movieRepository.addToAvailableSeats(movie.getId(), -3);
        Optional<Movie> updatedMovie = movieRepository.findById(movie.getId());

        // then
        assertThat(updatedRecords).isEqualTo(1);
        assertThat(updatedMovie).isNotNull();
        assertThat(updatedMovie.isPresent()).isTrue();
        assertThat(updatedMovie.get().getAvailableSeats()).isEqualTo(2);
    }

    @Test
    public void givenAnExistingMovie_whenDecreasingAvailableSeatsUnderZero_thenNoRecordsShouldBeUpdated() {
        // given
        Movie movie = new Movie().setName("movieA").setAvailableSeats(5);
        movieRepository.save(movie);

        // when
        int updatedRecords = movieRepository.addToAvailableSeats(movie.getId(), -6);

        // then
        assertThat(updatedRecords).isEqualTo(0);
    }

    @Test
    public void givenANonExistingMovie_whenAddToAvailableSeats_thenNoRecordsShouldBeUpdated() {
        // when
        int updatedRecords = movieRepository.addToAvailableSeats(-1, 1);

        // then
        assertThat(updatedRecords).isEqualTo(0);
    }

}
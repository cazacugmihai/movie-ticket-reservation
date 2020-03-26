package com.orange.mtr.repository;

import com.orange.mtr.model.Movie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for {@link Movie}s.
 */
public interface MovieRepository extends CrudRepository<Movie, Long> {

    boolean existsByName(String name);

    /**
     * Returns the available seats number of the given movie ID.
     *
     * @param movieId the movie ID
     * @return the available seats number
     */
    @Query("select m.availableSeats from Movie m where m.id = :movieId")
    Optional<Integer> getAvailableSeats(@Param("movieId") long movieId);

    /**
     * Updates the number of available seats by adding the specified number of seats.
     *
     * @param movieId the movie ID
     * @param seats   the number of seats (can be negative)
     * @return the updated records number.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Movie m " +
            "set m.availableSeats = m.availableSeats + :seats " +
            "where m.id = :movieId and m.availableSeats + :seats >= 0")
    int addToAvailableSeats(@Param("movieId") long movieId, @Param("seats") int seats);

}

package com.orange.mtr.exception;

import lombok.Getter;

/**
 * The error used in case the {@link com.orange.mtr.model.Movie} is not found.
 */
@Getter
public class MovieNotFoundException extends RuntimeException {

    private final Long movieId;

    public MovieNotFoundException(Long movieId) {
        this.movieId = movieId;
    }

}

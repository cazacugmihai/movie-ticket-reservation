package com.orange.mtr.exception;

import lombok.Getter;

/**
 * The error used in case the number of requested seats is not available.
 */
@Getter
public class NotEnoughSeatsException extends RuntimeException {

    private final int availableSeats;

    public NotEnoughSeatsException(int availableSeats) {
        this.availableSeats = availableSeats;
    }

}

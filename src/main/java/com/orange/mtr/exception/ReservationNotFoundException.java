package com.orange.mtr.exception;

import lombok.Getter;

/**
 * The error used in case the {@link com.orange.mtr.model.Reservation} is not found.
 */
@Getter
public class ReservationNotFoundException extends RuntimeException {

    private final Long reservationId;

    public ReservationNotFoundException(Long reservationId) {
        this.reservationId = reservationId;
    }

}

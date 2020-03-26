package com.orange.mtr.web.controller;

import com.orange.mtr.exception.dto.ErrorResponse;
import com.orange.mtr.model.Reservation;
import com.orange.mtr.service.ReservationService;
import com.orange.mtr.web.dto.ReservationRequest;
import com.orange.mtr.web.dto.ReservationResponse;
import com.orange.mtr.web.handler.CustomGlobalExceptionHandler;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * A controller that manages the {@link Reservation}s.
 */
@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Creates a reservation based on the input data.
     * <p>
     * In the first step, the request is validated and, if the constraints are fulfilled, another validation (the
     * existence of the specified movie, the availability of the requested number of seats, etc.) is done by
     * checking the database. If there are errors, an {@link ErrorResponse} is sent to the client (see
     * {@link CustomGlobalExceptionHandler}).
     * <p>
     * In the next step, the reservation is persisted and a reservation code is returned to the client.
     *
     * @param request the request
     * @return the reservation code
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request) {
        val reservation = reservationService.createReservation(
                request.getEmail(), request.getMovieId(), request.getSeats());

        // we can use a generated reservation code but, for simplicity, we'll return the entity ID
        return new ReservationResponse(reservation.getId().toString());
    }

    /**
     * Deletes a reservation based on a reservation code.
     * <p>
     * If the reservation doesn't exist, an {@link ErrorResponse} is sent to the client (see
     * {@link CustomGlobalExceptionHandler}).
     * <p>
     * If the reservation is deleted, the available seats number will be updated accordingly.
     *
     * @param reservationCode the reservation code (the ID, for now)
     */
    @DeleteMapping("/{reservation-code}")
    public void delete(@PathVariable("reservation-code") Long reservationCode) {
        reservationService.deleteReservation(reservationCode);
    }

}

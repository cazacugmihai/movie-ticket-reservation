package com.orange.mtr.web.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * The reservation request sent by the client.
 *
 * @see ReservationResponse
 */
@Value
public class ReservationRequest {

    @Email
    @NotNull
    String email;

    @Min(1)
    long movieId;

    @Min(1)
    int seats;

}

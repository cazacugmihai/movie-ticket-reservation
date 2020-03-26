package com.orange.mtr.web.dto;

import lombok.Value;

/**
 * The reservation response received by the client.
 *
 * @see ReservationRequest
 */
@Value
public class ReservationResponse {

    String code;

}

package com.orange.mtr.exception.dto;

import lombok.Value;

import java.util.Date;

/**
 * The error response sent in case of exceptions.
 */
@Value
public class ErrorResponse {

    Date timestamp;
    int status;
    Object error;

}

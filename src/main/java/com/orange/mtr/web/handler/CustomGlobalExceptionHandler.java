package com.orange.mtr.web.handler;

import com.orange.mtr.exception.MovieNotFoundException;
import com.orange.mtr.exception.NotEnoughSeatsException;
import com.orange.mtr.exception.ReservationNotFoundException;
import com.orange.mtr.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * A global exception handler that transforms the exceptions into {@link ErrorResponse}s before sending them to the
 * client.
 *
 * @see ErrorResponse
 */
@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Object> handleMovieNotFoundException(Exception ex) {
        log.error("The movie with ID {} doesn't exist.", ((MovieNotFoundException) ex).getMovieId());
        val message = "The movie doesn't exist.";
        return sendError(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Object> handleReservationNotFoundException(Exception ex) {
        log.error("The reservation with ID {} doesn't exist.", ((ReservationNotFoundException) ex).getReservationId());
        val message = "The reservation doesn't exist.";
        return sendError(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(NotEnoughSeatsException.class)
    public ResponseEntity<Object> handleNotEnoughSeatsException(Exception ex) {
        val seats = ((NotEnoughSeatsException) ex).getAvailableSeats();
        val message = (seats == 0)
                ? "No more seats available on this movie."
                : ("There are only " + seats + " seats available on this movie.");
        log.error(message);

        return sendError(HttpStatus.BAD_REQUEST, message);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error(ex.getMessage());

        val errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                FieldError::getField,
                                Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet())
                        )
                );

        return sendError(status, errors.isEmpty() ? ex.getMessage() : errors);
    }

    private ResponseEntity<Object> sendError(HttpStatus status, Object message) {
        val errorResponse = new ErrorResponse(new Date(), status.value(), message);
        return new ResponseEntity<>(errorResponse, status);
    }

}
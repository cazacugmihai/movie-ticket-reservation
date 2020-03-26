package com.orange.mtr.web.controller;

import com.orange.mtr.exception.ReservationNotFoundException;
import com.orange.mtr.model.Reservation;
import com.orange.mtr.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test suite for {@link ReservationController}.
 */
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    // createReservation

    @Test
    public void givenAnInvalidReservationRequest_whenCreateReservation_thenReturnTheErrors() throws Exception {
        when(reservationService.createReservation(anyString(), anyLong(), anyInt()))
                .thenReturn(new Reservation().setId(1L));

        mockMvc
                .perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\": \"mia@@email.com\"," +
                                        "\"movieId\": -1," +
                                        "\"seats\": -100" +
                                        "}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("error").exists())
                .andExpect(jsonPath("error.email").value("must be a well-formed email address"))
                .andExpect(jsonPath("error.movieId").value("must be greater than or equal to 1"))
                .andExpect(jsonPath("error.seats").value("must be greater than or equal to 1"))
                .andReturn();
    }

    @Test
    public void givenAValidReservationRequest_whenCreateReservation_thenReturnReservationCode() throws Exception {
        when(reservationService.createReservation(anyString(), anyLong(), anyInt()))
                .thenReturn(new Reservation().setId(1L));

        mockMvc
                .perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\": \"mia@email.com\"," +
                                        "\"movieId\": 1," +
                                        "\"seats\": 100" +
                                        "}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("code").value(1L))
                .andReturn();
    }

    // deleteReservation

    @Test
    public void givenANonExistingReservationId_whenDeleteReservation_thenReturnStatusNotFound() throws Exception {
        doThrow(new ReservationNotFoundException(1L))
                .when(reservationService).deleteReservation(anyLong());

        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenAnExistingReservationId_whenDeleteReservation_thenReturnStatusOk() throws Exception {
        mockMvc.perform(delete("/reservations/1"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
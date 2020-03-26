package com.orange.mtr.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * A reservation.
 */
@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(
            generator = "reservationSequence",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "reservationSequence")
    private Long id;

    @NotBlank
    @Email
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String email;

    @ManyToOne
    private Movie movie;

    @Min(1)
    @Column(nullable = false)
    private int seats;

}

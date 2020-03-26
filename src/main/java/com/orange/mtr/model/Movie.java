package com.orange.mtr.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * A movie.
 */
@Entity
//@DynamicUpdate - replaced by MovieRepository#addToAvailableSeats
@Data
public class Movie {

    @Id
    @GeneratedValue(
            generator = "movieSequence",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "movieSequence")
    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private int availableSeats;

}

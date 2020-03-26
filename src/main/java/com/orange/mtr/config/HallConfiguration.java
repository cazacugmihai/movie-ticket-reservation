package com.orange.mtr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The hall configuration.
 */
@Data
@ConfigurationProperties(prefix = "hall")
public class HallConfiguration {

    private Integer seats;

}

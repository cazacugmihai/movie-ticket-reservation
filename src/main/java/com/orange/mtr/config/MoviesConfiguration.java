package com.orange.mtr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * The movie configuration.
 */
@Data
@ConfigurationProperties(prefix = "movie")
public class MoviesConfiguration {

    private List<String> names = new ArrayList<>();

}

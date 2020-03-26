package com.orange.mtr;

import com.orange.mtr.config.HallConfiguration;
import com.orange.mtr.config.MoviesConfiguration;
import com.orange.mtr.model.Movie;
import com.orange.mtr.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("with-data")
public class DBPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private final HallConfiguration hallConfiguration;
    private final MoviesConfiguration moviesConfiguration;
    private final MovieRepository movieRepository;
    private ApplicationContext applicationContext;

    @Autowired
    public DBPopulator(HallConfiguration hallConfiguration,
                       MoviesConfiguration moviesConfiguration,
                       MovieRepository movieRepository) {
        this.hallConfiguration = hallConfiguration;
        this.moviesConfiguration = moviesConfiguration;
        this.movieRepository = movieRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!event.getApplicationContext().equals(applicationContext)) {
            return;
        }

        populateDB();
    }

    public void populateDB() {
        List<String> moviesToImport = moviesConfiguration.getNames();
        boolean existMoviesToImport = moviesToImport != null && !moviesToImport.isEmpty();
        boolean isDBEmpty = movieRepository.count() == 0L;

        if (isDBEmpty && !existMoviesToImport) {
            log.error("There are no movies in DB and the list of movies is not specified in the configuration file.");
            System.exit(1);
        } else if (existMoviesToImport) {
            log.info("Populating DB...");
            for (val movieName : moviesToImport) {
                if (movieRepository.existsByName(movieName)) {
                    log.debug("Movie '{}' already exists.", movieName);
                } else {
                    val movie = new Movie()
                            .setName(movieName)
                            .setAvailableSeats(hallConfiguration.getSeats());
                    log.debug("Saving movie '{}'.", movie.getName());
                    movieRepository.save(movie);
                    log.debug("Saved movie '{}'. ID: {}.", movie.getName(), movie.getId());
                }
            }
            log.info("Populated DB.");
        }
    }

}

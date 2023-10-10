package service;

import entity.Movie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.MovieRepository;

import java.util.List;
import java.util.Optional;

class MovieServiceTest {

    private final MovieRepository movieRepository = new MovieRepository("test_movie.txt");
    private final MovieService movieService = new MovieService(movieRepository);
    @Test
    @DisplayName("영화가 성공적으로 저장된다.")
    public void addMovieTest(){
        movieService.addMovie("Pirate", 180);
        Optional<Movie> actual = movieRepository.findByMovieName("Pirate");
        Assertions.assertThat(actual).isNotEmpty();
        Assertions.assertThat(actual.get().getRunningTime()).isEqualTo(180);
        System.out.println(actual.get().getId());
    }
<<<<<<< HEAD

}
=======
}
>>>>>>> a55e061bda60c3bc8c3310072ea0c83c1a769e32

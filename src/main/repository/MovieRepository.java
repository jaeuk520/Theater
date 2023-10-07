package repository;

import database.StringIdEntityDatabase;
import entity.Movie;
import literal.Path;

import java.util.Objects;
import java.util.Optional;

public class MovieRepository extends StringIdEntityDatabase<Movie> {

    public MovieRepository() {
        super(Movie.class, 4, Path.MOVIE_DATA);
    }

    public Optional<Movie> findByMovieName(String movieName) {
        return findAll().stream().filter(movie -> Objects.equals(movie.getName(), movieName)).findFirst();
    }
}

package repository;

import database.StringIdEntityDatabase;
import entity.Movie;

import java.util.Objects;
import java.util.Optional;

public class MovieRepository extends StringIdEntityDatabase<Movie> {

    public MovieRepository(String path) {
        super(Movie.class, 4, path);
    }

    public Optional<Movie> findByMovieName(String movieName) {
        return findAll().stream()
                .filter(movie -> Objects.equals(movie.getName(), movieName))
                .findFirst();
    }
}
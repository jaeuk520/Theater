package repository;

import database.StringIdEntityDatabase;
import entity.Movie;
import literal.IdStrategy;
import literal.Path;

public class MovieRepository extends StringIdEntityDatabase<Movie> {

    public MovieRepository() {
        super(Movie.class,
                IdStrategy.UUID,
                4,
                Path.MOVIE_DATA);
    }
}

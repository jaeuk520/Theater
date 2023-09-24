import entity.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class MovieManager {

    private HashMap<String, Movie> movies;
    private FileLoader fileLoader;

    public MovieManager(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
        this.movies = fileLoader.getMovies();
    }

    public void addMovie(String name, int runningTime) {
        String id;
        do {
            id = UUID.randomUUID().toString().substring(0, 4).toUpperCase(Locale.ROOT);
        } while(movies.containsKey(id));

        movies.put(id, new Movie(id, name, runningTime));
        fileLoader.saveMovies(movies);
    }

    public int getTotalPages() {
        return movies.size() / 5 + (movies.size() % 5 != 0 ? 1 : 0);
    }

    public boolean hasNextPage(int page) {
        return (page + 1) <= getTotalPages();
    }

    public boolean hasPreviousPage(int page) {
        return (page - 1) >= 1;
    }

    public List<String> getSortedMovieNames() {
        return movies.values()
                .stream()
                .map(Movie::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}

package service;

import entity.Movie;
import repository.MovieRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    private final MovieRepository movieRepository;
    private final int MOVIES_PER_PAGE = 5;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public int getTotalPages() {
        return movieRepository.size() / MOVIES_PER_PAGE + (movieRepository.size() % MOVIES_PER_PAGE != 0 ? 1 : 0);
    }

    public boolean hasNextPage(int page) {
        return (page + 1) <= getTotalPages();
    }

    public boolean hasPreviousPage(int page) {
        return (page - 1) >= 1;
    }

    public List<String> getSortedMovieNames() {
        return movieRepository.findAll()
                .stream()
                .map(Movie::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Movie> getSortedMovies() {
        return movieRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Movie::getName))
                .collect(Collectors.toList());
    }

    public void addMovie(String movieName, int runningTime) {
        movieRepository.save(new Movie(null, movieName, runningTime));
    }
}

package service;

import entity.Movie;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import repository.MovieRepository;

public class MovieService {
    private final MovieRepository movieRepository;
    private final int MOVIES_PER_PAGE = 5;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public int getTotalPages() {
        return movieRepository.size() / 5 + (movieRepository.size() % 5 != 0 ? 1 : 0);
    }

    public boolean hasNextPage(int page) {
        return (page + 1) <= getTotalPages();
    }

    public boolean hasPreviousPage(int page) {
        return (page - 1) >= 1;
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

    public void deleteMovie(String movieId) {
        movieRepository.delete(movieId);
    }

    public void modifyMovieName(Movie movie, String movieName) {
        movieRepository.modify(movie.getId(), new Movie(movie.getId(), movieName, movie.getRunningTime()));
    }

    public void modifyMovieRunningTime(Movie movie, int runningTime) {
        movieRepository.modify(movie.getId(), new Movie(movie.getId(), movie.getName(), runningTime));
    }

    public void modifyMovieName(Movie movie, String movieName, int runningTime) {
        movieRepository.modify(movie.getId(), new Movie(movie.getId(), movieName, runningTime));
    }
}

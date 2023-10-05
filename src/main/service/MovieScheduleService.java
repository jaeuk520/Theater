package service;

import repository.MovieScheduleRepository;

public class MovieScheduleService {
    public final MovieScheduleRepository movieScheduleRepository;
    public MovieScheduleService(MovieScheduleRepository movieScheduleRepository) {
        this.movieScheduleRepository = movieScheduleRepository;
    }
}

package service;

import entity.Movie;
import entity.MovieSchedule;
import repository.MovieScheduleRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MovieScheduleService {
    public final MovieScheduleRepository movieScheduleRepository;

    public MovieScheduleService(MovieScheduleRepository movieScheduleRepository) {
        this.movieScheduleRepository = movieScheduleRepository;
    }

    public void addMovieSchedule(String scheduleId, Movie movie, LocalDate localDate, LocalTime localTime, Long roomNumber) {
        movieScheduleRepository.save(new MovieSchedule(scheduleId, movie, localDate, localTime, roomNumber));
    }

    public int getTotalPages(int sz) {
        return sz / 5 + (sz % 5 != 0 ? 1 : 0);
    }

    public boolean hasNextPage(int page, int totalPage) {
        return (page + 1) <= totalPage;
    }

    public boolean hasPreviousPage(int page) {
        return (page - 1) >= 1;
    }

    // MovieSchedule을 돌면서 주어진 Movie, RoomNumber에 해당하는

    public List<LocalTime> getSortedMovieSchedules(String movieId, LocalDate localDate, Long roomNumber) {
        return movieScheduleRepository.findAll().stream().filter(movieSchedule -> Objects.equals(movieSchedule.getMovie().getId(), movieId))
                .filter(movieSchedule -> Objects.equals(movieSchedule.getLocalDate(), localDate))
                .filter(movieSchedule -> Objects.equals(Long.parseLong(movieSchedule.getRoom().getRoomNumber()), roomNumber))
                .map(MovieSchedule::getLocalTime)
                .sorted()
                .collect(Collectors.toList());
    }
}
package service;

import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
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

    public void addMovieSchedule(String scheduleId, Movie movie, LocalDate localDate, LocalTime localTime, Room room) {
        movieScheduleRepository.save(new MovieSchedule(scheduleId, movie, localDate, localTime, room));
    }

    public List<LocalTime> getMovieSchedules(String movieId, LocalDate localDate, Long roomNumber) {
        return movieScheduleRepository.findAll().stream().filter(movieSchedule -> Objects.equals(movieSchedule.getMovie().getId(), movieId))
                .filter(movieSchedule -> Objects.equals(movieSchedule.getLocalDate(), localDate))
                .filter(movieSchedule -> Objects.equals(movieSchedule.getRoom().getRoomNumber(), roomNumber))
                .map(MovieSchedule::getLocalTime)
                .sorted()
                .collect(Collectors.toList());
    }
}

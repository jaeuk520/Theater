package service;

import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import repository.MovieScheduleRepository;

public class MovieScheduleService {
    public final MovieScheduleRepository movieScheduleRepository;

    public MovieScheduleService(MovieScheduleRepository movieScheduleRepository) {
        this.movieScheduleRepository = movieScheduleRepository;
    }

    public void addMovieSchedule(String scheduleId, Movie movie, LocalDate localDate, LocalTime localTime,
                                 Long roomNumber) {
        movieScheduleRepository.save(new MovieSchedule(scheduleId, movie, localDate, localTime, roomNumber));
    }


    // 영화와 날짜, 상영관이 주어질 때 영화 상영 시간을 반환
    public List<LocalTime> getMovieStartAtTimeByDateAndRoomNumber(String movieId, LocalDate localDate,
                                                                  Long roomNumber) {
        return movieScheduleRepository.findAllMoviesStartAtTimeByDateAndRoomNumber(movieId, localDate, roomNumber);
    }

    //
    public List<Movie> getDistinctMoviesByStartDateAtAndRoomNumber(LocalDate startDateAt, Long roomNumber) {
        return movieScheduleRepository.findAll().stream()
                .filter(movieSchedule -> Objects.equals(movieSchedule.getStartAtDate(), startDateAt))
                .filter(movieSchedule -> Objects.equals(Long.parseLong(movieSchedule.getRoom().getRoomNumber()),
                        roomNumber)).map(MovieSchedule::getMovie).distinct().collect(Collectors.toList());
    }

    public List<MovieSchedule> getSchedulesByMovieIdAndRoomNumber(String movieId, Long roomNumber) {
        return movieScheduleRepository.findAll().stream()
                .filter(movieSchedule -> movieSchedule.getMovie().getId().equals(movieId))
                .filter(movieSchedule -> movieSchedule.getRoom().getRoomNumber().equals(String.valueOf(roomNumber)))
                .collect(Collectors.toList());
    }

    public boolean hasScheduleOnTimeBlock(String movieId,
                                    LocalDateTime startBlockInclusive,
                                    Long roomNumber) {
        return getSchedulesByMovieIdAndRoomNumber(movieId, roomNumber).stream()
                .anyMatch(movieSchedule -> {
                    LocalDateTime movieStartTime =
                            LocalDateTime.of(movieSchedule.getStartAtDate(), movieSchedule.getStartAtTime());
                    LocalDateTime movieEndTime = movieStartTime.plusMinutes(movieSchedule.getMovie().getRunningTime());
                    LocalDateTime endBlockExclusive = startBlockInclusive.plusMinutes(30);
                    return movieStartTime.isBefore(endBlockExclusive) && movieEndTime.isAfter(startBlockInclusive);
                });
    }

    public boolean validateScheduleTime(String movieId, int runningTime, LocalDateTime startTime, Long roomNumber) {
        int ceiledTime = (int) Math.ceil(runningTime / 30.0) * 30;
        for (int i = 0; i < ceiledTime; i += 30) {
            if (hasScheduleOnTimeBlock(movieId, startTime.plusMinutes(i), roomNumber)) {
                return false;
            }
        }
        return true;
    }

    //MovieSchedule을 돌면서 주어진 Movie, 날짜에 해당하는 상영관 번호 리턴
    public List<Long> getTheaterByMovieAndDate(Movie movie, LocalDate date) {
        List<MovieSchedule> movieSchedules = movieScheduleRepository.findAll();
        List<Long> result = new ArrayList<>();
        for (MovieSchedule movieSchedule : movieSchedules) {
            if (movieSchedule.getMovie().equals(movie) && movieSchedule.getStartAtDate().equals(date)) {
                result.add(Long.parseLong(movieSchedule.getRoom().getRoomNumber()));
            }
        }
        Set<Long> distinctSet = new HashSet<>(result);
        List<Long> ret = new ArrayList<>(distinctSet);
        ret.sort(Comparator.naturalOrder());
        return ret;
    }

    //영화 정보, 관, 시작 시각이 주어졌을 때, 해당 스케줄의 Room 리턴
    public Room getRoomByMovieSchedule(Movie movie, LocalDate date, LocalTime time, String roomNumber) {
        List<MovieSchedule> movieSchedules = movieScheduleRepository.findAll();
        for (MovieSchedule movieSchedule : movieSchedules) {
            if (movieSchedule.getMovie().equals(movie) && movieSchedule.getStartAtDate().equals(date)
                    && movieSchedule.getStartAtTime().equals(time) && movieSchedule.getRoom().getRoomNumber()
                    .equals(roomNumber)) {
                return movieSchedule.getRoom();
            }
        }
        return null; // 원하는 Room이 없을 경우 null을 반환
    }


    public MovieSchedule getByMovieAndDateAndRoomNumberAndStartAt(Movie movie, LocalDate date, Long roomNumber,
                                                                  LocalTime startAt) {
        return movieScheduleRepository.findOne(movie, date, roomNumber, startAt);
    }
}

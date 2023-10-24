package service;

import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
import repository.MovieScheduleRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<LocalTime> getSortedMovieSchedules(Movie movie, LocalDate localDate, Long roomNumber) {
        return movieScheduleRepository.findAll().stream()
                .filter(movieSchedule -> Objects.equals(movieSchedule.getMovie().getId(), movie.getId()))
                .filter(movieSchedule -> Objects.equals(movieSchedule.getLocalDate(), localDate))
                .filter(movieSchedule -> Objects.equals(Long.parseLong(movieSchedule.getRoom().getRoomNumber()),
                        roomNumber))
                .map(MovieSchedule::getLocalTime)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Movie> getMovies(LocalDate localDate, Long roomNumber) {
        return movieScheduleRepository.findAll().stream()
                .filter(movieSchedule -> Objects.equals(movieSchedule.getLocalDate(), localDate))
                .filter(movieSchedule -> Objects.equals(Long.parseLong(movieSchedule.getRoom().getRoomNumber()),
                        roomNumber))
                .map(MovieSchedule::getMovie)
                .distinct()
                .collect(Collectors.toList());
    }

    public Boolean[] getMovieStartTimes(Movie movie, LocalDate localDate, Long roomNumber) {
        List<LocalTime> startTimes = getSortedMovieSchedules(movie, localDate, roomNumber);
        List<LocalTime> startTimesOfLastDay = getSortedMovieSchedules(movie, localDate.minusDays(1), roomNumber);
        Boolean[] isChecked = new Boolean[48];

        for (int i = 0; i < 48; i++)
            isChecked[i] = false;

        for (int i = 0; i < startTimes.size(); i++) {
            int start_h = startTimes.get(i).getHour();
            int start_m = startTimes.get(i).getMinute();

            int end_time = start_h * 60 + start_m + movie.getRunningTime();
            int end_h = end_time / 60;
            int end_m = end_time % 60;

            int s = start_h * 2 + (start_m == 30 ? 1 : 0);
            int e = end_h * 2 + (end_m > 30 ? 2 : (end_m > 0 ? 1 : 0));
            for (int j = s; j < Math.min(48, e); j++)
                isChecked[j] |= true;
        }

        for (int i = 0; i < startTimesOfLastDay.size(); i++) {
            int start_h = startTimesOfLastDay.get(i).getHour();
            int start_m = startTimesOfLastDay.get(i).getMinute();

            int end_time = start_h * 60 + start_m + movie.getRunningTime();
            int end_h = end_time / 60;
            int end_m = end_time % 60;

            if (end_h >= 24) {
                int e = (end_h - 24) * 2 + (end_m > 30 ? 2 : (end_m > 0 ? 1 : 0));
                for (int j = 0; j < Math.min(48, e); j++)
                    isChecked[j] |= true;
            }
        }
        return isChecked;
    }

    //MovieSchedule을 돌면서 주어진 Movie, 날짜에 해당하는 상영관 번호 리턴
    public List<Room> getTheaterByMovieAndDate(Movie movie, LocalDate date){
        List<MovieSchedule> movieSchedules = movieScheduleRepository.findAll();
        List<Room> result = new ArrayList<>();
        for(MovieSchedule movieSchedule : movieSchedules){
            if(movieSchedule.getMovie() == movie && movieSchedule.getLocalDate() == date) result.add(movieSchedule.getRoom());
        }
        return result;
    }

    //영화 정보, 관, 시작 시각이 주어졌을 때, 해당 스케줄의 Room 리턴
    public Room getRoomByMovieDateTheaterNo(Movie movie, LocalDate date, String roomNumber) {
        List<MovieSchedule> movieSchedules = movieScheduleRepository.findAll();
        for (MovieSchedule movieSchedule : movieSchedules) {
            if (movieSchedule.getMovie().equals(movie) &&
                    movieSchedule.getLocalDate().equals(date) &&
                    movieSchedule.getRoom().getRoomNumber().equals(roomNumber)) {
                return movieSchedule.getRoom();
            }
        }
        return null; // 원하는 Room이 없을 경우 null을 반환
    }
}
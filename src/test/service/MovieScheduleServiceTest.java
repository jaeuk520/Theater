package service;

import static org.assertj.core.api.Assertions.assertThat;

import entity.Movie;
import entity.MovieSchedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.MovieRepository;
import repository.MovieScheduleRepository;
import repository.RoomRepository;

class MovieScheduleServiceTest {
    MovieScheduleRepository movieScheduleRepository;
    MovieRepository movieRepository;
    RoomRepository roomRepository;
    MovieScheduleService movieScheduleService;

    @Test
    @DisplayName("영화 상영 날짜와 영화 ID로 해당 날짜에 상영하는 영화의 시간들을 올바르게 구한다.")
    public void findAllMoviesStartAtTimeByDateAndMovie() throws Exception {
        Movie movie = addMovieSchedulesAndReturnMovie();

        List<LocalTime> actual = movieScheduleService.getMovieStartAtTimeByDateAndRoomNumber(movie.getId(),
                LocalDate.of(2023, 10, 1), 1L);
        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(
                LocalTime.of(10, 0),
                LocalTime.of(13, 0));
    }

    @Test
    @DisplayName("영화 상영 시작 날짜와 상영관 번호로 모든 영화를 불러온다.")
    public void testGetMoviesByStartDateAtAndRoomNumber() throws Exception {
        final Movie movie = addMovieSchedulesAndReturnMovie();
        final Movie movie1 = new Movie(null, "IronMan", 110);
        movieRepository.save(movie1);

        MovieSchedule movieSchedule1 = new MovieSchedule(null, movie1,
                LocalDate.of(2023, 10, 1),
                LocalTime.of(15, 30, 0), 2L);
        movieScheduleRepository.save(movieSchedule1);

        final List<Movie> actual = movieScheduleService.getDistinctMoviesByStartDateAtAndRoomNumber(
                LocalDate.of(2023, 10, 1),
                1L
        );
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(movie);
    }

    @Test
    @DisplayName("영화 스케줄이 올바르게 추가된다.")
    public void pagingTest() throws Exception {
        Movie movie = new Movie(null, "Avengers", 180);
        movieRepository.save(movie);

        movieScheduleService.addMovieSchedule(null, movie, LocalDate.of(2023, 10, 1), LocalTime.of(12, 0), 1L);
        final List<MovieSchedule> actual = movieScheduleRepository.findAll();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getMovie()).isEqualTo(movie);
    }

    Movie addMovieSchedulesAndReturnMovie() {
        Movie avengers = new Movie(null, "Avengers", 180);
        String id = movieRepository.save(avengers);

        MovieSchedule movieSchedule1 = new MovieSchedule(null, avengers,
                LocalDate.of(2023, 10, 1),
                LocalTime.of(10, 0, 0), 1L);

        MovieSchedule movieSchedule2 = new MovieSchedule(null, avengers,
                LocalDate.of(2023, 10, 1),
                LocalTime.of(13, 0, 0), 1L);

        MovieSchedule movieSchedule3 = new MovieSchedule(null, avengers,
                LocalDate.of(2023, 10, 2),
                LocalTime.of(13, 0, 0), 1L);

        MovieSchedule movieSchedule4 = new MovieSchedule(null, avengers,
                LocalDate.of(2023, 10, 3),
                LocalTime.of(13, 0, 0), 1L);

        movieScheduleRepository.save(movieSchedule1);
        movieScheduleRepository.save(movieSchedule2);
        movieScheduleRepository.save(movieSchedule3);
        movieScheduleRepository.save(movieSchedule4);

        return avengers;
    }


    @BeforeEach
    void ready() {
        movieScheduleRepository = new MovieScheduleRepository("test_schedule.txt");
        movieRepository = new MovieRepository("test_movie.txt");
        roomRepository = new RoomRepository("test_theater.txt");
        movieScheduleService = new MovieScheduleService(movieScheduleRepository);
    }
}

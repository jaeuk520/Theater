package service;

import entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.MovieScheduleRepository;
import repository.RoomRepository;
import repository.TicketRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TicketServiceTest {
    private final RoomRepository roomRepository = new RoomRepository("test_theater.txt");
    private final MovieScheduleRepository movieScheduleRepository = new MovieScheduleRepository("test_schedule.txt");
    private final TicketRepository ticketRepository = new TicketRepository("test_ticket.txt");
    private final TicketService ticketService = new TicketService(ticketRepository);

    @Test
    @DisplayName("영화 예매하기.")
    public void addReservationTest() {
        // Movie 객체 생성
        Movie movie = new Movie(null, "파이트클럽", 200);

        // Seat 객체 생성
        Seat[][] seats = new Seat[10][10];
        for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++) seats[i][j] = new Seat("", true, false);

        // Room 객체 생성
        Room room = new Room(null, seats);
        roomRepository.save(room);

        // MovieSchedule 객체 생성
        LocalDate startDate = LocalDate.of(2023, 10, 15);
        LocalTime startTime = LocalTime.of(18, 30);
        MovieSchedule movieSchedule = new MovieSchedule(null, movie, startDate, startTime, 1L);
        movieScheduleRepository.save(movieSchedule);

        ticketService.addReservation(movieSchedule, "A01", "010-1234-5678", LocalDateTime.of(2023, 10, 16, 13,30));
    }
    @Test
    @DisplayName("예매 취소하기.")
    public void cancelReservationTest() {
        List<Ticket> actual = ticketRepository.findAll();
        Assertions.assertThat(actual).hasSize(1);
        System.out.println(actual.get(0).toString());

        System.out.println(actual.get(0).getId());
        ticketService.cancelReservation(actual.get(0).getId(), LocalDateTime.now());

        List<Ticket> actual2 = ticketRepository.findAll();
        Assertions.assertThat(actual2).hasSize(0);

    }

}

package service;

import entity.MovieSchedule;
import entity.Ticket;
import entity.TicketDto;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import repository.TicketRepository;

public class TicketService {
    public static final String OVERLAPPING_TIME = "ERR_OVERLAP";
    public static final String DUPLICATE_TICKET = "ERR_DUPLICATE";
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public String addReservation(MovieSchedule movieSchedule, String seatId, String phoneNumber,
                               LocalDateTime reservationTime) {

        Optional<Ticket> overlappingTicket = ticketRepository.findAllTicketsByPhoneNumber(phoneNumber).stream()
                .filter(ticket -> {
                            MovieSchedule ticketMovieSchedule = ticket.getMovieSchedule();
                            return !ticket.isCanceled() &&
                                    !ticketMovieSchedule.getId().equals(movieSchedule.getId()) &&
                                    ticketMovieSchedule.getStartAtDate().equals(movieSchedule.getStartAtDate()) &&
                                    isOverlap(ticketMovieSchedule.getStartAtTime(),
                                            ticketMovieSchedule.getStartAtTime()
                                                    .plusMinutes(ticketMovieSchedule.getMovie().getRunningTime()),
                                            movieSchedule.getStartAtTime(),
                                            movieSchedule.getStartAtTime()
                                                    .plusMinutes(movieSchedule.getMovie().getRunningTime()));
                        }
                )
                .findAny();

        Optional<Ticket> dupActual = ticketRepository.findAll().stream()
                .filter(ticket -> !ticket.isCanceled() && ticket.getMovieSchedule().getId().equals(movieSchedule.getId()) &&
                        ticket.getSeatId().equals(seatId))
                .findAny();

        if (overlappingTicket.isPresent()) {
            return OVERLAPPING_TIME;
        }
        if (dupActual.isPresent()) {
            return DUPLICATE_TICKET;
        }
        else {
            movieSchedule.getRoom().getSeatById(seatId).setReserved(true);
            return ticketRepository.save(new Ticket(null, movieSchedule, seatId, phoneNumber, reservationTime, reservationTime, 0));
        }
    }

    private boolean isOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    //cancelReservation함수는 예매 취소가 성공하는 경우 true를, 실패하는 경우 false를 반환합니다.
    public boolean isTicketExistsById(String id) {
        return ticketRepository.findById(id).isPresent();
    }

    public boolean cancelReservation(String id, LocalDateTime cancellationTime) {
        Optional<Ticket> actual = ticketRepository.findById(id);
        if (actual.isPresent()) {
            actual.get().cancel(cancellationTime);
            actual.get().getMovieSchedule().getRoom().getSeatById(actual.get().getSeatId()).setReserved(false);
            return true;
        }
        else {
            return false;
        }
    }

    //reservation.txt를 돌면서 주어진 전화번호와 동일한 티켓리스트 리턴
    public List<Ticket> getReservationDetails(String phoneNumber) {
        return ticketRepository.findAllTicketsByPhoneNumber(phoneNumber);
    }

    public List<TicketDto> getTicketStatus(String phoneNumber) {
        List<TicketDto> ticketList = new ArrayList<>();
        ticketRepository.findAllTicketsByPhoneNumber(phoneNumber)
                .forEach(ticket -> {
                    ticketList.add(TicketDto.fromReservedTicket(ticket));
                    if (ticket.isCanceled()) {
                        ticketList.add(TicketDto.fromCancelledTicket(ticket));
                    }
                });
        return ticketList.stream()
                .sorted(Comparator.comparing(TicketDto::getLastModified).reversed())
                .collect(Collectors.toList());
    }
}

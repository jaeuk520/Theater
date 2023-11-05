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
        if (overlappingTicket.isPresent()) {
            return null;
        }
        else {
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
        return actual.map(ticket -> ticket.cancel(cancellationTime)).orElse(false);
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
                .sorted(Comparator.comparing(TicketDto::getLastModified))
                .collect(Collectors.toList());
    }
}

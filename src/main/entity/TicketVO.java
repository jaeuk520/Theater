package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TicketVO {
    boolean isCancelled;
    LocalDateTime lastModified;
    String movieTitle;
    LocalDate scheduleDate;
    LocalTime scheduleTime;
    String roomNumber;
    String seatId;

    private TicketVO(boolean isCancelled, LocalDateTime lastModified, String movieTitle, LocalDate scheduleDate,
                     LocalTime scheduleTime, String roomNumber, String seatId) {
        this.isCancelled = isCancelled;
        this.lastModified = lastModified;
        this.movieTitle = movieTitle;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.roomNumber = roomNumber;
        this.seatId = seatId;
    }

    private static TicketVO from(Ticket ticket) {
        final MovieSchedule movieSchedule = ticket.getMovieSchedule();
        final Movie movie = movieSchedule.getMovie();

        return new TicketVO(
                ticket.isCanceled(),
                null,
                movie.getName(),
                movieSchedule.getStartAtDate(),
                movieSchedule.getStartAtTime(),
                movieSchedule.getRoom().getRoomNumber(),
                ticket.getSeatId()
        );
    }

    public static TicketVO fromCancelledTicket(Ticket ticket) {
        final TicketVO ticketVO = from(ticket);
        ticketVO.lastModified = ticket.getCancellationTime();
        ticketVO.isCancelled = true;
        return ticketVO;
    }

    public static TicketVO fromReservedTicket(Ticket ticket) {
        final TicketVO ticketVO = from(ticket);
        ticketVO.lastModified = ticket.getReservationTime();
        ticketVO.isCancelled = false;
        return ticketVO;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDate getStartAtDate() {
        return scheduleDate;
    }

    public LocalTime getStartAtTime() {
        return scheduleTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getSeatNumber() {
        return seatId;
    }

    public String isCanceledOrReserved() {
        return isCancelled ? "취소" : "예매";
    }
}

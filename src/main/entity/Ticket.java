package entity;

import java.time.LocalDateTime;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;
    private final String phoneNumber;
    private boolean isCanceled;
    private final LocalDateTime reservationTime;
    private LocalDateTime cancellationTime;


    public Ticket(String Id, MovieSchedule movieSchedule, String seatId, String phoneNumber,
                  LocalDateTime reservationTime) {
        super(Id);
        this.movieSchedule = movieSchedule;
        this.seatId = seatId;
        this.phoneNumber = phoneNumber;
        this.isCanceled = false; //1이면 취소된 경우, 생성할 땐 0
        this.reservationTime = reservationTime;
        this.cancellationTime = null;
    }

    @Override
    public String toString() {
        return id + "$" + movieSchedule.getId() + "$" + seatId + '\n';
    }

    public MovieSchedule getMovieSchedule() {
        return movieSchedule;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setIscanceled(LocalDateTime cancellationTime) {
        this.isCanceled = true;
        this.cancellationTime = cancellationTime;
    }
}

package entity;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;
    private final String id;

    public Ticket(MovieSchedule movieSchedule, String seatId, String reservationId) {
        super(null);
        this.movieSchedule = movieSchedule;
        this.seatId = seatId;
        this.id = reservationId;
    }

    @Override
    public String toString() {
        return id + "$" + movieSchedule.getMovie().getName() + "$" +
                movieSchedule.toString() +
                seatId + '\n';
    }
}

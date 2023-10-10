package entity;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;

    public Ticket(MovieSchedule movieSchedule, String seatId) {
        super(null);
        this.movieSchedule = movieSchedule;
        this.seatId = seatId;
    }

    @Override
    public String toString() {
        return id + "$" + movieSchedule.getId() + "$" + seatId + '\n';
    }
}

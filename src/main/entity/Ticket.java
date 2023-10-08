package entity;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;
    private final String id;

    public Ticket(String reservationId, MovieSchedule movieSchedule, String seatId) {
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

    public MovieSchedule getMovieSchedule() {
        return movieSchedule;
    }

    public String getSeatId() {
        return seatId;
    }
}

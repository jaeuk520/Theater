package entity;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;

    public Ticket(String Id, MovieSchedule movieSchedule, String seatId) {
        super(Id);
        this.movieSchedule = movieSchedule;
        this.seatId = seatId;
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
}
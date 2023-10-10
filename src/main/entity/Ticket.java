package entity;

public class Ticket extends Entity<String> {

    private final MovieSchedule movieSchedule;
    private final String seatId;

<<<<<<< HEAD
    public Ticket(String reservationId, MovieSchedule movieSchedule, String seatId) {
=======
    public Ticket(MovieSchedule movieSchedule, String seatId) {
>>>>>>> a55e061bda60c3bc8c3310072ea0c83c1a769e32
        super(null);
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

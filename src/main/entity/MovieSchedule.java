package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovieSchedule extends Entity<String> {

    private final Movie movie;
    private final LocalDateTime startAt;
    private final Room room;

    public MovieSchedule(Movie movie, LocalDateTime startAt, Room room) {
        super(null);
        this.movie = movie;
        this.startAt = startAt;
        this.room = room;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return movie.getId() + "$" +
                startAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "$" +
                startAt.format(DateTimeFormatter.ofPattern("hh:mm")) + "$" +
                room.getRoomNumber() + '\n';
    }
}

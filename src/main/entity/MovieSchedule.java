package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MovieSchedule extends Entity<String> {

    private final Movie movie;
    private final LocalDateTime startAt;
    private final Room room;

    public MovieSchedule(String id, Movie movie, LocalDate startAtDate, LocalTime startAtTime, Room room) {
        super(id);
        this.movie = movie;
        this.startAt = LocalDateTime.of(startAtDate, startAtTime);
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

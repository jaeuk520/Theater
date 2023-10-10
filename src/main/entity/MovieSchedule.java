package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MovieSchedule extends Entity<String> {

    private final Movie movie;
    private final LocalDate startAtDate;
    private final LocalTime startAtTime;
    private final Room room;

    public MovieSchedule(String id, Movie movie, LocalDate startAtDate, LocalTime startAtTime, Room room) {
        super(id);
        this.movie = movie;
        this.startAtDate = startAtDate;
        this.startAtTime = startAtTime;
        this.room = room;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalTime getLocalTime() {
        return startAtTime;
    }

    public LocalDate getLocalDate() {
        return startAtDate;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return movie.getId() + "$" +
                startAtDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "$" +
                startAtTime.format(DateTimeFormatter.ofPattern("hh:mm")) + "$" +
                room.getRoomNumber() + '\n';
    }
}

package entity;

import database.DatabaseContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MovieSchedule extends Entity<String> {

    private final Movie movie;
    private final LocalDate startAtDate;
    private final LocalTime startAtTime;
    private final Room room;

    public MovieSchedule(String id, Movie movie, LocalDate startAtDate, LocalTime startAtTime, int roomNumber) {
        super(id);
        this.movie = movie;
        this.startAtDate = startAtDate;
        this.startAtTime = startAtTime;
        this.room = new Room((Room) DatabaseContext.getDatabase(Room.class).get(Integer.toString(roomNumber)));
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

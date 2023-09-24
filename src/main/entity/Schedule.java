package entity;

import java.time.LocalDateTime;

public class Schedule {

    private final Movie movie;
    private final Room room;
    private final LocalDateTime startAt;
    private String reservationId;

    public Schedule(Movie movie, Room room, LocalDateTime startAt) {
        this.movie = movie;
        this.room = room;
        this.startAt = startAt;
    }
}

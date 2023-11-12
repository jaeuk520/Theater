package entity;

import database.DatabaseContext;
import exception.EntityInstantiateException;
import java.util.Arrays;

public class Room extends Entity<String> implements EntityValidator {
    private final Seat[][] seats;

    public Room(String roomNumber, Seat[][] seats) {
        super(roomNumber);
        this.seats = seats;
    }

    public Room(Room room) {
        super(room.id);
        this.seats = new Seat[room.seats.length][room.seats[0].length];
        for (int i = 0; i < room.seats.length; i++) {
            for (int j = 0; j < room.seats[0].length; j++) {
                this.seats[i][j] = new Seat(room.seats[i][j]);
            }
        }
        this.validate();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d %d\n", seats.length, seats[0].length));
        for (Seat[] seat : seats) {
            for (Seat s : seat) {
                sb.append(s.isAvailable() ? "O" : "X");
            }
            sb.append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }

    public int getTotalSeats() {
        return (int) Arrays.stream(seats)
                .mapToLong(seatArray -> Arrays.stream(seatArray)
                        .filter(Seat::isAvailable).count())
                .sum();
    }

    public int getRemainSeats() {
        return (int) Arrays.stream(seats)
                .mapToLong(seatArray -> Arrays.stream(seatArray)
                        .filter(seat -> seat.isAvailable() && !seat.isReserved()).count())
                .sum();
    }

    public String getSeatsToString() {
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 1; i <= seats.length; i++) {
            sb.append(String.format("%02d ", i));
        }
        sb.append("\n");
        for (int i = 0; i < seats.length; i++) {
            sb.append(String.valueOf((char) (i + 'A')) + " ");
            for (Seat s : seats[i]) {
                sb.append(s.isAvailable() ? (s.isReserved() ? "■" : "□") : " ").append("  ");
            }
            sb.append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }

    public String getRoomNumber() {
        return getId();
    }

    public Seat getSeatById(String seatId) {
        try {
            int row = (int) (seatId.charAt(0) - 'A');
            int col = Integer.parseInt(seatId.substring(1)) - 1;
            if (row >= seats.length || col >= seats[0].length) {
                return null;
            }
            return seats[row][col];
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void validate() {
        super.validate();
        // <영화상영관번호> 1이상, 현재 영화관의 상영관 개수 이하
        if (Integer.parseInt(getRoomNumber()) < 1 ||
                Integer.parseInt(getRoomNumber()) > DatabaseContext.getDatabase(Room.class).size()) {
            throw new EntityInstantiateException();
        }
    }
}

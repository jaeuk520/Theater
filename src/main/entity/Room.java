package entity;

import database.DatabaseContext;
import exception.EntityInstantiateException;

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
        int cnt = 0;
        for (Seat[] seat : seats) {
            for (Seat s : seat) {
                if (s.isAvailable())
                    cnt++;
            }
        }
        return cnt;
    }

    public int getRemainSeats() {
        int cnt = 0;
        for (Seat[] seat : seats) {
            for (Seat s : seat) {
                if (s.isAvailable() && !s.isReserved())
                    cnt++;
            }
        }
        return cnt;
    }

    public String getRoomNumber() {
        return getId();
    }

    @Override
    public void validate() {
        // <영화상영관번호> 1이상, 현재 영화관의 상영관 개수 이하
        if (Integer.parseInt(getRoomNumber()) < 1 || Integer.parseInt(getRoomNumber()) > DatabaseContext.getDatabase(
                Room.class).size()) {
            throw new EntityInstantiateException();
        }
    }
}

package entity;

public class Seat {

    private String seatId;
    private Boolean isAvailable;
    private Boolean isReserved;

    public Seat(String id, boolean isAvailable, boolean isReserved) {
        this.seatId = id;
        this.isAvailable = isAvailable;
        this.isReserved = isReserved;
    }

    public Seat(Seat seat) {
        this.seatId = seat.seatId;
        this.isReserved = seat.isReserved;
        this.isAvailable = seat.isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}

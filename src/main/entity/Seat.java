package entity;

public class Seat {

    private final String id;
    private boolean isAvailable;
    private boolean isReserved;

    public Seat(String id, boolean isAvailable, boolean isReserved) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.isReserved = isReserved;
    }

    public String getId() {
        return id;
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

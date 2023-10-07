package entity;

public class Seat extends Entity<String>{

    private Boolean isAvailable;
    private Boolean isReserved;

    public Seat(String id, boolean isAvailable, boolean isReserved) {
        super(id);
        this.isAvailable = isAvailable;
        this.isReserved = isReserved;
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

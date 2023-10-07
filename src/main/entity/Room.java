package entity;

public class Room extends Entity<Long>{

    private final Seat[][] seats;

    public Room(Long roomNumber, Seat[][] seats) {
        super(roomNumber);
        this.seats = seats;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d %d\n", seats.length,seats[0].length));
        for (Seat[] seat : seats){
            for (Seat s : seat) sb.append(s.isAvailable() ? "O" : "X");
            sb.append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }

    public Long getRoomNumber() {
        return getId();
    }
}

package entity;

public class Room extends Entity<String>{

    private final Seat[][] seats;

    public Room(String roomNumber, Seat[][] seats) {
        super(roomNumber);
        this.seats = seats;
    }

    public Room(Room room) {
        super(null);
        this.seats = new Seat[room.seats.length][room.seats[0].length];
        for(int i = 0; i < room.seats.length; i++) {
            for(int j = 0; j < room.seats[0].length; j++) {
                this.seats[i][j] = new Seat(room.seats[i][j]);
            }
        }

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

    public String getRoomNumber() {
        return getId();
    }
}

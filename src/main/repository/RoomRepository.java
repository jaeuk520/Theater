package repository;

import database.NumericIdEntityDatabase;
import entity.Room;
import literal.IdStrategy;

public class RoomRepository extends NumericIdEntityDatabase<Room> {

    public RoomRepository() {
        super(Room.class, IdStrategy.NUMBER, "theater.txt");
    }
}

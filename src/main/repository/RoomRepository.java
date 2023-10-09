package repository;

import database.NumericIdEntityDatabase;
import entity.Room;
import literal.Path;

public class RoomRepository extends NumericIdEntityDatabase<Room> {

    public RoomRepository() {
        super(Room.class, Path.THEATER_DATA);
    }
}

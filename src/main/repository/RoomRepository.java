package repository;

import database.NumericIdEntityDatabase;
import entity.Room;

public class RoomRepository extends NumericIdEntityDatabase<Room> {

    public RoomRepository(String path) {
        super(Room.class, path);
    }
}

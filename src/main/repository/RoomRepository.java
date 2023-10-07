package repository;

import database.NumericIdEntityDataBase;
import entity.Room;

public class RoomRepository extends NumericIdEntityDataBase<Room> {

    public RoomRepository() {
        super(Room.class, "theater.txt");
    }
}

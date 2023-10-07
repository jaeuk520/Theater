package repository;

import database.NumericIdEntityDataBase;
import entity.Room;
import literal.IdStrategy;

public class RoomRepository extends NumericIdEntityDataBase<Room> {

    public RoomRepository() {
        super(Room.class, IdStrategy.NUMBER, "theater.txt");
    }
}

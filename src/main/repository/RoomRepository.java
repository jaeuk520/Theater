package repository;

import database.NumericIdEntityDatabase;
import entity.Room;
import literal.Path;

public class RoomRepository extends NumericIdEntityDatabase<Room> {

<<<<<<< HEAD
    public RoomRepository() {
        super(Room.class, Path.THEATER_DATA);
=======
    public RoomRepository(String path) {
        super(Room.class, path);
>>>>>>> a55e061bda60c3bc8c3310072ea0c83c1a769e32
    }
}

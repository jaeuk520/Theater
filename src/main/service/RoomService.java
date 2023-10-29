package service;

import entity.Room;
import java.util.List;
import java.util.stream.Collectors;
import repository.RoomRepository;

public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public int getTotalPages() {
        return roomRepository.size() / 5 + (roomRepository.size() % 5 != 0 ? 1 : 0);
    }

    public boolean hasNextPage(int page) {
        return (page + 1) <= getTotalPages();
    }

    public boolean hasPreviousPage(int page) {
        return (page - 1) >= 1;
    }

    public List<Room> getSortedRooms() {
        return roomRepository.findAll()
                .stream()
                .sorted((a, b) -> (Long.parseLong(a.getRoomNumber()) - Long.parseLong(b.getRoomNumber()) > 0 ? 1 : -1))
                .collect(Collectors.toList());
    }
}

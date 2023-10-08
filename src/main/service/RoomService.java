package service;

import java.util.List;
import java.util.stream.Collectors;

import entity.Room;
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

    public List<Long> getSortedRoomNumbers() {
        return roomRepository.findAll()
                .stream()
                .map(Room::getRoomNumber)
                .sorted()
                .collect(Collectors.toList());
    }
}

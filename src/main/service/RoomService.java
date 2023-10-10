package service;

import entity.Room;
import repository.RoomRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                .map(Long::parseLong)
                .sorted(Long::compareTo)
                .collect(Collectors.toList());
    }

    public List<Room> getSortedRooms() {
        return roomRepository.findAll()
                .stream()
                .sorted((a, b) -> (Long.parseLong(a.getRoomNumber()) - Long.parseLong(b.getRoomNumber()) > 0 ? 1 : -1))
                .collect(Collectors.toList());
    }
}

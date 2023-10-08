package database;

import entity.Room;
import entity.Seat;
import literal.LiteralRegex;
import util.Input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class EntityLoader<E> {

    private final String path;
    private final Class<E> type;
    private final Input input;

    public EntityLoader(String path, Class<E> type) {
        this.path = path;
        this.type = type;
        this.input = Input.getInstance(path, LiteralRegex.FILE_DELIMITER);
    }

    /**
     * 데이터 파일로부터 HashMap으로 엔티티들을 불러옵니다. 이는 최초 한 번만 실행합니다.
     */
    public void load(HashMap<String, E> data) {
        if (Room.class.isAssignableFrom(type)) {
            loadTheater((HashMap<String, Room>) data);
            return;
        }

        while (input.hasNext()) {
            String[] attr = input.readLine().split("\\$");
            data.put(attr[0], EntityFactory.createEntity(type, data, attr));
        }
    }

    public void loadTheater(HashMap<String, Room> data) {
        long roomNumber = 1L;
        while(input.hasNext()) {
            List<Integer> rowCol = Arrays.stream(input.getByPattern("\\d\\ \\d")
                    .split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            int row = rowCol.get(0);
            int col = rowCol.get(1);
            Seat[][] seats = new Seat[row][col];
            for (int i = 0; i < row; i++) {
                String rowData = input.getByPattern(String.format("[OX]{%d}", col));
                for (int j = 0; j < col; j++) {
                    seats[i][j] = new Seat(
                            (char) ('A' + i) + Integer.toString(j+1),
                            rowData.charAt(j) == 'O',
                            false
                    );
                }
            }
            Room room = new Room(roomNumber++, seats);
            data.put(Long.toString(roomNumber), room);
        }
    }
}

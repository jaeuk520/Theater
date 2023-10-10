package database;

import entity.MovieSchedule;
import entity.Room;
import entity.Seat;
import entity.Ticket;
import exception.EntityInstantiateException;
import literal.LiteralRegex;
import repository.RoomRepository;
import service.RoomService;
import util.Input;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            //의미 규칙 검사:
            if(type.getName().equals("entity.Movie")) {
                //영화 데이터 파일 검사
                validateMovieDuplication(data, attr[0].toString(), attr[1].toString());
            }
            if(type.getName().equals("entity.MovieSchedule")) {
                //상영 스케줄 데이터 파일 검사
                validateRoomIndex(Integer.parseInt(attr[4]));
                validateMovieStartTime(attr[3]);
                validateCodeDuplication(data, attr[0]);
                validateMovieScheduleDuplication();
            }
            if(type.getName().equals("entity.Ticket")) {
                validateCodeDuplication(data,attr[0]);
                validateSeatDuplication(data, attr[1], attr[2]);
            }
            data.put(attr[0], EntityFactory.createEntity(type, attr));
        }
    }

    private void validateMovieStartTime(String localTime) {
        //영화 시작 시간이 0분 혹은 30분인지 확인
        LocalTime startTime = LocalTime.parse(localTime);
        if(startTime.getMinute() % 30 != 0) {
            throw new EntityInstantiateException();
        }
    }

    private <E> void validateCodeDuplication(HashMap<String, E> data, String code) {
        //의미 규칙: 같은 <스케줄코드>/<예매코드>를 가진 데이터는 최대 한 개만 존재한다.
        data.forEach((key, value) -> {
            if(code.equals(key)) {
                throw new EntityInstantiateException();
            }
        });
    }
    private <E> void validateSeatDuplication(HashMap<String, E> data, String scheduleCode, String seat) {
        //의미 규칙: 서로 다른 두 예매 정보에 대해서, <영화 스케줄>이 동일한 경우, 좌석번호는 서로 달라야 한다.
        data.forEach((key, value) -> {
            if(scheduleCode.equals((((Ticket)value).getMovieSchedule()).getId()) && seat.equals(((Ticket)value).getSeatId())) {
                throw new EntityInstantiateException();
            }
        });
    }

    private <E> void validateMovieScheduleDuplication() {
        //의미 규칙: 임의의 시각에 대해서, 하나의 영화관에서 상영되는 영화의 개수는 1개 이하여야 한다.
    }

    private static void validateRoomIndex(int room) {
        // <영화상영관번호> 1이상, 현재 영화관의 상영관 개수 이하
        if(room < 1 || room > DatabaseContext.getDatabase(Room.class).size()) {
            throw new EntityInstantiateException();
        }
    }

    private static <E> void validateMovieDuplication(HashMap<String,E> data, String movieId, String movieName) {
        data.forEach((key, value) -> {
            if(movieId.equals(key) || movieName.equals((value.toString().split("\\$"))[2].replaceAll("\n", ""))) {
                throw new EntityInstantiateException();
            }
        });
    }

    public void loadTheater(HashMap<String, Room> data) {
        long roomNumber = 1L;
<<<<<<< HEAD
        Long roomMax = Long.parseLong(input.readLine());
        while(input.hasNext()) {
=======
        int totalTheaters = Integer.parseInt(input.readLine());
        for(int t = 0; t < totalTheaters; t++) {
>>>>>>> a55e061bda60c3bc8c3310072ea0c83c1a769e32
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
<<<<<<< HEAD
            Room room = new Room(roomNumber, seats);
            data.put(Long.toString(roomNumber++), room);
        }
        if(roomNumber != roomMax + 1) {
            throw new EntityInstantiateException();
=======
            Room room = new Room(Long.toString(roomNumber), seats);
            data.put(Long.toString(roomNumber), room);
            roomNumber++;
>>>>>>> a55e061bda60c3bc8c3310072ea0c83c1a769e32
        }
    }
}

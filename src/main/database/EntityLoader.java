package database;

import entity.*;
import exception.EntityInstantiateException;
// import javafx.util.Pair;
import literal.LiteralRegex;
import util.Input;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                //validateRoomIndex(Integer.parseInt(attr[4]));
                //validateMovieStartTime(attr[3]);
                validateCodeDuplication(data, attr[0]);
            }
            if(type.getName().equals("entity.Ticket")) {
                validateCodeDuplication(data,attr[0]);
                validateSeatDuplication(data, attr[1], attr[2]);
            }
            data.put(attr[0], EntityFactory.createEntity(type, attr));
        }
        if(type.getName().equals("entity.MovieSchedule")) validateMovieScheduleDuplication(data);
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

    private <E> void validateMovieScheduleDuplication(HashMap<String,E> data) {
        //의미 규칙: 임의의 시각에 대해서, 하나의 영화관에서 상영되는 영화의 개수는 1개 이하여야 한다.
        List<String> schedules = new ArrayList<>();

        data.forEach((key, value) -> {
            int runningTime = (((MovieSchedule)value).getMovie()).getRunningTime();
            String startAtDate = ((MovieSchedule)value).getLocalDate().toString();
            String startAtTime = ((MovieSchedule)value).getLocalTime().toString();
            String roomNumber = ((MovieSchedule)value).getRoom().getRoomNumber();
            schedules.add(startAtTime + "$" + startAtDate + "$" + runningTime + "$" + roomNumber);
        });
        schedules.sort(Comparator.naturalOrder());
        System.out.println(schedules);

        for(int i=0; i < schedules.size() - 1; i++) {
            for(int j=i+1; j < schedules.size(); j++) {
                //상영관번호, 상영 날짜가 같은지 비교
                if(schedules.get(i).split("\\$")[1].equals(schedules.get(j).split("\\$")[1])
                        && schedules.get(i).split("\\$")[3].equals(schedules.get(j).split("\\$")[3])) {
                    // A 영화 상영 시작 시간 + 러닝 타임 <= B 영화 상영 시작 시간
                    if(LocalTime.parse(schedules.get(i).split("\\$")[0])
                            .plusMinutes(Long.parseLong(schedules.get(i).split("\\$")[2]))
                            .isAfter(LocalTime.parse(schedules.get(j).split("\\$")[0]))) {
                        throw new EntityInstantiateException();
                    }
                }
            }
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
        int totalTheaters = Integer.parseInt(input.readLine());
        for(int t = 0; t < totalTheaters; t++) {
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
            Room room = new Room(Long.toString(roomNumber), seats);
            data.put(Long.toString(roomNumber), room);
            roomNumber++;
        }
    }
}
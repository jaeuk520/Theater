package util;

import entity.Movie;
import entity.MovieSchedule;
import entity.Room;
import entity.TicketVO;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import literal.LiteralRegex;
import literal.Literals;
import literal.Path;
import repository.MovieRepository;
import repository.MovieScheduleRepository;
import repository.RoomRepository;
import repository.TicketRepository;
import service.MovieScheduleService;
import service.MovieService;
import service.RoomService;
import service.TicketService;


public class Console {
    private final Input input;
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final TicketService ticketService;
    private final RoomService roomService;
    private LocalDateTime systemTime;

    public Console() {
        input = new Input(System.in, LiteralRegex.CONSOLE_DELIMITER);
        roomService = new RoomService(new RoomRepository(Path.THEATER_DATA));
        movieService = new MovieService(new MovieRepository(Path.MOVIE_DATA));
        movieScheduleService = new MovieScheduleService(new MovieScheduleRepository(Path.SCHEDULE_DATA));
        ticketService = new TicketService(new TicketRepository(Path.RESERVATION_DATA));
    }

    public String inputLoop(String printMessage, String pattern, String errorMessage) {
        while (true) {
            try {
                printf(printMessage);
                String userInput = input.getByPattern(pattern);
                if (!userInput.matches(pattern)) {
                    continue;
                }
                return userInput;
            } catch (Exception e) {
                printError(errorMessage);
            }
        }
    }

    public int mainMenu() {
        String command = inputLoop(
                "============== 메인메뉴 ==============\n" + "1. 영화관리\n" + "2. 예매\n" + "3. 예매취소\n" + "4. 예매 내역 조회\n"
                        + "0. 종료 \n" + "입력: ", LiteralRegex.MAIN_INPUT, "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");

        Map<String, Runnable> nextMenu = new HashMap<>();
        nextMenu.put(Literals.QUIT, () -> System.exit(0));
        nextMenu.put(Literals.RESERVATION, this::reservationMenu);
        nextMenu.put(Literals.MANAGEMENT, this::managementMenu);
        nextMenu.put(Literals.CANCEL_RESERVATION, this::cancelReservationMenu);
        nextMenu.put(Literals.CHECK_RESERVATION, this::checkReservationDetailsMenu);

        nextMenu.get(command).run();
        return mainMenu();
    }

    /* 부 프롬프트 1: 영화 관리 */
    private void managementMenu() {
        int page = 1;

        while (true) {
            int totalPage = movieService.getTotalPages();
            List<Movie> movies = movieService.getSortedMovies();
            StringBuilder sb = new StringBuilder("============== 영화관리 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++) {
                sb.append(idx).append(". ").append(movies.get(i).getName()).append("\n");
            }
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n9. 영화 추가\n0. 뒤로가기\n입력: ");

            String command = inputLoop(sb.toString(), LiteralRegex.MANAGE_INPUT, "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
            int selected = Integer.parseInt(command);
            if (1 <= selected && selected <= 5) {
                if (!checkValidNumber(movies.size(), page, selected)) {
                    printError("해당 영화가 존재하지 않습니다. 다시 입력해 주세요.");
                    continue;
                }
                addMovieScheduleMenu(movies.get((page - 1) * 5 + selected - 1));
            }

            if (command.equals(Literals.PREVIOUS_PAGE)) {
                if (!movieService.hasNextPage(page)) {
                    printError("이전 페이지가 존재하지 않습니다.");
                    continue;
                }
                page++;
            }

            if (command.equals(Literals.NEXT_PAGE)) {
                if (!movieService.hasPreviousPage(page)) {
                    printError("다음 페이지가 존재하지 않습니다.\n");
                    continue;
                }
                page--;
            }

            if (command.equals(Literals.ADD_MOVIE)) {
                addMovieMenu();
            }

            if (command.equals(Literals.BACK)) {
                return;
            }
        }
    }

    /* 부 프롬프트 1.1: 영화 추가 */
    private void addMovieMenu() {
        while (true) {
            printf("영화 이름을 입력해주세요 : ");
            String movieName = input.getByPattern(LiteralRegex.MOVIE_NAME).trim();
            if (movieName.contains("$")) {
                printError("$는 포함될 수 없는 문자입니다. 다시 입력해 주세요.");
                continue;
            }
            String runningTimeStr = inputLoop("러닝타임을 입력해주세요 : ", LiteralRegex.RUNNING_TIME,
                    "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
            int runningTime = Integer.parseInt(runningTimeStr);
            movieService.addMovie(movieName, runningTime);
            println("영화를 저장했습니다.");
            break;
        }
    }

    /* 부 프롬프트 1.2: 영화 상영 일정 추가 */
    private void addMovieScheduleMenu(Movie movie) {
        int page = 1;

        while (true) {
            int totalPage = roomService.getTotalPages();
            List<Room> rooms = roomService.getSortedRooms();
            StringBuilder sb = new StringBuilder("============== 일정추가 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < rooms.size(); i++, idx++) {
                sb.append(idx).append(". ").append(rooms.get(i).getRoomNumber()).append("관\n");
            }
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");

            String command = inputLoop(sb.toString(), LiteralRegex.PAGE_NO_OPTION_INPUT,
                    "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
            int selected = Integer.parseInt(command);
            if (1 <= selected && selected <= 5) {
                if (!checkValidNumber(rooms.size(), page, selected)) {
                    printError("해당 상영관이 존재하지 않습니다. 다시 입력해 주세요.");
                    continue;
                }
                selectMovieDateMenu(movie, rooms.get(getPageNumber(page, selected - 1)));
            }

            if (command.equals(Literals.PREVIOUS_PAGE)) {
                if (!roomService.hasNextPage(page)) {
                    printError("이전 페이지가 존재하지 않습니다.");
                    continue;
                }
                page++;
            }

            if (command.equals(Literals.NEXT_PAGE)) {
                if (!roomService.hasPreviousPage(page)) {
                    printError("다음 페이지가 존재하지 않습니다.\n");
                    continue;
                }
                page--;
            }

            if (command.equals(Literals.BACK)) {
                return;
            }
        }
    }

    /* 부 프롬프트 1.3: 영화 상영일 선택 */
    private void selectMovieDateMenu(Movie movie, Room room) {
        LocalDate date;
        while (true) {
            String command = inputLoop("============== 일정추가 ==============\n" + "입력 (YYYY-MM-DD 형식): ",
                    LiteralRegex.MOVIE_DATE, "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");

            if (!validateDate(systemTime.toLocalDate(), command)) {
                continue;
            }
            date = LocalDate.parse(command);
            break;
        }
        selectMovieTimeMenu(movie, room, date);
    }

    private boolean validateDate(LocalDate systemDate, String dateInput) {
        final LocalDate LAST_DATE = LocalDate.of(2100, 12, 31);
        try {
            LocalDate.parse(dateInput);
        } catch (DateTimeException e) {
            printError("존재하지 않는 날짜입니다. 다시 입력해주세요.\n");
            return false;
        }
        LocalDate date = LocalDate.parse(dateInput);
        if (date.isBefore(systemDate)) {
            printError("지난 날짜입니다. 다시 입력해주세요.\n");
            return false;
        }
        if (date.isAfter(LAST_DATE)) {
            printError("2100년 이내의 날짜여야 합니다. 다시 입력해주세요.\n");
            return false;
        }
        return true;
    }

    /* 부 프롬프트 1.4: 영화 시작 시각 입력 */
    private void selectMovieTimeMenu(Movie movie, Room room, LocalDate date) {
        LocalDateTime inpDateTime;
        while (true) {
            StringBuilder sb = new StringBuilder(String.format("============== %s 상영 시각 추가 ==============\n", date));
            for (int i = 0; i < 2; i++) { // i: 0, 12시를 의미
                for (int j = 0; j < 12; j++) {
                    sb.append(String.format("%02d", i * 12 + j)).append("    ");
                }
                sb.append("\n");
                for (int j = 0; j < 24; j++) { // j: 00분부터 30분 단위
                    boolean validTimeBlock = movieScheduleService.hasScheduleOnTimeBlock(movie.getId(),
                            LocalDateTime.of(date, LocalTime.of(i * 12 + j / 2, (j % 2 == 1) ? 30 : 0, 0)),
                            Long.parseLong(room.getRoomNumber()));
                    sb.append(validTimeBlock ? "■" : "□").append("  ");
                }
                sb.append("\n");
            }
            sb.append("상영 시작 시각 (HH:MM 형식): ");

            String command = inputLoop(sb.toString(), LiteralRegex.START_TIME,
                    "입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");

            // 과거의 시간인지
            LocalDateTime curDateTime = systemTime;
            inpDateTime = LocalDateTime.of(date, LocalTime.parse(command));
            if (inpDateTime.isBefore(curDateTime)) {
                printError("지난 시간입니다. 다시 입력해주세요.\n");
                continue;
            }

            // 추가하려는 시간 ~ 러닝타임 동안 상영 중인 영화가 있어서 추가가 불가능한 경우
            if (!movieScheduleService.validateScheduleTime(movie.getId(), movie.getRunningTime(), inpDateTime,
                    Long.parseLong(room.getRoomNumber()))) {
                printError("이미 상영 중인 영화가 있습니다. 다시 입력해주세요.\n");
                continue;
            }

            break;
        }

        movieScheduleService.addMovieSchedule(null, movie, date, inpDateTime.toLocalTime(),
                Long.parseLong(room.getRoomNumber()));

        managementMenu();
    }

    private void removeMovieMenu() {

    }

    /* 부 프롬프트 2: 예매 */
    private void reservationMenu() {
        int nextMenu = 0;
        int page = 1;
        int totalPage = movieService.getTotalPages();
        List<Movie> movies = movieService.getSortedMovies();
        String command = "";

        while (true) {
            StringBuilder sb = new StringBuilder("============== 영화예매 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++) {
                sb.append(idx).append(". ").append(movies.get(i).getName()).append("\n");
            }
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.PAGE_NO_OPTION_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidMovieNumber(movies, page, num)) {
                        printError("해당 영화가 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    } else {
                        nextMenu = 1;
                        break;
                    }
                }

                case Literals.PREVIOUS_PAGE: {
                    if (movieService.hasPreviousPage(page)) {
                        page--;
                    } else {
                        printError("이전 페이지가 존재하지 않습니다.");
                    }
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (movieService.hasNextPage(page)) {
                        page++;
                    } else {
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    }
                    break;
                }

                case Literals.BACK:
                    return;
            }
            if (nextMenu != 0) {
                break;
            }
        }
        if (nextMenu == 1) {
            int num = Integer.parseInt(command);
            selectReservationDateMenu(movies.get(getPageNumber(page, num) - 1));
        }
    }

    /* 부 프롬프트 2.1: 예매 날짜 선택 */
    // 인자로 선택한 영화를 받는다
    private void selectReservationDateMenu(Movie movie) {
        int nextMenu;
        String command = "";
        while (true) {
            StringBuilder sb = new StringBuilder("============== 날짜선택 ==============\n");
            for (int i = 0; i < 7; i++) {
                LocalDateTime dates = systemTime.plusDays(i);
                sb.append(
                        String.format("%d. %s\n", i + 1, dates.toLocalDate().toString() + (i == 0 ? " (Today)" : "")));
            }
            sb.append("0. 뒤로가기\n").append("입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.RESERVATION_DATE_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            if (command.equals(Literals.BACK)) {
                nextMenu = -1;
            } else {
                nextMenu = 1;
            }

            break;
        }
        if (nextMenu == -1) {
            reservationMenu();
        } else if (nextMenu == 1) {
            int num = Integer.parseInt(command);
            selectReservationRoomMenu(movie, systemTime.plusDays(num - 1).toLocalDate());
        }
    }

    /* 부 프롬프트 2.2: 상영관 선택 */
    // 영화, 날짜 선택 완료
    private void selectReservationRoomMenu(Movie movie, LocalDate date) {
        int nextMenu = 0;
        List<Long> rooms = movieScheduleService.getTheaterByMovieAndDate(movie, date);
        int page = 1;
        int totalPage = rooms.size() / 5 + (rooms.size() % 5 != 0 ? 1 : 0);
        String command = "";

        while (true) {
            command = "";
            StringBuilder sb = new StringBuilder("============== 상영관 선택 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < rooms.size(); i++, idx++) {
                sb.append(idx).append(". ").append(rooms.get(i)).append("관\n");
            }
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.PAGE_NO_OPTION_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidNumber(rooms.size(), page, num)) {
                        printError("해당 관이 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    } else {
                        nextMenu = 1;
                    }
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if ((page - 1) >= 1) {
                        page--;
                    } else {
                        printError("이전 페이지가 존재하지 않습니다.");
                    }
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if ((page + 1) <= totalPage) {
                        page++;
                    } else {
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    }
                    break;
                }

                case Literals.BACK: {
                    nextMenu = -1;
                    break;
                }
            }

            if (nextMenu != 0) {
                break;
            }
        }
        if (nextMenu == -1) {
            selectReservationDateMenu(movie);
        } else if (nextMenu == 1) {
            int num = Integer.parseInt(command);
            selectReservationTimeMenu(movie, date, rooms.get(getPageNumber(page, num) - 1));
        }
    }

    /* 부 프롬프트 2.3: 일정 선택 */
    // 시간 선택
    private void selectReservationTimeMenu(Movie movie, LocalDate date, Long roomNumber) {
        List<LocalTime> schedules = movieScheduleService.getMovieStartAtTimeByDateAndRoomNumber(movie.getId(), date,
                roomNumber);
        int sz = schedules.size();
        int page = 1;
        int totalPage = sz / 5 + (sz % 5 != 0 ? 1 : 0);
        int nextMenu = 0;
        String command = "";

        while (true) {
            command = "";
            StringBuilder sb = new StringBuilder("============== 일정 선택 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < sz; i++, idx++) {
                LocalTime startAtTime = schedules.get(i);
                LocalTime endAtTime = startAtTime.plusMinutes(movie.getRunningTime());
                Room room = movieScheduleService.getRoomByMovieSchedule(movie, date, startAtTime,
                        String.valueOf(roomNumber));
                sb.append(idx).append(". ").append(startAtTime.toString()).append("-").append(endAtTime.toString())
                        .append(String.format("[%d / %d]\n", room.getRemainSeats(), room.getTotalSeats()));
            }
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.PAGE_NO_OPTION_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidNumber(sz, page, num)) {
                        printError("해당 일정이 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    } else {
                        nextMenu = 1;
                    }
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if ((page - 1) >= 1) {
                        page--;
                    } else {
                        printError("이전 페이지가 존재하지 않습니다.");
                    }
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if ((page + 1) <= totalPage) {
                        page++;
                    } else {
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    }
                    break;
                }

                case Literals.BACK: {
                    nextMenu = -1;
                    break;
                }
            }

            if (nextMenu != 0) {
                break;
            }
        }

        if (nextMenu == -1) {
            selectReservationRoomMenu(movie, date);
        } else {
            int num = Integer.parseInt(command);

            MovieSchedule movieSchedule = movieScheduleService.getByMovieAndDateAndRoomNumberAndStartAt(movie, date,
                    roomNumber, schedules.get(getPageNumber(page, num) - 1));
            selectReservationSeatMenu(movieSchedule);
        }
    }

    /* 부 프롬프트 2.4: 좌석 선택 */
    private void selectReservationSeatMenu(MovieSchedule movieSchedule) {
        int nextMenu = 0;
        String command = "";
        Room room = movieSchedule.getRoom();

        while (true) {
            command = "";
            StringBuilder sb = new StringBuilder("============== 좌석 선택 ==============\n");
            sb.append(room.getSeatsToString());
            sb.append("입력 (뒤로가기: 0) : ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.SEAT_NUMBER)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            if (command.equals(Literals.BACK)) {
                nextMenu = -1;
                break;
            } else if (room.getSeatById(command) != null) {
                if (room.getSeatById(command).isReserved()) {
                    printError("이미 예매된 좌석입니다. 다시 입력해주세요.\n");
                    continue;
                }
                nextMenu = 1;
                break;
            } else {
                printError("좌석이 존재하지 않거나 예매 불가능한 좌석입니다. 다시 입력해주세요.\n");
            }
        }
        if (nextMenu == -1) {
            selectReservationTimeMenu(movieSchedule.getMovie(), movieSchedule.getStartAtDate(),
                    Long.parseLong(movieSchedule.getRoom().getRoomNumber()));
        } else if (nextMenu == 1) {
            inputPhoneNumberMenu(movieSchedule, command);
        }
    }

    /* 부 프롬프트 2.5: 전화번호 입력 */
    private void inputPhoneNumberMenu(MovieSchedule movieSchedule, String seatId) {
        int nextMenu = 0;
        String command = "";
        while (true) {
            println("============== 전화번호 입력 ==============");
            printf("전화번호 입력(뒤로가기: 0): ");

            if ((command = input.getByPattern(LiteralRegex.PHONE_NUMBER)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            if (command.equals(Literals.BACK)) {
                nextMenu = -1;
            } else {
                nextMenu = 1;
            }

            if (nextMenu != 0) {
                break;
            }
        }
        if (nextMenu == -1) {
            selectReservationSeatMenu(movieSchedule);
        } else if (nextMenu == 1) {
            String ticketId = ticketService.addReservation(movieSchedule, seatId, command, systemTime);
            if (ticketId.equals(TicketService.OVERLAPPING_TIME)) {
                printError("같은 시간에 다른 상영관에서 영화가 예매되어 있습니다.");
                inputPhoneNumberMenu(movieSchedule, seatId);
            } else {
                printReservationCodeMenu(ticketId);
            }
        }
    }

    /* 부 프롬프트 2.6: 예매 코드 출력 */
    private void printReservationCodeMenu(String ticketId) {
        println("============== 예매코드 ==============");
        println("예매 코드는 " + ticketId + " 입니다.");
    }

    /* 부 프롬프트 3: 예매 취소 */
    private void cancelReservationMenu() {
        String command = "";
        while (true) {
            println("============== 예매취소 ==============");
            printf("예매 코드 입력 (뒤로 가기: 0): ");
            if ((command = input.getByPattern(LiteralRegex.RESERVATION_CODE)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }
            if (command.equals(Literals.BACK)) {
                return;
            }
            if (!ticketService.isTicketExistsById(command)) {
                printError("존재하지 않는 예매 코드입니다. 다시 입력해주세요.\n");
            } else if (ticketService.isTicketAlreadyCanceled(command)) {
                printError("이미 취소된 예매정보입니다. 다시 입력해주세요.\n");
            } else if (!ticketService.cancelReservation(command, systemTime)) {
                printError("이미 지난 예매정보입니다. 다시 입력해주세요.\n");
            } else {
                println("예매가 취소되었습니다.");
                break;
            }
        }
    }

    /* 부 프롬프트 4: 예매 내역 조회 */
    private void checkReservationDetailsMenu() {
        int nextMenu = 0;
        String command = "";
        while (true) {
            println("============== 전화번호 입력 ==============");
            printf("전화번호 입력(뒤로가기: 0): ");

            if ((command = input.getByPattern(LiteralRegex.PHONE_NUMBER)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            if (command.equals(Literals.BACK)) {
                nextMenu = -1;
                break;
            }

            List<TicketVO> tickets = ticketService.getTicketStatus(command);
            if (tickets.isEmpty()) {
                printError("해당하는 예매내역이 없습니다. 다시 입력해주세요.");
                continue;
            }
            println("예매 내역은 다음과 같습니다.");
            for (TicketVO ticket : tickets) {
                println(String.format("(%s %s %s) %s %s %s %s관 %s", ticket.getLastModified().toLocalDate().toString(),
                        ticket.getLastModified().toLocalTime().toString(), ticket.isCanceledOrReserved(),
                        ticket.getMovieTitle(), ticket.getStartAtDate().toString(), ticket.getStartAtTime().toString(),
                        ticket.getRoomNumber(), ticket.getSeatNumber()));
            }
            break;
        }
        if (nextMenu == -1) {
            return;
        }
    }

    /* 시스템 시각 입력 */
    public void inputCurrentTime() {
        String command = "";
        while (true) {
            printf("기준 시각 입력 (yyyy-MM-dd-HH-mm 형식): ");

            if ((command = input.getByPattern(LiteralRegex.SYSTEM_TIME)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            int year = Integer.parseInt(command.split("-")[0]);
            int month = Integer.parseInt(command.split("-")[1]);
            int day = Integer.parseInt(command.split("-")[2]);
            int hour = Integer.parseInt(command.split("-")[3]);
            int minute = Integer.parseInt(command.split("-")[4]);

            try {
                systemTime = LocalDateTime.of(year, month, day, hour, minute);
                if (!(systemTime.isAfter(LocalDateTime.of(2000, 1, 1, 0, 0, 0)) && systemTime.isBefore(
                        LocalDateTime.of(2100, 12, 31, 23, 59, 59)))) {
                    printError("지원하지 않는 시각 범위입니다. 다시 입력해주세요.\n");
                    continue;
                }
            } catch (DateTimeException e) {
                printError("올바르지 않는 시각 형식입니다. 다시 입력해주세요.\n");
                continue;
            }

            break;
        }
    }

    public void run() {
        inputCurrentTime();
        mainMenu();
    }

    public void exit() {
        input.close();
    }

    private void printError(String s) {
        System.err.println("Error : " + s);
        flush();
    }

    private void println(String s) {
        System.out.println(s);
        flush();
    }

    private void printf(String format, Object... args) {
        System.out.printf(format, args);
        flush();
    }

    private void flush() {
        System.err.flush();
        System.out.flush();
    }

    private int getPageNumber(int page, int num) {
        return (page - 1) * 5 + num;
    }

    private boolean checkValidNumber(int size, int page, int num) {
        return getPageNumber(page, num) <= size;
    }

    private boolean checkValidMovieNumber(List<Movie> movies, int page, int num) {
        return getPageNumber(page, num) <= movies.size();
    }

    private boolean checkValidRoomNumber(List<Room> rooms, int page, int num) {
        return getPageNumber(page, num) <= rooms.size();
    }
}

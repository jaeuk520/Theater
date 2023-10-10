package util;

import entity.Movie;
import entity.MovieSchedule;
import entity.Ticket;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Console {

    private final Input input;
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final TicketService ticketService;
    private final RoomService roomService;

    public Console() {
        input = new Input(System.in, LiteralRegex.CONSOLE_DELIMITER);
        roomService = new RoomService(new RoomRepository(Path.THEATER_DATA));
        movieService = new MovieService(new MovieRepository(Path.MOVIE_DATA));
        movieScheduleService = new MovieScheduleService(new MovieScheduleRepository(Path.SCHEDULE_DATA));
        ticketService = new TicketService(new TicketRepository(Path.RESERVATION_DATA));
    }

    public int mainMenu() {
        while (true) {
            String command = "";
            printf("============== 메인메뉴 ==============\n" +
                    "1. 영화관리\n" +
                    "2. 예매\n" +
                    "3. 예매취소\n" +
                    "0. 종료 \n" +
                    "입력: ");
            if ((command = input.getByPattern(LiteralRegex.MAIN_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }
            switch (command) {
                case Literals.QUIT:
                    return exit();
                case Literals.RESERVATION: {
                    reservationMenu();
                    break;
                }
                case Literals.MANAGEMENT: {
                    managementMenu();
                    break;
                }
                case Literals.CANCEL_RESERVATION: {
                    cancelReservationMenu();
                    break;
                }
            }
        }
    }

    /* 부 프롬프트 1: 영화 관리 */
    private void managementMenu() {
        int nextMenu = 0;
        int page = 1;
        int totalPage = movieService.getTotalPages();
        List<Movie> movies = movieService.getSortedMovies();
        String command = "";

        while (true) {
            command = "";
            StringBuilder sb = new StringBuilder("============== 영화관리 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++)
                sb.append(idx).append(". ").append(movies.get(i).getName()).append("\n");
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n9. 영화 추가\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.MANAGE_INPUT)) == null) {
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
                    } else
                        nextMenu = 2;
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if (movieService.hasPreviousPage(page))
                        page--;
                    else
                        printError("이전 페이지가 존재하지 않습니다.");
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (movieService.hasNextPage(page))
                        page++;
                    else
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    break;
                }

                case Literals.ADD_MOVIE: {
                    nextMenu = 1;
                    break;
                }
                case Literals.BACK:
                    return;
            }

            if (nextMenu != 0)
                break;
        }

        if (nextMenu == 1)
            addMovieMenu();
        else if (nextMenu == 2) ;
//            addMovieScheduleMenu(new MovieSchedule(null, movies.get(getPageNumber(page, Integer.parseInt(command))), LocalDate.now(), LocalTime.now(), null));
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
            printf("러닝타임을 입력해주세요 : ");
            String runningTimeStr = input.getByPattern(LiteralRegex.RUNNING_TIME);
            if (runningTimeStr == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }
            int runningTime = Integer.parseInt(runningTimeStr);
            movieService.addMovie(movieName, runningTime);
            println("영화를 저장했습니다.");
            break;
        }
        managementMenu();
    }

    /* 부 프롬프트 1.2: 영화 상영 일정 추가 */
    private void addMovieScheduleMenu(MovieSchedule movieSchedule) {
        int nextMenu = 0;
        int page = 1;
        int totalPage = roomService.getTotalPages();
        List<Long> rooms = roomService.getSortedRoomNumbers();
        String command = "";

        while (true) {
            command = "";
            StringBuilder sb = new StringBuilder("============== 일정추가 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < rooms.size(); i++, idx++)
                sb.append(idx).append(". ").append(Long.toString(rooms.get(i))).append("관\n");
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
                    if (!checkValidRoomNumber(rooms, page, num)) {
                        printError("해당 관이 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    } else
                        nextMenu = 1;
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if (roomService.hasPreviousPage(page))
                        page--;
                    else
                        printError("이전 페이지가 존재하지 않습니다.");
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (roomService.hasNextPage(page))
                        page++;
                    else
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    break;
                }

                case Literals.BACK: {
                    nextMenu = -1;
                    break;
                }
            }

            if (nextMenu != 0)
                break;
        }
        if (nextMenu == -1)
            managementMenu();
        else if (nextMenu == 1)
            selectMovieDateMenu(movieSchedule);
    }

    /* 부 프롬프트 1.3: 영화 상영일 선택 */
    private void selectMovieDateMenu(MovieSchedule movieSchedule) {
        String command = "";
        while (true) {
            command = "";
            printf("============== 일정추가 ==============\n" +
                    "입력 (YYYY-MM-DD 형식): ");
            if ((command = input.getByPattern(LiteralRegex.MOVIE_DATE)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                format.parse(command);

                /* 실제로 존재하는 날짜라면 과거가 아닌지, 유효기간 내에 존재하는지 테스트 */
                Date curDate = format.parse(format.format(new Date()));
                Date inpDate = format.parse(command);
                Date endDate = format.parse("2100-12-31");

                if (inpDate.compareTo(curDate) < 0) {
                    printError("지난 날짜입니다. 다시 입력해주세요.\n");
                    continue;
                } else if (endDate.compareTo(inpDate) < 0) {
                    printError("2100년 이내의 날짜여야 합니다. 다시 입력해주세요.\n");
                    continue;
                }
            } catch (ParseException | IllegalArgumentException e) {
                printError("존재하지 않는 날짜입니다. 다시 입력해주세요.\n");
                continue;
            }

            break;
        }
        selectMovieTimeMenu(movieSchedule);
    }

    /* 부 프롬프트 1.4: 영화 시작 시각 입력 */
    private void selectMovieTimeMenu(MovieSchedule movieSchedule) {

    }

    private void removeMovieMenu() {

    }

    /* 부 프롬프트 2: 예매 */
    private void reservationMenu() {
        int page = 1;
        int totalPage = movieService.getTotalPages();
        List<Movie> movies = movieService.getSortedMovies();

        while (true) {
            String command = "";

            StringBuilder sb = new StringBuilder("============== 영화예매 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++)
                sb.append(idx).append(". ").append(movies.get(i).getName()).append("\n");
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
                    }
                    // else 인 경우 해당 영화를 예매하는 곳으로 넘어가야 함
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if (movieService.hasPreviousPage(page))
                        page--;
                    else
                        printError("이전 페이지가 존재하지 않습니다.");
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (movieService.hasNextPage(page))
                        page++;
                    else
                        printError("다음 페이지가 존재하지 않습니다.\n");
                    break;
                }

                case Literals.BACK:
                    return;
            }
        }
    }
    /* 부 프롬프트 2.1: 예매 날짜 선택 */
    private void selectReservationDateMenu(Ticket ticket) {

    }
    /* 부 프롬프트 2.2: 상영관 선택 */
    private void selectReservationRoomMenu(Ticket ticket) {

    }
    /* 부 프롬프트 2.3: 일정 선택 */
    private void selectReservationTimeMenu(Ticket ticket) {

    }
    /* 부 프롬프트 2.4: 좌석 선택 */
    private void selectReservationSeatMenu(Ticket ticket) {

    }
    /* 부 프롬프트 2.5: 예매 코드 출력 */
    private void printReservationCodeMenu(Ticket ticket) {

    }
    /* 부 프롬프트 3: 예매 취소 */
    private void cancelReservationMenu() {
        println("cancel Reservation menu here");
    }

    public void run(){
        mainMenu();
    }

    public int exit(){
        int exitCode = 0;
        exitCode |= input.close();
        return exitCode;
    }

    private void printError(String s){
        System.err.println("Error : " + s);
        flush();
    }

    private void println(String s){
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

    private boolean checkValidMovieNumber(List<Movie> movies, int page, int num) {
        return getPageNumber(page, num) <= movies.size();
    }

    private boolean checkValidRoomNumber(List<Long> rooms, int page, int num) {
        return getPageNumber(page, num) <= rooms.size();
    }
}
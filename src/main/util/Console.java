package util;

import literal.LiteralRegex;
import literal.Literals;
import repository.MovieRepository;
import repository.MovieScheduleRepository;
import repository.RoomRepository;
import repository.TicketRepository;
import service.MovieScheduleService;
import service.MovieService;
import service.RoomService;
import service.TicketService;

import java.util.List;


public class Console {

    private final Input input;
    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final TicketService ticketService;
    private final RoomService roomService;

    public Console() {
        input = new Input(System.in, LiteralRegex.CONSOLE_DELIMITER);
        roomService = new RoomService(new RoomRepository());
        movieService = new MovieService(new MovieRepository());
        movieScheduleService = new MovieScheduleService(new MovieScheduleRepository());
        ticketService = new TicketService(new TicketRepository());
    }

    public int mainMenu(){
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
                case Literals.QUIT: return exit();
                case Literals.RESERVATION: { reservationMenu(); break; }
                case Literals.MANAGEMENT: { managementMenu(); break; }
                case Literals.CANCEL_RESERVATION: { cancelReservationMenu(); break; }
            }
        }
    }

    private void managementMenu() {
        int page = 1;
        int totalPage = movieService.getTotalPages();
        List<String> movies = movieService.getSortedMovieNames();

        while (true) {
            String command = "";

            StringBuilder sb = new StringBuilder("============== 영화관리 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++)
                sb.append(idx).append(". ").append(movies.get(i)).append("\n");
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n9. 영화 추가\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.MANAGE_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1": case "2": case "3": case "4": case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidMovieNumber(movies, page, num)) {
                        printError("해당 영화가 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    }
                    else 
                        addMovieScheduleMenu();
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
                    addMovieMenu();
                    movies = movieService.getSortedMovieNames();
                    totalPage = movieService.getTotalPages();
                    break;
                }
                case Literals.BACK:
                    return;
            }
        }
    }
    
    private void addMovieScheduleMenu() {
        int page = 1;
        int totalPage = roomService.getTotalPages();
        List<Long> rooms = roomService.getSortedRoomNumbers();

        while (true) {
            String command = "";
            StringBuilder sb = new StringBuilder("============== 일정추가 ==============\n");
            for (int i = (page - 1) * 5, idx = 1; idx <= 5 && i < rooms.size(); i++, idx++)
                sb.append(idx).append(". ").append(Long.toString(rooms.get(i))).append("관\n");
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.MANAGE_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1": case "2": case "3": case "4": case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidRoomNumber(rooms, page, num)) {
                        printError("해당 관이 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    }
                    // else 인 경우 해당 영화 상영일 선택으로 넘어가야 함.
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

                case Literals.BACK:
                    return;
            }
        }
    }

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
    }

    private void removeMovieMenu() {

    }

    private void reservationMenu() {
        int page = 1;
        int totalPage = movieService.getTotalPages();
        List<String> movies = movieService.getSortedMovieNames();

        while (true) {
            String command = "";

            StringBuilder sb = new StringBuilder("============== 영화예매 ==============\n");
            for (int i = (page-1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++)
                sb.append(idx).append(". ").append(movies.get(i)).append("\n");
            sb.append(String.format("=========== 페이지 %d / %d ===========\n", page, totalPage));
            sb.append("7. 이전 페이지\n8. 다음 페이지\n0. 뒤로가기\n입력: ");
            printf(sb.toString());

            if ((command = input.getByPattern(LiteralRegex.RESERVATION_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }

            switch (command) {
                case "1": case "2": case "3": case "4": case "5": {
                    int num = Integer.parseInt(command);
                    if (!checkValidMovieNumber(movies, page, num)) {
                        printError("해당 영화가 존재하지 않습니다. 다시 입력해 주세요.");
                        continue;
                    }
                    // else 인 경우 해당 영화를 예매하는 곳으로 넘어가야 함
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if (movieService.hasPreviousPage(page)) page--;
                    else printError("이전 페이지가 존재하지 않습니다.");
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (movieService.hasNextPage(page)) page++;
                    else printError("다음 페이지가 존재하지 않습니다.\n");
                    break;
                }

                case Literals.BACK: return;
            }
        }
    }
    
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

    private void flush(){
        System.err.flush();
        System.out.flush();
    }

    private boolean checkValidMovieNumber(List<String> movies, int page, int num) {
        return (page - 1) * 5 + num <= movies.size();
    }
    
    private boolean checkValidRoomNumber(List<Long> rooms, int page, int num) {
        return (page - 1) * 5 + num <= rooms.size();
    }
}

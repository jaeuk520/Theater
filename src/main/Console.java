import literal.ConsolePrompt;
import literal.LiteralRegex;
import literal.Literals;

import java.util.List;


public class Console {

    private final Input input;
    private final MovieManager movieManager;
    private final FileLoader fileLoader;

    public Console() {
        input = new Input(System.in, LiteralRegex.CONSOLE_DELIMITER);
        fileLoader = new FileLoader();
        movieManager = new MovieManager(fileLoader);
    }

    public int mainMenu(){
        while (true) {
            String command = "";
            printf(ConsolePrompt.MAIN_MENU_PROMPT);
            if ((command = input.getByPattern(LiteralRegex.MAIN_INPUT)) == null) {
                printError("입력 형식에 맞지 않습니다. 다시 입력해주세요.\n");
                continue;
            }
            switch (command) {
                case Literals.QUIT: return exit();
                case Literals.RESERVATION: { reservationMenu(); break; }
                case Literals.MANAGEMENT: { managementMenu(); break; }
            }
        }
    }

    private void managementMenu() {
        int page = 1;
        int totalPage = movieManager.getTotalPages();
        List<String> movies = movieManager.getSortedMovieNames();

        while (true) {
            String command = "";

            StringBuilder sb = new StringBuilder("============== 영화관리 ==============\n");
            for (int i = (page-1) * 5, idx = 1; idx <= 5 && i < movies.size(); i++, idx++)
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
                    break;
                }

                case Literals.PREVIOUS_PAGE: {
                    if (movieManager.hasPreviousPage(page)) page--;
                    else printError("이전 페이지가 존재하지 않습니다.");
                    break;
                }

                case Literals.NEXT_PAGE: {
                    if (movieManager.hasNextPage(page)) page++;
                    else printError("다음 페이지가 존재하지 않습니다.\n");
                    break;
                }

                case Literals.ADD_MOVIE: {
                    addMovieMenu();
                    movies = movieManager.getSortedMovieNames();
                    break;
                }
                case Literals.BACK: return;
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
            movieManager.addMovie(movieName, runningTime);
            println("영화를 저장했습니다.");
            break;
        }
    }

    private void removeMovieMenu() {

    }

    public void reservationMenu(){
        println("Reservation menu here");
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
        return (page-1) * 5 + num <= movies.size();
    }
}

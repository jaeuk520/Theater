import entity.Movie;
import literal.LiteralRegex;

import java.io.*;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileLoader {

    public Input getInput(String fileName) {
        Input input = null;
        try {
            input = new Input(new FileInputStream(fileName), LiteralRegex.FILE_DELIMITER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input;
    }

    public void save(String fileName, String desc) {
        try {
            File f = new File("movie.txt");
            f.createNewFile();
            FileWriter fileWriter = new FileWriter(f);
            fileWriter.append(desc);
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("파일을 저장하는 중 오류가 발생했습니다.");
        }
    }


    public HashMap<String, Movie> getMovies(){
        HashMap<String, Movie> movies = new HashMap<>();
        Input input = getInput("movie.txt");
        if (input == null) return movies;
        while (input.hasNext()) {
            String movieId = input.getByPattern(LiteralRegex.MOVIE_ID).trim();
            int runningTime = Integer.parseInt(input.getByPattern(LiteralRegex.RUNNING_TIME));
            String movieName = input.getByPattern(LiteralRegex.MOVIE_NAME).trim();
            movies.put(movieId, new Movie(movieId, movieName, runningTime));
        }
        return movies;
    }

    public void saveMovies(HashMap<String, Movie> movies) {
        StringBuilder sb = new StringBuilder();
        for(String key : movies.keySet().stream().sorted().collect(Collectors.toList()))
            sb.append(movies.get(key).toString()).append("\n");
        save("movies.txt", sb.toString());
    }
}

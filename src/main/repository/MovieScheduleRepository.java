package repository;

import database.StringIdEntityDatabase;
import entity.MovieSchedule;

public class MovieScheduleRepository extends StringIdEntityDatabase<MovieSchedule> {

    public MovieScheduleRepository(String path) {
        super(MovieSchedule.class,6, path);
    }
}
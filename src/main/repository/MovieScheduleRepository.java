package repository;

import database.StringIdEntityDatabase;
import entity.MovieSchedule;
import literal.Path;

public class MovieScheduleRepository extends StringIdEntityDatabase<MovieSchedule> {

    public MovieScheduleRepository() {
        super(MovieSchedule.class,6, Path.SCHEDULE_DATA);
    }
}

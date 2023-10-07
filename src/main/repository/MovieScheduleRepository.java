package repository;

import database.StringIdEntityDatabase;
import entity.MovieSchedule;
import literal.IdStrategy;
import literal.Path;

public class MovieScheduleRepository extends StringIdEntityDatabase<MovieSchedule> {
    public MovieScheduleRepository() {
        super(MovieSchedule.class,
                IdStrategy.UUID,
                6,
                Path.SCHEDULE_DATA);
    }
}

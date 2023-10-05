package repository;

import database.StringIdEntityDatabase;
import entity.Ticket;
import literal.IdStrategy;
import literal.Path;

public class TicketRepository extends StringIdEntityDatabase<Ticket> {
    public TicketRepository() {
        super(IdStrategy.UUID,
                6,
                Path.RESERVATION_DATA);
    }
}

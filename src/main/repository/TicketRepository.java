package repository;

import database.StringIdEntityDatabase;
import entity.Ticket;
import literal.Path;

public class TicketRepository extends StringIdEntityDatabase<Ticket> {

    public TicketRepository() {
        super(Ticket.class, 6, Path.RESERVATION_DATA);
    }
}

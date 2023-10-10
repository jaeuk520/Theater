package repository;

import database.StringIdEntityDatabase;
import entity.Ticket;

public class TicketRepository extends StringIdEntityDatabase<Ticket> {

    public TicketRepository(String path) {
        super(Ticket.class, 6, path);
    }
}
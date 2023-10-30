package repository;

import database.StringIdEntityDatabase;
import entity.Ticket;
import java.util.List;
import java.util.stream.Collectors;

public class TicketRepository extends StringIdEntityDatabase<Ticket> {

    public TicketRepository(String path) {
        super(Ticket.class, 6, path);
    }

    public List<Ticket> findAllTicketsByPhoneNumber(String phoneNumber) {
        return findAll().stream()
                .filter(ticket -> ticket.getPhoneNumber().equals(phoneNumber))
                .collect(Collectors.toList());
    }
}

package service;

import entity.MovieSchedule;
import entity.Ticket;
import repository.TicketRepository;

public class TicketService {
    private final TicketRepository ticketRepository;
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void addReservation(MovieSchedule movieSchedule, String seatId){

        ticketRepository.save(new Ticket(movieSchedule, seatId));
    }

    public void cancelReservation(String id){
        ticketRepository.delete(id);
    }

}

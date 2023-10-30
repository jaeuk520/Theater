package service;

import entity.MovieSchedule;
import entity.Ticket;
import repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void addReservation(MovieSchedule movieSchedule, String seatId, String phoneNumber,
                               LocalDateTime reservationTime) {

        ticketRepository.save(new Ticket(null, movieSchedule, seatId, phoneNumber, reservationTime));
    }

    //cancelReservation함수는 예매 취소가 성공하는 경우 true를, 실패하는 경우 false를 반환합니다.
    public boolean cancelReservation(String id, LocalDateTime cancellationTime) {

        if (this.ticketRepository.findById(id).isPresent()) {
            this.ticketRepository.findById(id).get().setIscanceled(cancellationTime);
            return true;
        } else {
            return false;
        }
    }

    //reservation.txt를 돌면서 주어진 전화번호와 동일한 티켓리스트 리턴
    public List<Ticket> getReservationDetails(String phoneNumber) {
        List<Ticket> ticketList = ticketRepository.findAll();
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : ticketList) {
            if (t.getPhoneNumber().equals(phoneNumber)) {
                result.add(t);
            }
        }
        return result;
    }
}

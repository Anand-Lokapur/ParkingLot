package com.parkinglot.repository;

import com.parkinglot.model.Ticket;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepository {

    private final ConcurrentHashMap<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Ticket> closedTickets = new ConcurrentHashMap<>();

    public void saveActiveTicket(Ticket ticket) {
        activeTickets.put(ticket.getTicketId(), ticket);
    }

    public Ticket findActiveTicket(String ticketId) {
        return activeTickets.get(ticketId);
    }

    public void closeTicket(Ticket ticket) {
        activeTickets.remove(ticket.getTicketId());
        closedTickets.put(ticket.getTicketId(), ticket);
    }

    public Collection<Ticket> getAllActiveTickets() {
        return Collections.unmodifiableCollection(activeTickets.values());
    }

    public Collection<Ticket> getAllClosedTickets() {
        return Collections.unmodifiableCollection(closedTickets.values());
    }
}

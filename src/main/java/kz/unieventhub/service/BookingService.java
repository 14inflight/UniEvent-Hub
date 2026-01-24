package kz.unieventhub.service;

import kz.unieventhub.domain.Booking;
import kz.unieventhub.domain.Event;
import kz.unieventhub.domain.Student;
import kz.unieventhub.repo.InMemoryDatabase;

import java.time.LocalDateTime;
import java.util.*;

public final class BookingService {
    private final InMemoryDatabase db;

    public BookingService(InMemoryDatabase db) { this.db = db; }

    public boolean isRegistered(UUID eventId, UUID studentId) {
        return db.eventRegistrations.getOrDefault(eventId, Set.of()).contains(studentId);
    }

    public Booking register(Student student, Event event) {
        Objects.requireNonNull(student);
        Objects.requireNonNull(event);

        if (!event.isApproved()) {
            throw new IllegalStateException("Event is not approved yet");
        }
        if (isRegistered(event.getId(), student.getId())) {
            throw new IllegalStateException("You are already registered for this event");
        }
        if (event.getFreeSpots() <= 0) {
            throw new IllegalStateException("Event is full");
        }

        event.incrementRegistered();

        Booking b = new Booking(UUID.randomUUID(), event.getId(), student.getId(), LocalDateTime.now());
        db.bookings.put(b.getId(), b);
        db.eventRegistrations.computeIfAbsent(event.getId(), k -> new HashSet<>()).add(student.getId());
        return b;
    }

    public void cancel(Student student, Event event) {
        if (!isRegistered(event.getId(), student.getId())) {
            throw new IllegalStateException("You are not registered for this event");
        }
        // remove booking
        UUID toRemove = null;
        for (Booking b : db.bookings.values()) {
            if (b.getEventId().equals(event.getId()) && b.getStudentId().equals(student.getId())) {
                toRemove = b.getId();
                break;
            }
        }
        if (toRemove != null) db.bookings.remove(toRemove);

        db.eventRegistrations.getOrDefault(event.getId(), new HashSet<>()).remove(student.getId());
        event.decrementRegistered();
    }

    public List<Event> listMyEvents(Student student) {
        Set<UUID> mineEventIds = new HashSet<>();
        for (Booking b : db.bookings.values()) {
            if (b.getStudentId().equals(student.getId())) mineEventIds.add(b.getEventId());
        }
        List<Event> res = new ArrayList<>();
        for (UUID id : mineEventIds) {
            Event e = db.events.get(id);
            if (e != null) res.add(e);
        }
        res.sort(Comparator.comparing(Event::getDateTime));
        return res;
    }
}

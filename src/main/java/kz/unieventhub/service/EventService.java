package kz.unieventhub.service;

import kz.unieventhub.domain.Event;
import kz.unieventhub.domain.Organizer;
import kz.unieventhub.domain.Venue;
import kz.unieventhub.repo.InMemoryDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class EventService {
    private final InMemoryDatabase db;

    public EventService(InMemoryDatabase db) { this.db = db; }

    public List<Event> listApprovedEvents() {
        return db.events.values().stream()
                .filter(Event::isApproved)
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public List<Event> listAllEvents() {
        return db.events.values().stream()
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public List<String> listVenueNames() {
        return db.venues.values().stream()
                .map(v -> v.getName())
                .distinct()
                .sorted()
                .toList();
    }

    public List<Event> searchApproved(String query, String venueFilter, String dayFilter) {
        String q = query == null ? "" : query.trim().toLowerCase();

        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);

        return db.events.values().stream()
                .filter(Event::isApproved)
                .filter(e -> {
                    if (q.isBlank()) return true;
                    return e.getTitle().toLowerCase().contains(q) ||
                            e.getDescription().toLowerCase().contains(q);
                })
                .filter(e -> {
                    if (venueFilter == null || venueFilter.equals("ALL")) return true;
                    return e.getVenue().getName().equalsIgnoreCase(venueFilter);
                })
                .filter(e -> {
                    if (dayFilter == null || dayFilter.equals("ALL")) return true;

                    LocalDate d = e.getDateTime().toLocalDate();
                    if (dayFilter.equals("TODAY")) return d.equals(today);
                    if (dayFilter.equals("WEEK")) return !d.isBefore(today) && !d.isAfter(weekEnd);

                    return true;
                })
                .sorted(Comparator.comparing(Event::getDateTime))
                .toList();
    }

    public Event createEvent(Organizer organizer,
                             String title,
                             String description,
                             LocalDateTime dateTime,
                             Venue venue,
                             int capacity) {

        Objects.requireNonNull(organizer);
        Event e = new Event(UUID.randomUUID(), title, description, dateTime, venue, organizer.getId(), capacity);
        db.events.put(e.getId(), e);
        db.eventRegistrations.putIfAbsent(e.getId(), new HashSet<>());
        return e;
    }

    public Optional<Event> findEventByShortId(String shortId8) {
        for (Event e : db.events.values()) {
            if (e.getId().toString().substring(0, 8).equalsIgnoreCase(shortId8)) return Optional.of(e);
        }
        return Optional.empty();
    }

    public void approveEvent(Event event, boolean approved) {
        event.setApproved(approved);
    }
}

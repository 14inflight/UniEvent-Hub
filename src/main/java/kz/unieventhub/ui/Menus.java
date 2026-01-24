package kz.unieventhub.ui;

import kz.unieventhub.domain.*;
import kz.unieventhub.service.BookingService;
import kz.unieventhub.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Menus {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ConsoleIO io;
    private final EventService eventService;
    private final BookingService bookingService;

    public Menus(ConsoleIO io, EventService eventService, BookingService bookingService) {
        this.io = io;
        this.eventService = eventService;
        this.bookingService = bookingService;
    }

    public void studentMenu(Student student) {
        String query = "";
        String venueFilter = "ALL";
        String dayFilter = "ALL"; // TODAY / WEEK / ALL

        while (true) {
            io.println("\n==============================");
            io.println(" STUDENT: " + student.getName());
            io.println("==============================");
            io.println("Search: " + (query.isBlank() ? "(none)" : "\"" + query + "\"")
                    + " | Venue: " + venueFilter + " | Date: " + dayFilter);
            io.println("1) Browse events");
            io.println("2) Search (by title/desc)");
            io.println("3) Filter by venue");
            io.println("4) Filter by date (Today/Week/All)");
            io.println("5) Register for event (choose number)");
            io.println("6) My registrations");
            io.println("7) Cancel registration (choose number)");
            io.println("0) Logout");

            int c = io.readInt("Choose: ", 0, 7);

            try {
                if (c == 0) return;

                if (c == 1) {
                    browseStudentEvents(student, query, venueFilter, dayFilter);
                } else if (c == 2) {
                    query = io.readLine("Enter search text (empty = clear): ");
                } else if (c == 3) {
                    venueFilter = pickVenueFilter();
                } else if (c == 4) {
                    dayFilter = pickDateFilter();
                } else if (c == 5) {
                    registerFlow(student, query, venueFilter, dayFilter);
                } else if (c == 6) {
                    listMy(student);
                } else if (c == 7) {
                    cancelFlow(student);
                }
            } catch (Exception ex) {
                io.println(" Error: " + ex.getMessage());
            }
        }
    }

    private void browseStudentEvents(Student student, String query, String venueFilter, String dayFilter) {
        List<Event> events = eventService.searchApproved(query, venueFilter, dayFilter);
        printEventsWithNumbers(events, student);
        io.readLine("Press Enter to continue...");
    }

    private void registerFlow(Student student, String query, String venueFilter, String dayFilter) {
        List<Event> events = eventService.searchApproved(query, venueFilter, dayFilter);
        if (events.isEmpty()) {
            io.println("No events found.");
            return;
        }
        printEventsWithNumbers(events, student);

        int idx = io.readInt("Enter event number (0 = cancel): ", 0, events.size());
        if (idx == 0) return;

        Event chosen = events.get(idx - 1);
        bookingService.register(student, chosen);
        io.println(" Registered! Free spots: " + chosen.getFreeSpots());
    }

    private void cancelFlow(Student student) {
        List<Event> mine = bookingService.listMyEvents(student);
        if (mine.isEmpty()) {
            io.println("You have no registrations.");
            return;
        }
        io.println("\n-- My registrations --");
        for (int i = 0; i < mine.size(); i++) {
            io.println((i + 1) + ") " + mine.get(i));
        }

        int idx = io.readInt("Enter number to cancel (0 = back): ", 0, mine.size());
        if (idx == 0) return;

        Event chosen = mine.get(idx - 1);
        bookingService.cancel(student, chosen);
        io.println(" Canceled.");
    }

    private void listMy(Student student) {
        List<Event> mine = bookingService.listMyEvents(student);
        io.println("\n-- My events --");
        if (mine.isEmpty()) io.println("(empty)");
        for (Event e : mine) io.println("• " + e);
        io.readLine("Press Enter to continue...");
    }

    public void organizerMenu(Organizer organizer, Map<UUID, Venue> venues) {
        while (true) {
            io.println("\n==============================");
            io.println(" ORGANIZER: " + organizer.getName());
            io.println("==============================");
            io.println("1) View my events");
            io.println("2) Create event (guided)");
            io.println("0) Logout");

            int c = io.readInt("Choose: ", 0, 2);

            try {
                if (c == 0) return;
                if (c == 1) listOrganizerEvents(organizer);
                if (c == 2) createEventGuided(organizer, venues);
            } catch (Exception ex) {
                io.println(" Error: " + ex.getMessage());
            }
        }
    }

    private void listOrganizerEvents(Organizer organizer) {
        List<Event> all = eventService.listAllEvents();
        List<Event> mine = all.stream()
                .filter(e -> e.getOrganizerId().equals(organizer.getId()))
                .toList();

        io.println("\n-- My created events --");
        if (mine.isEmpty()) io.println("(empty)");
        for (Event e : mine) io.println("• " + e);
        io.readLine("Press Enter to continue...");
    }

    private void createEventGuided(Organizer organizer, Map<UUID, Venue> venues) {
        io.println("\n-- Choose venue --");
        List<Venue> list = new ArrayList<>(venues.values());
        for (int i = 0; i < list.size(); i++) {
            Venue v = list.get(i);
            io.println((i + 1) + ") " + v.getName() + " (cap=" + v.getCapacity() + ")");
        }

        int vIdx = io.readInt("Venue number (0 = cancel): ", 0, list.size());
        if (vIdx == 0) return;

        Venue venue = list.get(vIdx - 1);

        String title = io.readLine("Title: ");
        String desc = io.readLine("Description: ");

        LocalDateTime dateTime = readDateTime();
        int cap = io.readInt("Capacity (1.." + venue.getCapacity() + "): ", 1, venue.getCapacity());

        Event e = eventService.createEvent(organizer, title, desc, dateTime, venue, cap);
        io.println(" Created: " + e);
    }

    private LocalDateTime readDateTime() {
        while (true) {
            String dt = io.readLine("DateTime (yyyy-MM-dd HH:mm): ");
            try {
                return LocalDateTime.parse(dt, FMT);
            } catch (Exception ex) {
                io.println("Invalid format. Example: 2026-01-30 16:00");
            }
        }
    }

    public void adminMenu(Admin admin) {
        while (true) {
            io.println("\n==============================");
            io.println(" ADMIN: " + admin.getName());
            io.println("==============================");
            io.println("1) View all events");
            io.println("2) Approve/Reject event (choose number)");
            io.println("0) Logout");

            int c = io.readInt("Choose: ", 0, 2);

            try {
                if (c == 0) return;
                if (c == 1) {
                    printAllEvents();
                    io.readLine("Press Enter to continue...");
                }
                if (c == 2) approveFlow();
            } catch (Exception ex) {
                io.println(" Error: " + ex.getMessage());
            }
        }
    }

    private void printAllEvents() {
        List<Event> events = eventService.listAllEvents();
        io.println("\n-- All events --");
        if (events.isEmpty()) io.println("(empty)");
        for (int i = 0; i < events.size(); i++) {
            io.println((i + 1) + ") " + events.get(i));
        }
    }

    private void approveFlow() {
        List<Event> events = eventService.listAllEvents();
        if (events.isEmpty()) {
            io.println("(empty)");
            return;
        }
        printAllEvents();
        int idx = io.readInt("Choose event number (0 = back): ", 0, events.size());
        if (idx == 0) return;

        Event e = events.get(idx - 1);
        io.println("Selected: " + e);
        int v = io.readInt("1) Approve   2) Reject : ", 1, 2);
        eventService.approveEvent(e, v == 1);
        io.println(" Updated: " + e);
    }

    private void printEventsWithNumbers(List<Event> events, Student student) {
        io.println("\n-- Events --");
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            boolean registered = bookingService.isRegistered(e.getId(), student.getId());
            String mark = registered ? " (registered)" : "";
            io.println((i + 1) + ") " + e + mark);
        }
    }

    private String pickVenueFilter() {
        List<String> venues = eventService.listVenueNames();
        io.println("\n-- Venue filter --");
        io.println("0) ALL");
        for (int i = 0; i < venues.size(); i++) {
            io.println((i + 1) + ") " + venues.get(i));
        }
        int idx = io.readInt("Choose: ", 0, venues.size());
        if (idx == 0) return "ALL";
        return venues.get(idx - 1);
    }

    private String pickDateFilter() {
        io.println("\n-- Date filter --");
        io.println("1) TODAY");
        io.println("2) WEEK");
        io.println("3) ALL");
        int idx = io.readInt("Choose: ", 1, 3);
        return switch (idx) {
            case 1 -> "TODAY";
            case 2 -> "WEEK";
            default -> "ALL";
        };
    }
}

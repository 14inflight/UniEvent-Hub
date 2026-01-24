package kz.unieventhub.app;

import kz.unieventhub.domain.*;
import kz.unieventhub.repo.InMemoryDatabase;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SeedData {
    private SeedData() {}

    public static void fill(InMemoryDatabase db) {
        // users
        Student s1 = new Student(UUID.randomUUID(), "Alikhan A.");
        Organizer o1 = new Organizer(UUID.randomUUID(), "Ramazan");
        Admin a1 = new Admin(UUID.randomUUID(), "Madiyar S.");

        db.users.put(s1.getId(), s1);
        db.users.put(o1.getId(), o1);
        db.users.put(a1.getId(), a1);

        // venues
        Venue v1 = new Venue(UUID.randomUUID(), "Main Hall", 120);
        Venue v2 = new Venue(UUID.randomUUID(), "Room B-204", 40);

        db.venues.put(v1.getId(), v1);
        db.venues.put(v2.getId(), v2);

        // sample events
        Event e1 = new Event(UUID.randomUUID(), "AI Club Meetup",
                "Talk + networking",
                LocalDateTime.now().plusDays(3).withHour(16).withMinute(0),
                v2, o1.getId(), 30);

        Event e2 = new Event(UUID.randomUUID(), "Charity Run",
                "Sports event for students",
                LocalDateTime.now().plusDays(7).withHour(10).withMinute(0),
                v1, o1.getId(), 100);

        db.events.put(e1.getId(), e1);
        db.events.put(e2.getId(), e2);

        db.eventRegistrations.put(e1.getId(), new java.util.HashSet<>());
        db.eventRegistrations.put(e2.getId(), new java.util.HashSet<>());
    }
}

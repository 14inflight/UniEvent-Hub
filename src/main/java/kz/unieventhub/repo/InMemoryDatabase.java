package kz.unieventhub.repo;

import kz.unieventhub.domain.*;

import java.util.*;

public final class InMemoryDatabase {
    public final Map<UUID, User> users = new LinkedHashMap<>();
    public final Map<UUID, Venue> venues = new LinkedHashMap<>();
    public final Map<UUID, Event> events = new LinkedHashMap<>();
    public final Map<UUID, Booking> bookings = new LinkedHashMap<>();

    // быстрый поиск: eventId -> set(studentId)
    public final Map<UUID, Set<UUID>> eventRegistrations = new HashMap<>();
}

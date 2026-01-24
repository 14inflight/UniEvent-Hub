package kz.unieventhub.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Event {
    private final UUID id;
    private final String title;
    private final String description;
    private final LocalDateTime dateTime;
    private final Venue venue;
    private final UUID organizerId;

    private boolean approved;
    private final int capacity;
    private int registeredCount;

    public Event(UUID id,
                 String title,
                 String description,
                 LocalDateTime dateTime,
                 Venue venue,
                 UUID organizerId,
                 int capacity) {

        if (capacity <= 0) throw new IllegalArgumentException("Event capacity must be > 0");
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
        this.dateTime = Objects.requireNonNull(dateTime);
        this.venue = Objects.requireNonNull(venue);
        this.organizerId = Objects.requireNonNull(organizerId);

        if (capacity > venue.getCapacity()) {
            throw new IllegalArgumentException("Event capacity cannot exceed venue capacity");
        }
        this.capacity = capacity;
        this.approved = true;
        this.registeredCount = 0;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDateTime() { return dateTime; }
    public Venue getVenue() { return venue; }
    public UUID getOrganizerId() { return organizerId; }
    public boolean isApproved() { return approved; }
    public int getCapacity() { return capacity; }
    public int getRegisteredCount() { return registeredCount; }
    public int getFreeSpots() { return capacity - registeredCount; }

    public void setApproved(boolean approved) { this.approved = approved; }

    public void incrementRegistered() {
        if (registeredCount >= capacity) throw new IllegalStateException("No free spots left");
        registeredCount++;
    }

    public void decrementRegistered() {
        if (registeredCount <= 0) return;
        registeredCount--;
    }

    @Override
    public String toString() {
        return "[" + id.toString().substring(0, 8) + "] "
                + title + " | " + dateTime
                + " | " + venue.getName()
                + " | " + registeredCount + "/" + capacity
                + (approved ? "" : " (PENDING)");
    }
}

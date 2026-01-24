package kz.unieventhub.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Booking {
    private final UUID id;
    private final UUID eventId;
    private final UUID studentId;
    private final LocalDateTime createdAt;

    public Booking(UUID id, UUID eventId, UUID studentId, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id);
        this.eventId = Objects.requireNonNull(eventId);
        this.studentId = Objects.requireNonNull(studentId);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public UUID getId() { return id; }
    public UUID getEventId() { return eventId; }
    public UUID getStudentId() { return studentId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

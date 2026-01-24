package kz.unieventhub.domain;

import java.util.Objects;
import java.util.UUID;

public final class Venue {
    private final UUID id;
    private final String name;
    private final int capacity;

    public Venue(UUID id, String name, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Venue capacity must be > 0");
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.capacity = capacity;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return name + " (cap=" + capacity + ")";
    }
}

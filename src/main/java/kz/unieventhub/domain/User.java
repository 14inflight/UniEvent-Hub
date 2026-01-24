package kz.unieventhub.domain;

import java.util.Objects;
import java.util.UUID;

public abstract class User {
    private final UUID id;
    private final String name;
    private final Role role;

    protected User(UUID id, String name, Role role) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.role = Objects.requireNonNull(role);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Role getRole() { return role; }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}

package kz.unieventhub.domain;

import java.util.UUID;

public final class Organizer extends User {
    public Organizer(UUID id, String name) {
        super(id, name, Role.ORGANIZER);
    }
}

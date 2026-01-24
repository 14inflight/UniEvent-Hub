package kz.unieventhub.domain;

import java.util.UUID;

public final class Admin extends User {
    public Admin(UUID id, String name) {
        super(id, name, Role.ADMIN);
    }
}

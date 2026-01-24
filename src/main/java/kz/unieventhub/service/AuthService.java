package kz.unieventhub.service;

import kz.unieventhub.domain.User;
import kz.unieventhub.repo.InMemoryDatabase;

import java.util.Optional;
import java.util.UUID;

public final class AuthService {
    private final InMemoryDatabase db;

    public AuthService(InMemoryDatabase db) { this.db = db; }

    public Optional<User> findUserByShortId(String shortId8) {
        for (User u : db.users.values()) {
            String s = u.getId().toString().substring(0, 8);
            if (s.equalsIgnoreCase(shortId8)) return Optional.of(u);
        }
        return Optional.empty();
    }

    public Optional<User> findUserById(UUID id) {
        return Optional.ofNullable(db.users.get(id));
    }
}

package kz.unieventhub.domain;

import java.util.UUID;

public final class Student extends User {
    public Student(UUID id, String name) {
        super(id, name, Role.STUDENT);
    }
}

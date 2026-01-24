package kz.unieventhub.app;

import kz.unieventhub.domain.*;
import kz.unieventhub.repo.InMemoryDatabase;
import kz.unieventhub.service.BookingService;
import kz.unieventhub.service.EventService;
import kz.unieventhub.ui.ConsoleIO;
import kz.unieventhub.ui.Menus;

import java.util.ArrayList;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();
        SeedData.fill(db);

        ConsoleIO io = new ConsoleIO();
        EventService eventService = new EventService(db);
        BookingService bookingService = new BookingService(db);
        Menus menus = new Menus(io, eventService, bookingService);

        io.println("=== UniEvent Hub (Console) ===");

        while (true) {
            io.println("\n==============================");
            io.println(" Select user");
            io.println("==============================");

            List<User> users = new ArrayList<>(db.users.values());
            for (int i = 0; i < users.size(); i++) {
                io.println((i + 1) + ") " + users.get(i));
            }
            io.println("0) Exit");

            int choice = io.readInt("Choose: ", 0, users.size());
            if (choice == 0) {
                io.println("Bye!");
                return;
            }

            User user = users.get(choice - 1);

            switch (user.getRole()) {
                case STUDENT -> menus.studentMenu((Student) user);
                case ORGANIZER -> menus.organizerMenu((Organizer) user, db.venues);
                case ADMIN -> menus.adminMenu((Admin) user);
            }
        }
    }
}

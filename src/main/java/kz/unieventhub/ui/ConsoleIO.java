package kz.unieventhub.ui;

import java.util.Scanner;

public final class ConsoleIO {
    private final Scanner sc = new Scanner(System.in);

    public void println(String s) { System.out.println(s); }
    public void print(String s) { System.out.print(s); }

    public String readLine(String prompt) {
        print(prompt);
        return sc.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String s = readLine(prompt);
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                println("Enter a number from " + min + " to " + max + ".");
            }
        }
    }
}

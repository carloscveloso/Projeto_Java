package dentalcare.app;

import java.util.Scanner;

public class ConsoleUI {
    public void showHeader(String title) {
        System.out.println("====================================================");
        System.out.println(center(title, 52));
        System.out.println("====================================================");
    }

    public void showSection(String title) {
        System.out.println("\n" + title);
        System.out.println("-".repeat(Math.min(title.length(), 40)));
    }

    public void showDivider() {
        System.out.println("\n----------------------------------------------------");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public String prompt(String label, Scanner scanner) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    public void pause(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = width - text.length();
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }
}

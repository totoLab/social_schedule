package utils;

import java.util.Scanner;

public class Utils {

    public static boolean yesNo(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String response;
        boolean result = false;

        while (true) {
            System.out.printf("%s (y/n): ", prompt);
            response = scanner.nextLine().trim().toLowerCase();

            if (response.startsWith("y") || response.startsWith("n")) {
                if (response.startsWith("y")) {
                    result = true;
                }
                break;
            }
            System.out.println("Invalid response. Please enter 'yes' or 'no'.");
        }

        scanner.close();
        return result;
    }
}

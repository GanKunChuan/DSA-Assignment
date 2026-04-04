/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    
    public static String readNumberOnly(String prompt, int exactLength) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("\\d+") && input.length() == exactLength) {
                return input;
            }
            System.out.println("Invalid. Must be " + exactLength + " digits only.");
        }
    }
    
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }

            return input;
        }
    }
    
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
    
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
    
    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Enter y/n");
        }
    }
    
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            try {
                return LocalDate.parse(scanner.nextLine().trim(), dateFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date. Use yyyy-MM-dd");
            }
        }
    }
    
    public static LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (HH:mm): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Time cannot be empty.");
                continue;
            }
            try {
                return LocalTime.parse(input, timeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid time. Use HH:mm");
            }
        }
    }
    public static LocalTime readValidTime(String prompt) {
        while (true) {
            LocalTime time = InputHelper.readTime(prompt);
            int hour = time.getHour();
            int minute = time.getMinute();

            if (hour < 10 || hour > 16) {
                System.out.println("Hour must be between 10:00 and 16:00");
                continue;
            }
            if (minute != 0) {
                System.out.println("Minutes must be 00 (e.g., 10:00, 11:00)");
                continue;
            }
            return time;
        }
    }
    
    public static LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");
            try {
                return LocalDateTime.parse(scanner.nextLine().trim(), dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date/time. Use yyyy-MM-dd HH:mm");
            }
        }
    }
}
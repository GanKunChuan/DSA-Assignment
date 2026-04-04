/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

/**
 *
 * @author Soh Lian Ze, Bob Gan Kun Chuan
 */

import Control.UserController;
import Entity.*;
import Helper.InputHelper;
import Helper.OutputHelper;
import java.time.DateTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;


    
public class ManageUserUI {
    
    private UserController controller;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    
    public ManageUserUI(UserController controller) {
        this.controller = controller;
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }
    
    public void start() {
        while (true) {
            showMainMenu();
            int choice = InputHelper.readInt("Choose option: ");
            
            switch (choice) {
                case 1 -> patientMenu();
                case 2 -> doctorMenu();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    // =========================================================
    // ================= MAIN MENU =============================
    // =========================================================
    
    private void showMainMenu() {
        System.out.println("\n========== MEDICAL SYSTEM ==========");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("0. Exit");
        System.out.println("====================================");
    }
    
    // =========================================================
    // ================= PATIENT MENU ==========================
    // =========================================================
    
    private void patientMenu() {
        while (true) {
            showPatientMenu();
            int choice = InputHelper.readInt("Choose option: ");
            
            switch (choice) {
                case 1 -> registerPatient();
                case 2 -> findAndUpdatePatient();
                case 3 -> findAndDeletePatient();
                case 4 -> listAllPatients();
                case 5 -> searchPatientsByName();
                case 6 -> viewPatientsByDate();
                case 7 -> viewPatientsByMonth();
                case 8 -> viewPatientsByCompany();
                case 9 -> viewPatientStatistics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private void showPatientMenu() {
        System.out.println("\n========== PATIENT MANAGEMENT ==========");
        System.out.println("1. Register New Patient");
        System.out.println("2. Find and Update Patient");
        System.out.println("3. Find and Delete Patient");
        System.out.println("4. List All Patients");
        System.out.println("5. Search Patients by Name");
        System.out.println("6. View Patients Registered by Date");
        System.out.println("7. View Patients Registered by Month");
        System.out.println("8. View Patients by Company");
        System.out.println("9. View Statistics");
        System.out.println("0. Back to Main Menu");
        System.out.println("========================================");
    }
    
    // =========================================================
    // ================= PATIENT OPERATIONS ====================
    // =========================================================
    
    private void registerPatient() {
        System.out.println("\n--- Register New Patient ---");
        
        String patientID = InputHelper.readString("Patient ID: ");
        String name = InputHelper.readString("Name: ");
        String nric = InputHelper.readNumberOnly("NRIC: ", 12);
        int age = InputHelper.readInt("Age: ");
        boolean hasMedicalCard = InputHelper.readBoolean("Has Medical Card? (y/n): ");
        String allergy = InputHelper.readString("Allergy (or 'none'): ");
        String contactNumber = InputHelper.readNumberOnly("Contact Number (10 digits): ", 10);
        
        // SELECT COMPANY FROM PREDEFINED LIST
        Company company = selectCompany();
        
        // Get registration date from user (or use current date)
        System.out.println("\n--- Registration Date ---");
        LocalDate regDate = InputHelper.readDate("Enter registration date (yyyy-MM-dd): ");
        
        boolean success = controller.registerPatient(patientID, name, nric, age,
                hasMedicalCard, allergy, contactNumber, company, regDate);
        
        if (success) {
            System.out.println(" Patient registered successfully!");
        } else {
            System.out.println(" Registration failed. NRIC may already exist.");
        }
    }
    
    
    private Company selectCompany() {
        System.out.println("\n--- Select Company ---");

        // First pass: display all companies (count and show)
        Iterator<Company> displayIt = controller.getAllCompanies();
        int index = 1;
        int companyCount = 0;
        while (displayIt.hasNext()) {
            Company c = displayIt.next();
            System.out.println(index++ + ". " + c.getCompanyName() + 
                    " (" + c.getCompanyType() + ") - " + 
                    c.getCompanyLocation() + " - " +
                    (c.getDiscountRate() * 100) + "% discount");
            companyCount++;
        }
        System.out.println("0. None / No Company");
        
        if (companyCount == 0) {
            System.out.println("No companies available.");
            return null;
        }

        int choice = InputHelper.readInt("Choose company: ");
        if (choice < 1 || choice > companyCount) {
            System.out.println("Invalid choice. No company selected.");
            return null;
        }

        // Second pass: find company by position (still no List)
        Iterator<Company> findIt = controller.getAllCompanies();
        int current = 1;
        while (findIt.hasNext()) {
            Company c = findIt.next();
            if (current == choice) {
                return c;
            }
            current++;
        }
        
        return null;

        
    }
    
    
    private void findAndUpdatePatient() {
        System.out.println("\n--- Find Patient to Update ---");
        String nric = InputHelper.readNumberOnly("Enter NRIC: ", 12);
        
        Patient p = controller.findPatientByNRIC(nric);
        if (p == null) {
            System.out.println(" Patient not found.");
            return;
        }
        
        displayPatientDetails(p);
        updatePatientMenu(p);
    }
    
    private void updatePatientMenu(Patient p) {
        while (true) {
            System.out.println("\n--- Update Options ---");
            System.out.println("1. Update Contact Number");
            System.out.println("2. Update Allergy");
            System.out.println("3. Update Company");
            System.out.println("4. Update Medical Card Status");
            System.out.println("0. Cancel");
            
            int choice = InputHelper.readInt("Choose: ");
            
            switch (choice) {
                case 1 -> {
                    String newContact = InputHelper.readNumberOnly("New Contact Number (10 digits): ", 10);
                    if (controller.updatePatientContactNumber(p, newContact)) {
                        System.out.println(" Contact updated!");
                    } else {
                        System.out.println(" Update failed.");
                    }
                }
                case 2 -> {
                    String newAllergy = InputHelper.readString("New Allergy: ");
                    if (controller.updatePatientAllergy(p, newAllergy)) {
                        System.out.println(" Allergy updated!");
                    } else {
                        System.out.println(" Update failed.");
                    }
                }
                case 3 -> {
                    System.out.println("\n--- Select New Company ---");
                    Company newCompany = selectCompany();  // Reuse the same selectCompany() method
                    if (controller.updatePatientCompany(p, newCompany)) {
                        System.out.println(" Company updated!");
                    } else {
                        System.out.println(" Update failed.");
                    }
                }
                case 4 -> {
                    boolean status = InputHelper.readBoolean("Has Medical Card? (y/n): ");
                    if (controller.updatePatientMedicalCard(p, status)) {
                        System.out.println(" Medical Card status updated!");
                    } else {
                        System.out.println(" Update failed.");
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
    
    private void findAndDeletePatient() {
        System.out.println("\n--- Find Patient to Delete ---");
        String nric = InputHelper.readString("Enter NRIC: ");
        
        Patient p = controller.findPatientByNRIC(nric);
        if (p == null) {
            System.out.println(" Patient not found.");
            return;
        }
        
        displayPatientDetails(p);
        boolean confirm = InputHelper.readBoolean("Confirm delete? (y/n): ");
        
        if (confirm) {
            Patient deleted = controller.deletePatient(p);
            if (deleted != null) {
                System.out.println(" Patient deleted successfully!");
            } else {
                System.out.println(" Delete failed.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }
    
    private void listAllPatients() {
        System.out.println("\n--- All Patients ---");
        Iterator<Patient> it = controller.getAllPatients();
        
        if (!it.hasNext()) {
            System.out.println("No patients registered.");
            return;
        }
        
        System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
            "ID", "Name", "NRIC", "Age", "MedCard", "Company", "Allergy", "Reg Date");
        System.out.println("-----------|----------------------|-----------------|-----|---------|-----------------|-----------------|-----------");
        
        int count = 0;
        while (it.hasNext()) {
            displayPatientBrief(it.next());
            count++;
        }
        System.out.println("\nTotal: " + count + " patients");
    }
    
    private void searchPatientsByName() {
        System.out.println("\n--- Search by Name ---");
        String name = InputHelper.readString("Enter name: ");
        
        Iterator<Patient> it = controller.findPatientByName(name);
        
        if (!it.hasNext()) {
            System.out.println("No patients found with name: " + name);
            return;
        }
        
        System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
            "ID", "Name", "NRIC", "Age", "MedCard", "Company", "Allergy", "Reg Date");
        System.out.println("-----------|----------------------|-----------------|-----|---------|-----------------|-----------------|-----------");
        int count = 0;
        while (it.hasNext()) {
            displayPatientBrief(it.next());
            count++;
        }
        System.out.println("Found: " + count + " patient(s)");
    }
    
    private void viewPatientsByDate() {
        System.out.println("\n--- View by Date ---");
        LocalDate date = InputHelper.readDate("Enter date (yyyy-MM-dd): ");
        
        Iterator<Patient> it = controller.getNewPatientsToday(date);
        
        if (!it.hasNext()) {
            System.out.println("No patients registered on " + date);
            return;
        }
        
        System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
            "ID", "Name", "NRIC", "Age", "MedCard", "Company", "Allergy", "Reg Date");
        System.out.println("-----------|----------------------|-----------------|-----|---------|-----------------|-----------------|-----------");
        int count = 0;
        while (it.hasNext()) {
            displayPatientBrief(it.next());
            count++;
        }
        System.out.println("Total: " + count + " patients registered on " + date);
    }
    
    private void viewPatientsByMonth() {
        while (true) {
            System.out.println("\n--- View by Month ---");
            int monthNum = InputHelper.readInt("Enter month (1-12): ");

            try {
                Month month = Month.of(monthNum);

                Iterator<Patient> it = controller.getNewPatientsByMonth(month);

                if (!it.hasNext()) {
                    System.out.println("No patients registered in " + month);
                    return;
                }
                System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
                    "ID", "Name", "NRIC", "Age", "MedCard", "Company", "Allergy", "Reg Date");
                System.out.println("-----------|----------------------|-----------------|-----|---------|-----------------|-----------------|-----------");
                int count = 0;
                while (it.hasNext()) {
                    displayPatientBrief(it.next());
                    count++;
                }
                System.out.println("Total: " + count + " patients registered in " + month);
                break;  // Exit loop on success

            } catch (DateTimeException e) {
                System.out.println("Invalid month. Use 1-12.");
                // Loop continues, ask again
            }
        }
    }
    
    private void viewPatientsByCompany() {
        System.out.println("\n--- View by Company ---");
        Company selected = selectCompany();  // Reuse existing method

        if (selected == null) {
            System.out.println("No company selected.");
            return;
        }

        String companyName = selected.getCompanyName();
        Iterator<Patient> it = controller.getPatientsByCompany(companyName);

        if (!it.hasNext()) {
            System.out.println("No patients from company: " + companyName);
            return;
        }
        
        System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
            "ID", "Name", "NRIC", "Age", "MedCard", "Company", "Allergy", "Reg Date");
        System.out.println("-----------|----------------------|-----------------|-----|---------|-----------------|-----------------|-----------");
        int count = 0;
        while (it.hasNext()) {
            displayPatientBrief(it.next());
            count++;
        }
        System.out.println("Total: " + count + " patient(s) from " + companyName);
    
    }
    
    private void viewPatientStatistics() {
        System.out.println("\n--- Patient Statistics ---");
        System.out.println("Total Patients: " + controller.getTotalPatients());
        
        // Today's count
        Iterator<Patient> today = controller.getNewPatientsToday(LocalDate.now());
        int todayCount = 0;
        while (today.hasNext()) {
            todayCount++;
            today.next();
        }
        System.out.println("Registered Today: " + todayCount);
        
        // Current month count
        Iterator<Patient> thisMonth = controller.getNewPatientsByMonth(LocalDate.now().getMonth());
        int monthCount = 0;
        while (thisMonth.hasNext()) {
            monthCount++;
            thisMonth.next();
        }
        System.out.println("Registered This Month: " + monthCount);
    }
    
    // =========================================================
    // ================= DOCTOR MENU ===========================
    // =========================================================
    
    private void doctorMenu() {
        while (true) {
            showDoctorMenu();
            int choice = InputHelper.readInt("Choose option: ");
            
            switch (choice) {
                case 1 -> registerDoctor();
                case 2 -> findAndDeleteDoctor();
                case 3 -> listAllDoctors();
                case 4 -> searchDoctorsByName();
                case 5 -> viewDoctorsBySpecialization();
                case 6 -> viewDoctorStatistics();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    private void showDoctorMenu() {
        System.out.println("\n========== DOCTOR MANAGEMENT ==========");
        System.out.println("1. Register New Doctor");
        System.out.println("2. Find and Delete Doctor");
        System.out.println("3. List All Doctors");
        System.out.println("4. Search Doctors by Name");
        System.out.println("5. View Doctors by Specialization");
        System.out.println("6. View Statistics");
        System.out.println("0. Back to Main Menu");
        System.out.println("=======================================");
    }
    
    private void registerDoctor() {
        System.out.println("\n--- Register New Doctor ---");
        
        String id = InputHelper.readString("Doctor ID: ");
        String name = InputHelper.readString("Name: ");
        
        // Safe enum selection - no String to enum conversion error
        Specialization spec = selectSpecialization();
        
        boolean success = controller.registerDoctor(id, name, spec);
        
        if (success) {
            System.out.println(" Doctor registered successfully!");
        } else {
            System.out.println(" Registration failed. ID may already exist.");
        }
    }
    
    
    private void findAndDeleteDoctor() {
        System.out.println("\n--- Find Doctor to Delete ---");
        String id = InputHelper.readString("Enter Doctor ID: ");
        
        Doctor d = controller.findDoctorByID(id);
        if (d == null) {
            System.out.println(" Doctor not found.");
            return;
        }
        
        displayDoctorDetails(d);
        boolean confirm = InputHelper.readBoolean("Confirm delete? (y/n): ");
        
        if (confirm) {
            Doctor deleted = controller.deleteDoctor(d);
            if (deleted != null) {
                System.out.println(" Doctor deleted successfully!");
            } else {
                System.out.println(" Delete failed.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }
    
    private void listAllDoctors() {
        System.out.println("\n--- All Doctors ---");
        Iterator<Doctor> it = controller.getAllDoctors();
        
        if (!it.hasNext()) {
            System.out.println("No doctors registered.");
            return;
        }
        
        int count = 0;
        while (it.hasNext()) {
            displayDoctorBrief(it.next());
            count++;
        }
        System.out.println("\nTotal: " + count + " doctors");
    }
    
    private void searchDoctorsByName() {
        System.out.println("\n--- Search Doctors by Name ---");
        String name = InputHelper.readString("Enter name: ");
        
        Iterator<Doctor> it = controller.findDoctorByName(name);
        
        if (!it.hasNext()) {
            System.out.println("No doctors found with name: " + name);
            return;
        }
        
        int count = 0;
        while (it.hasNext()) {
            displayDoctorBrief(it.next());
            count++;
        }
        System.out.println("Found: " + count + " doctor(s)");
    }
    
    
    private Specialization selectSpecialization() {
        System.out.println("\n--- Select Specialization ---");

        // Display enum options with numbers
        Specialization[] specs = Specialization.values();
        for (int i = 0; i < specs.length; i++) {
            System.out.println((i + 1) + ". " + specs[i]);
        }

        int choice = InputHelper.readInt("Choose (1-" + specs.length + "): ");

        if (choice >= 1 && choice <= specs.length) {
            return specs[choice - 1];  // Returns enum directly
        }

        System.out.println("Invalid choice. Defaulting to GENERAL.");
        return Specialization.GENERAL;
    }
    
    private void viewDoctorsBySpecialization() {
        System.out.println("\n--- View by Specialization ---");
        System.out.println("Specializations:");
        for (Specialization s : Specialization.values()) {
            System.out.println("  - " + s);
        }
        // Safe enum selection - no String to enum conversion error
        Specialization spec = selectSpecialization();
        
        Iterator<Doctor> it = controller.getDoctorsBySpecialization(spec);
        
        if (!it.hasNext()) {
            System.out.println("No doctors with specialization: " + spec);
            return;
        }
        
        int count = 0;
        while (it.hasNext()) {
            displayDoctorBrief(it.next());
            count++;
        }
        System.out.println("Total: " + count + " doctor(s) with specialization " + spec);
    }
    
    
    private void viewDoctorStatistics() {
        System.out.println("\n--- Doctor Statistics ---");
        System.out.println("Total Doctors: " + controller.getTotalDoctors());
    }
    
    // =========================================================
    // ================= DISPLAY HELPERS =======================
    // =========================================================
    
    private void displayPatientDetails(Patient p) {
        System.out.println("\n--- Patient Details ---");
        System.out.println("ID: " + p.getPatientID());
        System.out.println("Name: " + p.getName());
        System.out.println("NRIC: " + p.getNric());
        System.out.println("Age: " + p.getAge());
        System.out.println("Medical Card: " + (p.hasMedicalCard() ? "Yes" : "No"));
        System.out.println("Allergy: " + (p.getAllergy() == null ? "None" : p.getAllergy()));
        System.out.println("Contact: " + p.getContactNumber());
        System.out.println("Company: " + (p.getCompany() == null ? "None" : p.getCompany().getCompanyName()));
        System.out.println("Registered: " + p.getRegisterDate().format(dateFormatter));
    }
    
    private void displayPatientBrief(Patient p) {
        System.out.printf("%-10s | %-20s | %-15s | %-3s | %-7s | %-15s | %-15s | %-10s%n",
            p.getPatientID(),
            OutputHelper.truncate(p.getName(), 20),
            p.getNric(),
            p.getAge(),
            p.hasMedicalCard() ? "Yes" : "No",
            p.getCompany() == null ? "None" : OutputHelper.truncate(p.getCompany().getCompanyName(), 15),
            p.getAllergy() == null || p.getAllergy().equalsIgnoreCase("none") ? "None" : OutputHelper.truncate(p.getAllergy(), 15),
            p.getRegisterDate().format(dateFormatter));
    }
    
    private void displayDoctorDetails(Doctor d) {
        System.out.println("\n--- Doctor Details ---");
        System.out.println("ID: " + d.getDoctorID());
        System.out.println("Name: " + d.getName());
        System.out.println("Specialization: " + d.getSpecialization());
    }
    
    private void displayDoctorBrief(Doctor d) {
        System.out.printf("%-10s | %-20s | %-15s%n",
                d.getDoctorID(),
                d.getName(),
                d.getSpecialization());
    }
    
    
    
    
    
    
    
    
    /*
    // =========================================================
    // ================= INPUT HELPERS =========================
    // =========================================================
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
    
    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Enter y/n");
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;  // Use current date
        }
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (Exception e) {
            System.out.println("Invalid date. Using today's date.");
            return null;
        }
    }
    
    private LocalTime getTimeInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(scanner.nextLine().trim(), timeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid time. Use HH:mm");
            }
        }
    } */
    
}


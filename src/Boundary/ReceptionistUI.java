/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.VisitController;
import Control.ServiceController;
import Entity.AppointmentVisit;
import Entity.Invoice;
import Entity.Service;
import Entity.Specialization;
import Helper.InputHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import Entity.Visit;
import Helper.OutputHelper;

/**
 *
 * @author Soh Lian Ze
 */
public class ReceptionistUI {
    private VisitController visitController;
    private ServiceController serviceController;
   

    public ReceptionistUI(VisitController vc, ServiceController sc) {
        this.visitController = vc;
        this.serviceController = sc;
    }

    public void menu() {

        int choice;
        do {
            System.out.println("\n=== Receptionist Menu ===");
            System.out.println("1. Create Appointment");
            System.out.println("2. Register Walk-in");
            System.out.println("3. Confirm Deposit");
            System.out.println("4. View All Visits");
            System.out.println("5. View Visits by Date");
            System.out.println("6. Show Invoice by Visit ID");
            System.out.println("7. Cancel Visit");
            System.out.println("8. View Services by Specialization");
            System.out.println("0. Exit");

            choice = InputHelper.readInt("Enter choice: "); 

            switch (choice) {
                case 1 -> createAppointment();
                case 2 -> registerWalkIn();
                case 3 -> confirmDeposit();
                case 4 -> viewAllVisits();
                case 5 -> viewVisitsByDate();
                case 6 -> showInvoice();
                case 7 -> cancelVisit();
                case 8 -> viewServicesBySpecialization();
                case 0 -> System.out.println("Returning to main menu...");
            }

        } while (choice != 0);
    }

    private void createAppointment() {
        System.out.println("\n--- Create Appointment ---");
        String patientNRIC = InputHelper.readString("Patient NRIC: ");
        String serviceID = InputHelper.readString("Service ID: ");
        
        LocalDate date = InputHelper.readDate("Appointment date: ");
        LocalDateTime slot = visitController.selectSlot(date);
        
        if (slot == null) {
            return;     // Error already printed by selectSlot
        }
        
        double deposit = InputHelper.readDouble("Deposit Amount: ");

        var result = visitController.createAppointment(patientNRIC, serviceID, slot, deposit);

        if (result != null) {
            System.out.println(" Appointment created! Visit ID: " + result.getVisitID());
        } else {
            System.out.println(" Appointment creation failed.");
        }
    }

    private void registerWalkIn() {
        System.out.println("\n--- Register Walk-in ---");
        String patientNRIC = InputHelper.readString("Patient NRIC: ");
        String serviceID = InputHelper.readString("Service ID: ");
        String note = InputHelper.readString("Triage Note: ");

        var result = visitController.registerWalkIn(patientNRIC, serviceID, note);

        if (result != null) {
            System.out.println(" Walk-in registered! Visit ID: " + result.getVisitID());
        } else {
            System.out.println(" Walk-in registration failed.");
        }
    }

    private void confirmDeposit() {
        System.out.println("\n--- Confirm Deposit ---");
        String visitID = InputHelper.readString("Visit ID: ");

       Visit v = visitController.findVisitByID(visitID);
        if (v == null || !(v instanceof AppointmentVisit)) {
            System.out.println(" Invalid appointment.");
            return;
        }
        
        double requiredDeposit = ((AppointmentVisit) v).getDepositAmount();
        System.out.println(" Required deposit: RM " + requiredDeposit);

        double paidAmount = InputHelper.readDouble("Enter paid amount: RM ");

        boolean ok = visitController.confirmDeposit(visitID, paidAmount);

        if (ok) {
            System.out.println(" Deposit confirmed. Appointment is now CONFIRMED.");
        } else {
            System.out.println(" Deposit confirmation failed.");
        }
    }
    
    private void viewAllVisits() {
        System.out.println("\n------------------------------------");
        System.out.println("              ALL VISITS                    ");
        System.out.println("------------------------------------");
        
        Iterator<Visit> it = visitController.getAllVisits();
        
        if (!it.hasNext()) {
            System.out.println("No visits found.");
            return;
        }
        
        System.out.println("ID         | Patient              | Type         | Service              | Doctor            | Date       | Status");
        System.out.println("-----------|----------------------|--------------|----------------------|-------------------|------------|------------");
        
        while (it.hasNext()) {
            Visit v = it.next();
            String type = (v instanceof AppointmentVisit) ? "Appointment" : "Walk-in";
            System.out.printf("%-10s | %-20s | %-11s  | %-20s | %-17s | %-10s | %s%n",
                v.getVisitID(),
                OutputHelper.truncate(v.getPatient().getName(), 20),
                type,
                OutputHelper.truncate(v.getService().getServiceName(), 20),
                v.getDoctor() != null ? OutputHelper.truncate(v.getDoctor().getName(), 17) : "Not assigned",
                v.getTime().toLocalDate(),
                v.getStatus());
        }
    }
    

    private void viewVisitsByDate() {
        System.out.println("\n--- VIEW VISITS BY DATE ---");
        LocalDate date = InputHelper.readDate("Enter date (yyyy-MM-dd): ");

        Iterator<Visit> it = visitController.getVisitByDate(date);

        if (!it.hasNext()) {
            System.out.println("No visits found for " + date);
            return;
        }

        System.out.println("ID         | Patient              | Type        | Service              | Doctor            | Time                         | Status");
        System.out.println("-----------|----------------------|-------------|----------------------|-------------------|------------------------------|------------");

        while (it.hasNext()) {
            Visit v = it.next();
            String type = (v instanceof AppointmentVisit) ? "Appointment" : "Walk-in";
            System.out.printf("%-11s| %-21s| %-11s | %-21s| %-18s|%-30s| %s%n",
                v.getVisitID(),
                OutputHelper.truncate(v.getPatient().getName(), 20),
                type,
                OutputHelper.truncate(v.getService().getServiceName(), 20),
                v.getDoctor() != null ? OutputHelper.truncate(v.getDoctor().getName(), 17) : "Not assigned",
                v.getTime(),
                v.getStatus());
        }
    }
    
    
    
    private void showInvoice() {
        System.out.println("\n--- SHOW INVOICE ---");
        String visitID = InputHelper.readString("Enter Visit ID: ");

        Visit v = visitController.findVisitByID(visitID);
        if (v == null) {
            System.out.println(" Visit not found.");
            return;
        }

        Invoice inv = v.getInvoice();
        if (inv == null) {
            System.out.println(" No invoice found for this visit.");
            return;
        }

        System.out.println("\n=======================================");
        System.out.println("                INVOICE                      ");
        System.out.println("=======================================");
        System.out.println("Invoice ID    : " + inv.getInvoiceID());
        System.out.println("Visit ID      : " + v.getVisitID());
        System.out.println("Patient       : " + v.getPatient().getName());
        System.out.println("Service       : " + v.getService().getServiceName());
        // Show prescription note if exists
        if (v.getPrescription() != null) {
            System.out.println("Prescription Note: " + v.getPrescription().getNote());
        }
        System.out.println("Date          : " + v.getTime().toLocalDate());
        System.out.println("--------------------------------------------");
        System.out.printf("Service Charge : RM %.2f%n", inv.getServiceCharge());
        System.out.println("--------------------------------------------");

        // Show medication breakdown if exists
        if (inv.getItems() != null && inv.getItems().hasNext()) {
            System.out.println("\nMedication Breakdown:");
            Iterator<Invoice.InvoiceItem> it = inv.getItems();
            while (it.hasNext()) {
                Invoice.InvoiceItem item = it.next();
                System.out.printf("  %s x%d = RM %.2f%n", 
                    item.getName(), 
                    item.getQuantity(), 
                    item.getPrice());
            }
        }
        System.out.println("--------------------------------------------");
        System.out.printf("Medication Charge: RM %.2f%n", inv.getMedicationCharge());
        System.out.printf("Discount      : RM %.2f%n", inv.getDiscountAmount());
        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL         : RM %.2f%n", inv.getTotalAmount());
        System.out.println("============================================");
    }
    
    
    private void cancelVisit() {
        System.out.println("\n--- Cancel Visit ---");
        String visitID = InputHelper.readString("Enter Visit ID: ");

        boolean success = visitController.cancelVisit(visitID);

        if (success) {
            System.out.println(" Visit cancelled successfully.");
        } else {
            System.out.println(" Cancellation failed. Visit may be completed or invalid.");
        }
    }
    
    
    private void viewServicesBySpecialization() {
    System.out.println("\n--- SERVICES BY SPECIALIZATION ---");
    
    Specialization spec = selectSpecialization();
    Iterator<Service> it = serviceController.getServicesBySpecialization(spec);
    
    if (!it.hasNext()) {
        System.out.println("No services found for " + spec);
        return;
    }
    
    System.out.println("\nID     | Service Name                   | Price     | Requires Equipment");
    System.out.println("-------|--------------------------------|-----------|--------------------");
    while (it.hasNext()) {
        Service s = it.next();
        System.out.printf("%-6s | %-30s | RM %6.2f | %s%n",
            s.getServiceID(),
            s.getServiceName(),
            s.getBasePrice(),
            s.requiresEquipment() ? "Yes" : "No");
    }
}

    private Specialization selectSpecialization() {
        System.out.println("\n--- Select Specialization ---");
        Specialization[] specs = Specialization.values();
        for (int i = 0; i < specs.length; i++) {
            System.out.println((i + 1) + ". " + specs[i]);
        }
        int choice = InputHelper.readInt("Choose (1-" + specs.length + "): ");
        if (choice >= 1 && choice <= specs.length) {
            return specs[choice - 1];
        }
        return Specialization.GENERAL;
    }
    
}
    
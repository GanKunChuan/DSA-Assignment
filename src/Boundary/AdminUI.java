/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

/**
 *
 * @author Soh Lian Ze, Kudzaishe Blessed Masunda
 */


import ADT.BinarySearchTreeInterface;
import Control.BillingStockController;
import Control.ServiceController;
import Control.UserController;
import Control.VisitController;
import Entity.AppointmentVisit;
import Entity.Doctor;
import Entity.Invoice;
import Entity.Medication;
import Entity.Service;
import Entity.Specialization;
import Entity.Visit;
import Helper.InputHelper;
import java.time.LocalDate;
import java.util.Iterator;

public class AdminUI {
    private BillingStockController billingController;
    private VisitController visitController;
    private UserController userController;
    private ServiceController serviceController;

    public AdminUI(BillingStockController bc, VisitController vc, ServiceController sc, UserController uc) {
        this.billingController = bc;
        this.visitController = vc;
        this.serviceController = sc;
        this.userController = uc;
    }

    
    public void menu() {
        int choice;
        do {
            System.out.println("\n---------------------------------");
            System.out.println("          ADMIN MENU                ");
            System.out.println("---------------------------------");
            System.out.println(" === INVENTORY MANAGEMENT === ");
            System.out.println(" 1. Add New Medication              ");
            System.out.println(" 2. Remove Medication               ");
            System.out.println(" 3. Update Selling Price            ");
            System.out.println(" 4. Update Cost Price               ");
            System.out.println(" 5. Add Stock                       ");
            System.out.println(" 6. Find Medication by Name         ");
            System.out.println(" 7. Low Stock Alert                 ");
            System.out.println(" 8. Show All Medications            ");
            System.out.println("                                    ");
            System.out.println(" === REPORTS ===                    ");
            System.out.println(" 9. Revenue by Date          ");
            System.out.println(" 10. Revenue by Date Range    ");
            System.out.println(" 11. Doctor Performance Ranking    ");
            System.out.println(" 12. View All Invoices    ");
            System.out.println(" === SERVICE MANAGEMENT === ");
            System.out.println("13. Add New Service");
            System.out.println("14. View All Services");
            System.out.println("                                    ");
            System.out.println(" 0. Exit                            ");
            System.out.println("---------------------------------");
            
            choice = InputHelper.readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addNewMedication();
                case 2 -> removeMedication();
                case 3 -> updateSellingPrice();
                case 4 -> updateCostPrice();
                case 5 -> addStock();
                case 6 -> findMedicationByName();
                case 7 -> showLowStock();
                case 8 -> showAllMedications();
                case 9 -> revenueByDate();
                case 10 -> revenueByDateRange();
                case 11 -> doctorPerformance();
                case 12 -> viewAllInvoices();
                case 13 -> addNewService();
                case 14 -> viewAllServices();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);
        
    }
    
    
    private void addNewMedication() {
        System.out.println("\n--- Add New Medication ---");
        String id = InputHelper.readString("Medication ID: ");
        String name = InputHelper.readString("Name: ");
        double costPrice = InputHelper.readDouble("Cost Price: RM ");
        double sellingPrice = InputHelper.readDouble("Selling Price: RM ");
        int stock = InputHelper.readInt("Initial Stock: ");
        int threshold = InputHelper.readInt("Low Stock Threshold: ");
        
        Medication m = new Medication(id, name, stock, costPrice, sellingPrice,  threshold);
        boolean success = billingController.addNewMedication(m);
        
        if (success) {
            System.out.println(" Medication added successfully!");
        } else {
            System.out.println(" Failed. ID may already exist.");
        }
    }
    
    
    private void removeMedication() {
        System.out.println("\n--- Remove Medication ---");
        String id = InputHelper.readString("Medication ID: ");
        boolean success = billingController.removeMedication(id);
        
        if (success) {
            System.out.println(" Medication removed.");
        } else {
            System.out.println(" Medication not found.");
        }
    }
    
    
    private void updateSellingPrice() {
        System.out.println("\n--- Update Selling Price ---");
        String id = InputHelper.readString("Medication ID: ");
        double newPrice = InputHelper.readDouble("New Selling Price: RM ");
        boolean success = billingController.updateMedicationSellingPrice(id, newPrice);
        
        if (success) {
            System.out.println(" Selling price updated.");
        } else {
            System.out.println(" Medication not found.");
        }
    }
    
    
    private void updateCostPrice() {
        System.out.println("\n--- Update Cost Price ---");
        String id = InputHelper.readString("Medication ID: ");
        double newPrice = InputHelper.readDouble("New Cost Price: RM ");
        boolean success = billingController.updateMedicationCostPrice(id, newPrice);
        
        if (success) {
            System.out.println(" Cost price updated.");
        } else {
            System.out.println(" Medication not found.");
        }
    }
    
    
    private void addStock() {
        System.out.println("\n--- Add Stock ---");
        String id = InputHelper.readString("Medication ID: ");
        
        Medication m = billingController.findMedicationByID(id);
        if (m == null) {
            System.out.println(" Medication not found.");
            return;
        }

        int oldStock = m.getQuantity();  // Store old value BEFORE restock
        System.out.println("\nCurrent details:");
        System.out.println("  Name: " + m.getName());
        System.out.println("  Current Stock: " + m.getQuantity());
        System.out.println("  Cost Price: RM " + m.getCostPrice());
        System.out.println("  Selling Price: RM " + m.getSellingPrice());
        
        int amount = InputHelper.readInt("Amount to add: ");
        double totalCost = amount * m.getCostPrice();
        
        System.out.println("\n--- Restock Calculation ---");
        System.out.printf("Quantity     : %d%n", amount);
        System.out.printf("Unit Cost    : RM %.2f%n", m.getCostPrice());
        System.out.printf("Total Cost   : RM %.2f%n", totalCost);
        System.out.println("----------------------------");
        
        boolean confirm = InputHelper.readBoolean("Confirm restock?");
        if (!confirm) {
            System.out.println("Restock cancelled.");
            return;
        }
        boolean success = billingController.addStock(id, amount);
        
        if (success) {
            Medication updated = billingController.findMedicationByID(id);
            System.out.println("\n Stock added successfully!");
            System.out.println("  Old Stock: "  + oldStock);      // Use stored old value
            System.out.println("  New Stock: " + updated.getQuantity());
            System.out.printf("  Cost Incurred: RM %.2f%n", totalCost);
        } else {
            System.out.println(" Failed to add stock.");
        }
    }
    
    
    private void findMedicationByName() {
        System.out.println("\n--- Find Medication by Name ---");
        String name = InputHelper.readString("Medication Name: ");
        Medication m = billingController.findMedicationByName(name);
        
        if (m != null) {
            System.out.println("\n Medication found:");
            System.out.println("  ID: " + m.getMedID());
            System.out.println("  Name: " + m.getName());
            System.out.println("  Stock: " + m.getQuantity());
            System.out.println("  Threshold: " + m.getLowStockThreshold());
            System.out.println("  Selling Price: RM " + m.getSellingPrice());
            System.out.println("  Cost Price: RM " + m.getCostPrice());
        } else {
            System.out.println(" Medication not found.");
        }
    }
    
     private void showLowStock() {
        System.out.println("\n--- LOW STOCK ALERT ---");
        Iterator<Medication> it = billingController.getLowStockMedications();
        
        if (!it.hasNext()) {
            System.out.println(" All medications adequately stocked.");
            return;
        }
        
        System.out.println("\nID     | Name                      | Stock  | Threshold | Cost Price | Selling Price");
        System.out.println("-------|---------------------------|--------|-----------|------------|--------------");
        while (it.hasNext()) {
            Medication m = it.next();
            System.out.printf("%-6s | %-25s | %6d | %9d | RM %6.2f  | RM %7.2f%n", 
                m.getMedID(), 
                m.getName(), 
                m.getQuantity(), 
                m.getLowStockThreshold(),
                m.getCostPrice(),
                m.getSellingPrice());
        }
    }

     
    private void showAllMedications() {
        System.out.println("\n--- ALL MEDICATIONS ---");
        Iterator<Medication> it = billingController.getAllMedications();
        
        if (!it.hasNext()) {
            System.out.println("No medications found.");
            return;
        }
        
        System.out.println("\nID     | Name                      | Stock  | Threshold | Cost Price | Selling Price");
        System.out.println("-------|---------------------------|--------|-----------|------------|--------------");
        while (it.hasNext()) {
            Medication m = it.next();
            System.out.printf("%-6s | %-25s | %6d | %9d | RM %6.2f  | RM %7.2f%n", 
                m.getMedID(), 
                m.getName(), 
                m.getQuantity(), 
                m.getLowStockThreshold(),
                m.getCostPrice(),
                m.getSellingPrice());
        }
    }
    
    
    private void revenueByDate() {
        System.out.println("\n--- REVENUE BY DATE ---");
        LocalDate date = InputHelper.readDate("Enter date (yyyy-MM-dd): ");

        double total = billingController.getRevenueByDate(date, visitController);
        System.out.printf("\nRevenue for %s: RM %.2f%n", date, total);
    }
    
    
    private void revenueByDateRange() {
        System.out.println("\n--- REVENUE BY DATE RANGE ---");
        LocalDate start = InputHelper.readDate("Start date (yyyy-MM-dd): ");
        LocalDate end = InputHelper.readDate("End date (yyyy-MM-dd): ");
        
        if (start.isAfter(end)) {
            System.out.println(" Start date cannot be after end date.");
            return;
        }
        
        double total = billingController.getRevenueByRange(start, end, visitController);
        System.out.printf("\nRevenue from %s to %s: RM %.2f%n", start, end, total);
    }
    
    
   
    private void doctorPerformance() {
        System.out.println("\n--- DOCTOR PERFORMANCE ---");

        Iterator<String> rankIt = visitController.getDoctorRanking(visitController.getAllVisits());

        if (!rankIt.hasNext()) {
            System.out.println("No visit data available.");
            return;
        }

        // Get visit counts
        BinarySearchTreeInterface<String, Integer> performance = visitController.getDoctorPerformance(visitController.getAllVisits());
        System.out.println("\nRank | Doctor ID | Doctor Name           | Visits");
        System.out.println("-----|-----------|-----------------------|-------");
        int rank = 1;
        while (rankIt.hasNext()) {
            String id = rankIt.next();
            Doctor d = userController.findDoctorByID(id);
            int count = performance.get(id);
            System.out.printf("%-4d | %-9s | %-21s | %d%n", rank++, id, 
                d.getName().length() > 21 ? d.getName().substring(0, 18) + "..." : d.getName(), 
                count);
        }
    }
    
    
    private void viewAllInvoices() {
        System.out.println("\n--- ALL INVOICES ---");
        Iterator<Invoice> invIt = billingController.getAllInvoices();

        if (!invIt.hasNext()) {
            System.out.println("No invoices found.");
            return;
        }

        System.out.println("\nInvoice ID   | Visit ID    | Patient ID | Patient Name          | Type       | Date");
        System.out.println("-------------|-------------|------------|-----------------------|------------|------------");
        
        while (invIt.hasNext()) {
            Invoice inv = invIt.next();
            // Find visit by scanning all visits
            Iterator<Visit> visitIt = visitController.getAllVisits();
            Visit found = null;
            while (visitIt.hasNext()) {
                Visit v = visitIt.next();
                if (v.getInvoice() == inv) {
                    found = v;
                    break;
                }
            }
            if (found != null) {
                String type = (found instanceof AppointmentVisit) ? "Appointment" : "Walk-in";
                String patientName = found.getPatient().getName();
                String patientId = found.getPatient().getPatientID();
                System.out.printf("%-11s | %-11s | %-10s | %-21s | %-10s | %s%n",
                    inv.getInvoiceID(),
                    found.getVisitID(),
                    patientId,
                    patientName.length() > 21 ? patientName.substring(0, 18) + "..." : patientName,
                    type,
                    found.getTime().toLocalDate());
            }
        }
    }
    
    
    
    private void addNewService() {
        System.out.println("\n--- Add New Service ---");
        String id = InputHelper.readString("Service ID: ");
        String name = InputHelper.readString("Service Name: ");

        System.out.println("Requires Equipment?");
        boolean requiresEquipment = InputHelper.readBoolean("(y/n): ");

        double price = InputHelper.readDouble("Base Price: RM ");

        System.out.println("\n--- Select Specialization ---");
        Specialization spec = selectSpecialization();

        boolean success = serviceController.addNewService(id, name, requiresEquipment, price, spec);

        if (success) {
            System.out.println(" Service added successfully!");
        } else {
            System.out.println(" Failed. ID may already exist.");
        }
    }

    private void viewAllServices() {
        System.out.println("\n--- ALL SERVICES ---");
        Iterator<Service> it = serviceController.getAllServices();

        if (!it.hasNext()) {
            System.out.println("No services found.");
            return;
        }

        System.out.println("\nID     | Name                           | Equipment | Price      | Specialization");
        System.out.println("-------|--------------------------------|-----------|------------|----------------");
        while (it.hasNext()) {
            Service s = it.next();
            System.out.printf("%-6s | %-30s | %-9s | RM %6.2f | %s%n",
                s.getServiceID(),
                s.getServiceName().length() > 30 ? s.getServiceName().substring(0, 27) + "..." : s.getServiceName(),
                s.requiresEquipment() ? "Yes" : "No",
                s.getBasePrice(),
                s.getSpecialization());
        }
    }

    private Specialization selectSpecialization() {
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

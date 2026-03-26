/*
 * @author Shaft (Module 3 - Prescription, Inventory & Billing)
 */
package Boundary;

import Control.BillingStockingController;
import Entity.Medication;
import Entity.MedicationBatch;
import Entity.Prescription;
import Entity.Invoice;
import Entity.Visit;
import ADT.ListInterface;

import java.time.LocalDate;
import java.util.Scanner;

public class PrescriptionBillingUI {

    private BillingStockingController controller;
    private Scanner scanner;

    public PrescriptionBillingUI(BillingStockingController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    // =====================================================
    // MAIN MENU
    // =====================================================
    public void showMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n===== MODULE 3: PRESCRIPTION, INVENTORY & BILLING =====");
            System.out.println(" [1]  Add New Medication");
            System.out.println(" [2]  Restock Medication");
            System.out.println(" [3]  Remove Medication");
            System.out.println(" [4]  Search Medication by ID");
            System.out.println(" [5]  Search Medication by Name");
            System.out.println(" [6]  List All Medications (Sorted A-Z)");
            System.out.println(" [7]  Receive Medication Batch");
            System.out.println("-------------------------------------------------------");
            System.out.println(" [8]  Create Prescription for Visit");
            System.out.println(" [9]  Add Medication to Prescription");
            System.out.println("[10]  View Prescription");
            System.out.println("[11]  Apply Prescription to Stock");
            System.out.println("-------------------------------------------------------");
            System.out.println("[12]  Generate Invoice");
            System.out.println("[13]  View Invoice");
            System.out.println("[14]  Calculate Total (Preview)");
            System.out.println("-------------------------------------------------------");
            System.out.println("[15]  Generate Treatment Summary Report");
            System.out.println(" [0]  Back");
            System.out.println("=======================================================");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1  -> addMedicationUI();
                case 2  -> restockMedicationUI();
                case 3  -> removeMedicationUI();
                case 4  -> searchByIDUI();
                case 5  -> searchByNameUI();
                case 6  -> listAllSortedUI();
                case 7  -> receiveBatchUI();
                case 8  -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 9  -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 10 -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 11 -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 12 -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 13 -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 14 -> System.out.println("[INFO] Pass a Visit object from main UI.");
                case 15 -> controller.generateTreatmentSummary();
                case 0  -> System.out.println("Returning...");
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // =====================================================
    // INVENTORY UI
    // =====================================================

    private void addMedicationUI() {
        System.out.println("\n--- ADD NEW MEDICATION ---");
        System.out.print("Medication ID   : "); String medID = scanner.nextLine().trim();
        System.out.print("Name            : "); String name = scanner.nextLine().trim();
        System.out.print("Initial Stock   : "); int qty = readInt();
        System.out.print("Low Stock Alert : "); int threshold = readInt();
        System.out.print("Cost Price (RM) : "); double cost = readDouble();
        System.out.print("Sell Price (RM) : "); double sell = readDouble();

        Medication med = new Medication(medID, name, qty, threshold, cost, sell);
        boolean success = controller.addMedication(med);
        System.out.println(success ? "Medication added successfully." : "Failed (duplicate ID?).");
    }

    private void restockMedicationUI() {
        System.out.println("\n--- RESTOCK MEDICATION ---");
        System.out.print("Medication ID  : "); String medID = scanner.nextLine().trim();
        System.out.print("Quantity to add: "); int qty = readInt();
        controller.addMedicationStock(medID, qty);
    }

    private void removeMedicationUI() {
        System.out.println("\n--- REMOVE MEDICATION ---");
        System.out.print("Medication ID : "); String medID = scanner.nextLine().trim();
        boolean success = controller.removeMedication(medID);
        System.out.println(success ? "Removed successfully." : "Medication not found.");
    }

    private void searchByIDUI() {
        System.out.println("\n--- SEARCH BY ID ---");
        System.out.print("Medication ID : "); String medID = scanner.nextLine().trim();
        Medication med = controller.findMedicationByID(medID);
        if (med != null) {
            System.out.println("  " + med);
        } else {
            System.out.println("Not found.");
        }
    }

    private void searchByNameUI() {
        System.out.println("\n--- SEARCH BY NAME ---");
        System.out.print("Medication Name : "); String name = scanner.nextLine().trim();
        ListInterface<Medication> results = controller.findMedicationByName(name);
        if (results.isEmpty()) {
            System.out.println("No medication found with name: " + name);
        } else {
            var it = results.getIterator();
            while (it.hasNext()) System.out.println("  " + it.next());
        }
    }

    private void listAllSortedUI() {
        System.out.println("\n--- ALL MEDICATIONS (SORTED A-Z) ---");
        ListInterface<Medication> list = controller.getAllMedicationsSorted();

        if (list.isEmpty()) {
            System.out.println("No medications in system.");
            return;
        }

        System.out.printf("%-10s %-25s %-8s %-10s %-10s%n",
                "ID", "Name", "Stock", "Cost(RM)", "Sell(RM)");
        System.out.println("-".repeat(63));

        var it = list.getIterator();
        while (it.hasNext()) {
            Medication med = it.next();
            System.out.printf("%-10s %-25s %-8d %-10.2f %-10.2f%s%n",
                    med.getMedID(), med.getName(), med.getQuantity(),
                    med.getCostPrice(), med.getSellingPrice(),
                    med.isLowStock() ? " [LOW STOCK]" : "");
        }
    }

    private void receiveBatchUI() {
        System.out.println("\n--- RECEIVE MEDICATION BATCH ---");
        System.out.print("Batch ID            : "); String batchID = scanner.nextLine().trim();
        System.out.print("Medication ID       : "); String medID = scanner.nextLine().trim();

        Medication med = controller.findMedicationByID(medID);
        if (med == null) {
            System.out.println("Medication not found.");
            return;
        }

        System.out.print("Quantity Added      : "); int qty = readInt();
        System.out.print("Supplier Name       : "); String supplier = scanner.nextLine().trim();
        System.out.print("Expiry (YYYY-MM-DD) : "); String dateStr = scanner.nextLine().trim();

        LocalDate expiry;
        try {
            expiry = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        MedicationBatch batch = new MedicationBatch(
                batchID, med, qty, supplier, LocalDate.now(), expiry);
        controller.receiveMedicationBatch(batch);
    }

    // =====================================================
    // VISIT-BASED METHODS (called from main UI with Visit object)
    // =====================================================

    public void createPrescriptionUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        System.out.print("Prescription ID : "); String presID = scanner.nextLine().trim();
        System.out.print("Notes           : "); String note = scanner.nextLine().trim();
        boolean success = controller.createPrescription(visit, presID, note);
        System.out.println(success ? "Prescription created." : "Failed.");
    }

    public void addMedToPrescriptionUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        System.out.print("Medication ID : "); String medID = scanner.nextLine().trim();
        System.out.print("Quantity      : "); int qty = readInt();
        boolean success = controller.addMedicationToPrescription(visit, medID, qty);
        System.out.println(success ? "Added to prescription." : "Failed.");
    }

    public void viewPrescriptionUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        Prescription p = controller.getPrescription(visit);
        if (p == null) { System.out.println("No prescription for this visit."); return; }

        System.out.println("\n--- PRESCRIPTION: " + p.getPrescriptionID() + " ---");
        System.out.println("Note: " + p.getNote());
        System.out.printf("%-20s %-8s %-10s%n", "Medication", "Qty", "Subtotal");
        System.out.println("-".repeat(40));

        var it = p.getItemsIterator();
        while (it.hasNext()) {
            Prescription.PrescriptionItem item = it.next();
            double subtotal = item.getQuantity() * item.getMedication().getSellingPrice();
            System.out.printf("%-20s %-8d RM%.2f%n",
                    item.getMedication().getName(), item.getQuantity(), subtotal);
        }
        System.out.printf("TOTAL: RM%.2f%n", p.calculateTotalCost());
    }

    public void applyToStockUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        boolean success = controller.applyPrescriptionToStock(visit);
        System.out.println(success ? "Stock updated." : "Failed.");
    }

    public void generateInvoiceUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        System.out.print("Invoice ID : "); String invoiceID = scanner.nextLine().trim();
        Invoice inv = controller.generateInvoice(visit, invoiceID);
        if (inv != null) displayInvoice(inv);
    }

    public void viewInvoiceUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        Invoice inv = visit.getInvoice();
        if (inv == null) { System.out.println("No invoice yet."); return; }
        displayInvoice(inv);
    }

    public void calculateTotalUI(Visit visit) {
        if (visit == null) { System.out.println("[INFO] No visit provided."); return; }
        System.out.printf("Estimated Total: RM%.2f%n", controller.calculateTotal(visit));
    }

    // =====================================================
    // DISPLAY HELPER
    // =====================================================

    private void displayInvoice(Invoice inv) {
        System.out.println("\n========== INVOICE ==========");
        System.out.println("Invoice ID     : " + inv.getInvoiceID());
        System.out.printf( "Service Charge : RM%.2f%n", inv.getServiceCharge());
        System.out.printf( "Medication     : RM%.2f%n", inv.getMedicationCharge());
        System.out.printf( "Discount       : RM%.2f%n", inv.getDiscountAmount());
        System.out.println("-----------------------------");
        System.out.printf( "TOTAL PAYABLE  : RM%.2f%n", inv.getTotalAmount());
        System.out.println("=============================");
    }

    // =====================================================
    // INPUT HELPERS
    // =====================================================

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, defaulting to 0.");
            return 0;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, defaulting to 0.0.");
            return 0.0;
        }
    }
}
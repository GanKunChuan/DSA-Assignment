/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import Control.BillingStockController;
import Control.VisitController;
import Entity.*;
import Helper.InputHelper;
import Helper.OutputHelper;
import java.time.LocalDate;
import java.util.Iterator;

/**
 *
 * @author Soh Lian Ze
 */
public class DoctorUI {
    private VisitController visitController;
    private BillingStockController billingController;

    public DoctorUI(VisitController vc, BillingStockController bc) {
        this.visitController = vc;
        this.billingController = bc;
    }

    public void menu() {

        int choice;
        do {
            System.out.println("\n=== Doctor Menu ===");
            System.out.println("1. View Walk-in Queue");
            System.out.println("2. View Today's Appointments");
            System.out.println("3. Next from Walk-in Queue (by priority)");
            System.out.println("4. Treat from Today's Appointments");
            System.out.println("0. Exit");
            System.out.println("==================");

            choice = InputHelper.readInt("Enter choice: ");

            switch (choice) {
                case 1 -> viewQueue();
                case 2 -> viewTodayAppointments();
                case 3 -> treatNextWalkIn();
                case 4 -> treatSelectedAppointment();
                case 0 -> System.out.println("Returning to main menu...");
            }

        } while (choice != 0);
    }

    private void viewQueue() {
        System.out.println("\n--- Walk-in Queue (By Priority) ---");
        System.out.println("(Higher score = Higher priority)");
        System.out.println("-----------------------------------");
        Iterator<WalkInVisit> it = visitController.viewWalkInQueue();

        if (!it.hasNext()) {
            System.out.println("Queue is empty.");
            return;
        }
        System.out.println("Pos | Patient Name           | Score | Service              | Triage Note                          | Visit ID");
        System.out.println("----|-----------------------|-------|----------------------|--------------------------------------|---------");
        int pos = 1;
        while (it.hasNext()) {
            WalkInVisit w = it.next();
            System.out.printf("%-3d | %-21s | %5d | %-20s | %-36s | %s%n",  
                pos++, 
                OutputHelper.truncate(w.getPatient().getName(), 21), 
                w.getTriageScore(),
                OutputHelper.truncate(w.getService().getServiceName(), 20),
                OutputHelper.truncate(w.getTriageNote(), 36),
                w.getVisitID());
        }
    }
    
   
    
    private void viewTodayAppointments() {
        LocalDate today = VisitController.getSystemDate();  // ← Use systemDate
        System.out.println("\n--- Today's Appointments ---");
        System.out.println("(Only CONFIRMED appointments shown)");
        System.out.println("----------------------------------------");
        Iterator<AppointmentVisit> it = visitController.getConfirmedAppointments(today);  //dont use localdate.now

        if (!it.hasNext()) {
            System.out.println("No confirmed appointments for today.");
            return;
        }
        
        System.out.println("Visit ID     | Patient Name          | Time     | Service              | Doctor            | Status");
        System.out.println("-------------|-----------------------|----------|----------------------|-------------------|----------");

        while (it.hasNext()) {
            AppointmentVisit a = it.next();
            System.out.printf("%-12s | %-21s | %-8s | %-20s | %-17s | %s%n", 
                a.getVisitID(), 
                OutputHelper.truncate(a.getPatient().getName(), 21), 
                a.getSlot().toLocalTime(),
                OutputHelper.truncate(a.getService().getServiceName(), 20),
                a.getDoctor() != null ? OutputHelper.truncate(a.getDoctor().getName(), 17) : "Not assigned",
                a.getStatus());
        }
    }
    
    private void treatNextWalkIn() {
        System.out.println("\n--- TREAT NEXT WALK-IN ---");
        // Step 1: Get next walk-in from queue
        WalkInVisit walkIn = visitController.getNextWalkIn();
        
        if (walkIn == null) {
            System.out.println(" Walk-in queue is empty.");
            return;
        }
        
        System.out.println(" Next walk-in: " + walkIn.getPatient().getName());
        System.out.println("  Visit ID: " + walkIn.getVisitID());
        System.out.println("  Service: " + walkIn.getService());
        System.out.println("  Triage Score: " + walkIn.getTriageScore());
        System.out.println("  Note: " + walkIn.getTriageNote());
        
        // Ask for doctor ID (manual assignment)
        String doctorID = InputHelper.readString("Enter Doctor ID: ");

        // Start visit with doctor assignment
        boolean started = visitController.startVisit(walkIn.getVisitID(), doctorID);
        if (!started) {
            System.out.println(" Cannot start visit.");
            return;
        }
        visitController.removeWalkInFromQueue(walkIn);
        // Step 2: Treat the patient
        treatVisit(walkIn.getVisitID());
    }
    
    
    
    private void treatSelectedAppointment() {
        LocalDate today = VisitController.getSystemDate();  // ← Use systemDate
        System.out.println("\n--- TREAT APPOINTMENT (" + today + ") ---");
        
        // Step 1: get today's appointments in time order
        System.out.println("\n--- Today's Appointments (by time) ---");
        Iterator<AppointmentVisit> it = visitController.getTodayAppointments(today);  // changed to program own systemDate

        if (!it.hasNext()) {
            System.out.println(" No appointments for today.");
            return;
        }
        
        // Step 2: Display all and find earliest
        System.out.println("\n--- Today's Appointments (by time) ---");

        // First pass: display and find earliest CONFIRMED
        AppointmentVisit earliest = null;
        int index = 1;
        
        System.out.println("\nAvailable Appointments:");
        System.out.println("No. | Time     | Patient Name           | Service              | Status");
        System.out.println("----|----------|------------------------|----------------------|----------");
    
        // Create a copy of iterator for display (ADT only)
        Iterator<AppointmentVisit> displayIt = visitController.getTodayAppointments(today);
        
        while (displayIt.hasNext()) {
            AppointmentVisit a = displayIt.next();
            System.out.printf("%-3d | %-8s | %-22s | %-20s | %s%n", 
                index++, 
                a.getSlot().toLocalTime(),
                OutputHelper.truncate(a.getPatient().getName(), 22),
                OutputHelper.truncate(a.getService().getServiceName(), 20),
                a.getStatus());

            // Track earliest CONFIRMED appointment
            if (earliest == null && a.getStatus() == VisitStatus.CONFIRMED) {
                earliest = a;
            }
        }
        
        // Step 3: Show next in order hint
        if (earliest != null) {
            System.out.println("\n→ Next in order: " + earliest.getSlot().toLocalTime() 
                + " - " + earliest.getPatient().getName());
        }

        // Step 4: Select by number (using iterator)
        int choice = InputHelper.readInt("Select appointment number (or 0 for next in order): ");

        AppointmentVisit selected = null;

        if (choice == 0 && earliest != null) {
            selected = earliest;
            System.out.println(" Taking next in order: " + selected.getPatient().getName());
        } else if (choice > 0) {
            // Traverse to selected position
            Iterator<AppointmentVisit> selectIt = visitController.getTodayAppointments(today);
            int currentPos = 1;
            while (selectIt.hasNext()) {
                AppointmentVisit a = selectIt.next();
                if (currentPos == choice) {
                    selected = a;
                    break;
                }
                currentPos++;
            }

            if (selected == null) {
                System.out.println(" Invalid selection.");
                return;
            }

            // Warn if skipping
            if (earliest != null && selected != earliest) {
                System.out.println(" WARNING: Skipping earlier appointments.");
                boolean confirm = InputHelper.readBoolean("Continue anyway? (y/n): ");
                if (!confirm) {
                    System.out.println("Treatment cancelled.");
                    return;
                }
            }
        } else {
            System.out.println(" Invalid selection.");
            return;
        }

        // Step 5: Check status
        if (selected.getStatus() != VisitStatus.CONFIRMED) {
            System.out.println(" Appointment not confirmed. Deposit not paid?");
            System.out.println(" Appointment Status: " + selected.getStatus());
            return;
        }
        
        
        // Step 6: assign doctor and start Treat
        String doctorID = InputHelper.readString("Enter Doctor ID: ");
        boolean started = visitController.startVisit(selected.getVisitID(), doctorID);
        if (!started) {
            System.out.println(" Cannot start visit.");
            return;
        }
        
        System.out.println(" Treating: " + selected.getPatient().getName());

        
        treatVisit(selected.getVisitID());
    }
    
    
    private void treatVisit(String visitID) {
        
        Visit visit = visitController.findVisitByID(visitID);
        System.out.println(" Started treating: " + visit.getPatient().getName());
        
        // Show allergy alert
        String allergy = visit.getPatient().getAllergy();
        if (allergy != null && !allergy.equalsIgnoreCase("none")) {
            System.out.println(" ALLERGY ALERT: " + allergy);
        }
        
        
        String prescriptionNote = null;
        BinarySearchTreeInterface<String, String> medDetails = new AVLTree<>();
        int medCounter = 0;
        
        // Step 1: Create prescription
        boolean hasPrescription = InputHelper.readBoolean("Create prescription? (y/n): ");
        if (hasPrescription) {
            String note = InputHelper.readString("Prescription note: ");
            billingController.createPrescription(visitID, note);
            
            // Add medications
            System.out.println("\n--- Add Medications ---");
            while (true) {
                String medID = InputHelper.readString("Medication ID (or 'done'): ");
                if (medID.equalsIgnoreCase("done")) break;
                
                // Get medication to show name
                Medication med = billingController.findMedicationByID(medID);
                if (med == null) {
                    System.out.println(" Medication not found.");
                    continue;
                }
                System.out.println(" Medication: " + med.getName() + " (Stock: " + med.getQuantity() + ")");

                int qty = InputHelper.readInt("Quantity: ");
                if (qty > med.getQuantity()) {
                    System.out.println(" Insufficient stock. Available: " + med.getQuantity());
                    continue;
                }
                
                
                String dosage = InputHelper.readString("Dosage (e.g., 1 tablet 2x daily): ");
                
                medDetails.add(String.valueOf(++medCounter), med.getName() + " x" + qty + " (" + dosage + ")");
                boolean added = billingController.addMedicationToPrescription(visitID, medID, qty, dosage);
                if (added) {
                    System.out.println(" Medication added.");
                } else {
                    System.out.println(" Failed. Check stock or medication ID.");
                }
            }
        }
        
        // Step 2: Complete visit
        Visit completed = visitController.completeVisit(visitID);
        if (completed == null) {
            System.out.println(" Failed to complete visit.");
            return;
        }
        
        // Step 3: Generate invoice
        Invoice inv = billingController.processCompletedVisit(completed);
        
         if (inv != null) {
        System.out.println("\n=======================================");
        System.out.println("                INVOICE           ");
        System.out.println("=======================================");
        System.out.println("Visit ID      : " + completed.getVisitID());
        System.out.println("Patient       : " + completed.getPatient().getName());
        System.out.println("Service       : " + completed.getService().getServiceName());
        if (prescriptionNote != null) {
            System.out.println("Prescription Note: " + prescriptionNote);
        }
        System.out.println("--------------------------------------------");
        System.out.printf("Service Charge : RM %.2f%n", inv.getServiceCharge());
        
        // Show medication breakdown with dosage
        if (!medDetails.isEmpty()) {
            System.out.println("\nMedication Breakdown:");
            Iterator<String> valueIt = medDetails.inOrderIterator();
            while (valueIt.hasNext()) {
                System.out.println("  " + valueIt.next());
            }
        }
        
        System.out.println("--------------------------------------------");
        System.out.printf("Medication Charge: RM %.2f%n", inv.getMedicationCharge());
        System.out.printf("Discount        : RM %.2f%n", inv.getDiscountAmount());
        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL           : RM %.2f%n", inv.getTotalAmount());
        System.out.println("============================================");
    } else {
        System.out.println(" Billing failed.");
        return;
    }
        
        System.out.println(" Visit completed successfully!");
    
    }

}   
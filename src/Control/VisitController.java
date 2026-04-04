/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import Entity.*;
import Helper.InputHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

/**
 *
 * @author Soh Lian Ze
 */
public class VisitController {
    private static LocalDate systemDate = LocalDate.now();      //special time for altering for demonstrating appointment purpose
    
    
    private BinarySearchTreeInterface<String, Visit> visitsByID;
    private BinarySearchTreeInterface<VisitKey, AppointmentVisit> appointments;
    private BinarySearchTreeInterface<TriageKey, WalkInVisit> walkInQueue;
    
    private UserController userController; // to access doctors
    private ServiceController serviceController;   // to access predefined services (services are to be used across billing too)
    
    private int visitCounter = 0;
    
    
    public VisitController(UserController userController, ServiceController serviceController) {
        this.visitsByID = new AVLTree<>();
        this.appointments = new AVLTree<>();
        this.walkInQueue = new AVLTree<>();
        this.userController = userController;
        this.serviceController = serviceController;
    }
    
        // ========================= ID GENERATION =========================
    private String generateVisitID() {
        return String.format("V%04d", ++visitCounter);  // V0001, V0002, V0010
    }

    // ========================= WALK-IN =========================
    public WalkInVisit registerWalkIn(String NRIC,
                                      String serviceID,
                                      String triageNote) {

        Patient patient = userController.findPatientByNRIC(NRIC);
        if (patient == null) {
            System.out.println(" Patient not found with NRIC: " + NRIC);
            return null;
        }
        
        Service service = serviceController.findService(serviceID);
        if (service == null) {
            System.out.println(" Service not found with ID: " + serviceID);
            return null;
        }
        if (service.requiresEquipment()) {
            System.out.println(" Service requires appointment. Use create appointment instead.");
            return null;
        }
        
        String id = generateVisitID();
        LocalDateTime now = LocalDateTime.now();

        int score = calculateTriageScore(patient);

        WalkInVisit visit =
            new WalkInVisit(id, patient, service, now, score, triageNote);
        visit.setStatus(VisitStatus.REGISTERED);
        
        visitsByID.add(id, visit);
        walkInQueue.add(new TriageKey(visit), visit);
        
        System.out.println(" Walk-in registered! Visit ID: " + id);
        return visit;
    }

    // ========================= TRIAGE FOR WALK IN =========================
    private int calculateTriageScore(Patient patient) {
        int score = 0;

        if (patient.getAge() > 60) score += 2;
        if (patient.hasMedicalCard()) score += 1;


        return score;
    }

    // ========================= APPOINTMENT =========================
    public AppointmentVisit createAppointment(String NRIC,
                                          String serviceID,
                                          LocalDateTime slot, double depositAmount) {

        Patient patient = userController.findPatientByNRIC(NRIC);
        if (patient == null) {
            System.out.println(" Patient not found with NRIC: " + NRIC);
            return null;
        }
        
        Service service = serviceController.findService(serviceID);
        if (service == null) {
            System.out.println(" Service not found with ID: " + serviceID);
            return null;
        }
        if (!service.requiresEquipment()) {
            System.out.println(" Service does not require appointment. Use walk-in instead.");
            return null;
        }

        if (slot.isBefore(LocalDateTime.now())) {
            System.out.println(" Cannot book past time.");
            return null;
        }

        if (isSlotTaken(slot)) {            
            System.out.println(" Slot already taken.");
            return null;
        }

        String id = generateVisitID();
        LocalDateTime now = LocalDateTime.now();

        AppointmentVisit appt =
            new AppointmentVisit(id, patient, service, now, slot, depositAmount);
        appt.setStatus(VisitStatus.REGISTERED);
        
        visitsByID.add(id, appt);
        appointments.add(new VisitKey(slot, id), appt);
        
        System.out.println(" Appointment created! Visit ID: " + id);
        return appt;
    }

    // ========================= SLOT CHECK =========================
    public boolean isSlotTaken(LocalDateTime slot) {        // Modify slot check to use systemDate for comparison

        // Only check slots on or after systemDate
        if (slot.toLocalDate().isBefore(systemDate)) {
            return true; // Past slots are considered taken
        }
        
        Iterator<AppointmentVisit> it =
            appointments.getRange(new VisitKey(slot, ""),
                                  new VisitKey(slot, "ZZZ"));   //dummy high value string serves as upperbound

        return it.hasNext();
    }
        // predefined time slots
    public BinarySearchTreeInterface<Integer, LocalDateTime> getAvailableSlots(LocalDate date) {

        if (date.isBefore(systemDate)) {
            System.out.println("Cannot book past dates.");
            return new AVLTree<>();     // dont return null later nullpointerexception
        }
        
        BinarySearchTreeInterface<Integer, LocalDateTime> slots =
            new AVLTree<>();

        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(16, 0);

        LocalTime current = start;
        int index = 1;

        while (current.isBefore(end)) {

            LocalDateTime slot = date.atTime(current);

            if (!isSlotTaken(slot)) {
                slots.add(index++, slot);
            }

            current = current.plusMinutes(60);
        }

        return slots;
    }
     
    public LocalDateTime selectSlot(LocalDate date) {

        var slots = getAvailableSlots(date);

        if (slots.isEmpty()) {
            System.out.println("No available slots");
            return null;
        }

        System.out.println("\n--- Available Slots ---");

        var it = slots.inOrderIterator();
        int index = 1;

        while (it.hasNext()) {
            System.out.println(index++ + ". " + it.next());
        }

        int choice = InputHelper.readInt("Select slot number: ");

        return slots.get(choice);       // direct access via key
    }
    
    public Iterator<AppointmentVisit> getTodayAppointments(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59);

        return appointments.getRange(
            new VisitKey(start, ""),
            new VisitKey(end, "ZZZ")
        );
    }
    public Iterator<AppointmentVisit> getConfirmedAppointments(LocalDate date) {

        Iterator<AppointmentVisit> it = getTodayAppointments(date);

        return new Iterator<>() {

            private AppointmentVisit next = findNext();

            private AppointmentVisit findNext() {
                while (it.hasNext()) {
                    AppointmentVisit a = it.next();
                    if (a.getStatus() == VisitStatus.CONFIRMED) {
                        return a;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() { return next != null; }

            @Override
            public AppointmentVisit next() {
                AppointmentVisit current = next;
                next = findNext();
                return current;
            }
        };
    }


    // ========================= CONFIRM APPOINTMENT =========================
    public boolean confirmDeposit(String visitID, double paidAmount) {
        Visit v = visitsByID.get(visitID);
        
        if (v == null) {
            System.out.println(" Visit not found with ID: " + visitID);
            return false;
        }

        if (!(v instanceof AppointmentVisit)) {
            System.out.println(" Only appointments can confirm deposit.");
            return false;
        }
        
        if (v.getStatus() != VisitStatus.REGISTERED) {
            System.out.println(" Deposit already confirmed or visit already started.");
            return false;
        }

        AppointmentVisit appt = (AppointmentVisit) v;
        
        double requiredDeposit = appt.getDepositAmount();
    
        if (paidAmount < requiredDeposit) {
            System.out.println(" Insufficient deposit. Required: RM " + requiredDeposit + ", Available: RM " + paidAmount);
            return false;
        }

        double change = paidAmount - requiredDeposit;
        if (change > 0) {
            System.out.println(" Change: RM " + change);
        }
        
        appt.payDeposit();
        System.out.println(" Deposit confirmed. Appointment is now CONFIRMED.");
        return true;
    }
    
    // ========================= CANCEL VISIT =========================
    public boolean cancelVisit(String visitID) {

        Visit v = visitsByID.get(visitID);
        if (v == null) return false;

        // already completed, cannot cancel
        if (v.getStatus() == VisitStatus.COMPLETED) return false;

        // set status
        v.setStatus(VisitStatus.CANCELLED);

        // remove from structures
        if (v instanceof AppointmentVisit) {
            AppointmentVisit appt = (AppointmentVisit) v;

            appointments.remove(new VisitKey(appt.getSlot(), appt.getVisitID()));

        } else if (v instanceof WalkInVisit) {
            WalkInVisit walk = (WalkInVisit) v;

            walkInQueue.remove(new TriageKey(walk));
        }
        System.out.println("Cancelling VisitID:" + visitID);
        return true;
    }
    
    // ====== STAGES OF VISIT ========
    public boolean startVisit(String visitID, String doctorID) {
        Visit v = visitsByID.get(visitID);
        if (v == null) return false;
        
        // Assign doctor manually
        if (doctorID != null) {
            Doctor d = userController.findDoctorByID(doctorID);
            if (d == null) return false;
            
            // Add specialization check
            if (!v.getService().isCompatibleWith(d)) {
                System.out.println(" Doctor not qualified for this service.");
                return false;
            }
            v.setDoctor(d);
        }

        // Walk-in: REGISTERED, can start
        // Appointment: CONFIRMED, can start
        if (v instanceof WalkInVisit && v.getStatus() == VisitStatus.REGISTERED) {
            v.setStatus(VisitStatus.IN_PROGRESS);
            return true;
        }

        if (v instanceof AppointmentVisit && v.getStatus() == VisitStatus.CONFIRMED) {
            v.setStatus(VisitStatus.IN_PROGRESS);
            return true;
        }

        return false;  // Invalid state
    }
    public Visit completeVisit(String visitID) {
        Visit v = visitsByID.get(visitID);
        if (v == null) return null;
        
        // must be in progress
        if (v.getStatus() != VisitStatus.IN_PROGRESS) return null;

        v.setStatus(VisitStatus.COMPLETED); // sets COMPLETED
        return v;       // Return to caller for billing processing
    }
    

    // ========================= GET NEXT WALK-IN =========================
    public WalkInVisit getNextWalkIn() {    // peek only
        return walkInQueue.getMin();
    }
    public void removeWalkInFromQueue(WalkInVisit v) {  //remove
        walkInQueue.remove(new TriageKey(v));
    }

    // ========================= VIEW QUEUE =========================
    public Iterator<WalkInVisit> viewWalkInQueue() {
        return walkInQueue.inOrderIterator();
    }

    /* // ========================= VIEW APPOINTMENTS =========================
    public Iterator<AppointmentVisit> viewAppointments() {
        return appointments.inOrderIterator();
    } */
    

    // ========================= GET ALL VISITS =========================
    public Iterator<Visit> getAllVisits() {
        return visitsByID.inOrderIterator();
    }

    
    // ========================= GET VISIT BY DATE =========================
    public Iterator<Visit> getVisitByDate(LocalDate date) {

        Iterator<Visit> allVisits = visitsByID.inOrderIterator();

        return new Iterator<>() {
            private Visit next = findNext();

            private Visit findNext() {
                while (allVisits.hasNext()) {
                    Visit v = allVisits.next();
                    LocalDate visitDate;

                    if (v instanceof AppointmentVisit) {
                        visitDate = ((AppointmentVisit) v).getSlot().toLocalDate();
                    } else {
                        visitDate = v.getTime().toLocalDate();
                    }

                    if (visitDate.equals(date)) {
                        return v;
                    }
                }
                return null;
            }
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Visit next() {
                Visit current = next;
                next = findNext();
                return current;
            }
        };
    }

    // ========================= FIND VISIT =========================
    public Visit findVisitByID(String id) {
        return visitsByID.get(id);
    }
    
    
    // ===================== DOCTORS PERFORMANCE REPORT =======================
    public BinarySearchTreeInterface<String, Integer> getDoctorPerformance(Iterator<Visit> visits) {

        BinarySearchTreeInterface<String, Integer> stats = new AVLTree<>();

        while (visits.hasNext()) {
            Visit v = visits.next();

            if (v.getDoctor() == null) continue;

            String id = v.getDoctor().getDoctorID();

            Integer count = stats.get(id);
            if (count == null) count = 0;

            stats.add(id, count + 1);
        }

        return stats;
    }
    public Iterator<String> getDoctorRanking(Iterator<Visit> visits) {

        BinarySearchTreeInterface<String, Integer> counts = new AVLTree<>();
        BinarySearchTreeInterface<DoctorPerformanceKey, String> ranking = new AVLTree<>();

        while (visits.hasNext()) {
            Visit v = visits.next();

            if (v.getDoctor() == null) continue;

            String id = v.getDoctor().getDoctorID();

            // update count
            Integer c = counts.get(id);
            if (c == null) c = 0;
            c++;
            counts.add(id, c);

            // update ranking (remove old entry first if existed)
            if (c > 1) {
                ranking.remove(new DoctorPerformanceKey(c - 1, id));
            }

            ranking.add(new DoctorPerformanceKey(c, id), id);
        }

        return ranking.inOrderIterator(); // sorted by performance
    }
    
    
    
    //special time altering for demonstration purpose (appointments)
    public static void setSystemDate(LocalDate date) {
        systemDate = date;
    }
    public static LocalDate getSystemDate() {
        return systemDate;
    }
}


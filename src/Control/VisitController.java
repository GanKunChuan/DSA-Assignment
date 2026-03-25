/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author user
 */
import ADT.AVLTree;
import ADT.DoublyLinkedList;
import ADT.Hash;
import ADT.ListInterface;
import ADT.NodeReference;
import Entity.AppointmentVisit;
import Entity.Doctor;
import Entity.Patient;
import Entity.Service;
import Entity.Visit;
import Entity.WalkInVisit;
import java.time.LocalDate;
import java.time.LocalDateTime;



//DLL → stores visits
//AVL → organizes by time / find by name
//Hash → finds by ID

public class VisitController {

    private DoublyLinkedList<Visit> visitList;
    private DoublyLinkedList<Visit> walkInQueue;   // sorted by triage
    private DoublyLinkedList<Visit> appointmentList;
    
    private Hash<String, NodeReference> visitIndex;     // visitID → ref
    private AVLTree<String> nameIndex;                  // patient name → ref
    
    private AVLTree<LocalDate> dateIndex;               // date 
    private AVLTree<LocalDateTime> appointmentIndex;    //appointment time
    

    public VisitController() {
        visitList = new DoublyLinkedList<>();
        walkInQueue = new DoublyLinkedList<>();
        appointmentList = new DoublyLinkedList<>();
        
        visitIndex = new Hash<>();
        nameIndex = new AVLTree<>();
        
        appointmentIndex = new AVLTree<>();
        dateIndex = new AVLTree<>();
    }
    
    
    
    // HELPER METHODS TO CHECK QUEUE DUPLICATES / APP SLOT CONFLICT / REMOVE FROM QUEUE
    
    private void removeFromQueue(DoublyLinkedList<Visit> list, Visit target) {

        int pos = 1;
        var it = list.getIterator();

        while (it.hasNext()) {
            if (it.next() == target) {
                list.remove(pos);
                return;
            }
            pos++;
        }
    }
    
    private boolean isPatientInWalkInQueue(Patient p) {         // keep
        var it = walkInQueue.getIterator();

        while (it.hasNext()) {
            Visit v = it.next();
            if (v.getPatient().getNric().equals(p.getNric())) {     //no hash used here, because hash cant tell patient is in queue or not
                return true;
            }
        }
        return false;
    }
    
    // this uses dll, not optimal o(n)
    /*private boolean hasAppointmentConflict(Patient p, LocalDateTime slot) {

        var it = appointmentList.getIterator();

        while (it.hasNext()) {
            AppointmentVisit v = (AppointmentVisit) it.next();

            if (v.getPatient().getNric().equals(p.getNric()) &&
                v.getSlot().equals(slot)) {
                return true;
            }
        }

        return false;
    }*/

    // ================= CREATE =================

    public NodeReference registerWalkIn(String id, Patient p, Service s,
                                        int score, String note) {
                                        
    if (isPatientInWalkInQueue(p)) return null; // duplicate queue
        
        WalkInVisit v = new WalkInVisit(id, p, s, LocalDateTime.now(), score, note);

        NodeReference ref = visitList.add(v);
        
        visitIndex.put(id, ref);        // store index to hash
        nameIndex.insert(p.getName(), ref);     // store index to avl tree
        dateIndex.insert(v.getTime().toLocalDate(), ref);

        insertByPriority(v);   //  triage-based insert
        return ref;
    }

    public NodeReference createAppointment(String id, Patient p, Service s,
                                           LocalDateTime slot) {
        
        ListInterface<NodeReference> refs = appointmentIndex.search(slot);

        if (refs != null && !refs.isEmpty()) {
            return null; // slot already taken
        }       //o(log n)
        

        AppointmentVisit v = new AppointmentVisit(id, p, s,
                                                  LocalDateTime.now(), slot);

        NodeReference ref = visitList.add(v);
        
        visitIndex.put(id, ref);        // store index to hash
        nameIndex.insert(p.getName(), ref);     // store index to avl tree
            
        appointmentList.add(v);
        
        appointmentIndex.insert(slot, ref);     // for avl tree
        dateIndex.insert(v.getTime().toLocalDate(), ref);

        return ref;
    }

    // ================= TRIAGE PRIORITY =================

    private void insertByPriority(WalkInVisit visit) {

        int score = visit.getTriageScore();

        int pos = 1;
        var it = walkInQueue.getIterator();

        while (it.hasNext()) {
            WalkInVisit current = (WalkInVisit) it.next();

            if (score > current.getTriageScore()) {
                break;
            }
            pos++;
        }

        walkInQueue.add(pos, visit);
    }

    // ================= ASSIGN DOCTOR =================

    public boolean assignDoctor(NodeReference ref, Doctor doctor) {

        Visit v = visitList.getNodeData(ref);

        if (v == null) return false;

        // check specialization
        if (!v.getService().isCompatibleWith(doctor)) {
            return false;
        }

        // check availability
        if (!doctor.isAvailableAt(v.getTime().toLocalTime())) {
            return false;
        }

        v.setDoctor(doctor);
        return true;
    }

    // ================= PROCESS WALK-IN =================

    public Visit getNextWalkIn() {

        if (walkInQueue.isEmpty()) return null;

        Visit v = walkInQueue.getEntry(1);
        walkInQueue.remove(1);

        return v;
    }
    
    
    // ========= CONFIRM DEPOSIT ==============
    
    public boolean confirmAppointmentDeposit(NodeReference ref, double amount) {

        Visit v = visitList.getNodeData(ref);

        if (!(v instanceof AppointmentVisit)) return false;

        AppointmentVisit appt = (AppointmentVisit) v;

        if (amount <= 0) return false;

        appt.setDeposit(amount);  // internally sets status
        return true;
    }
    
    // ================= CANCEL ==================
    
    public boolean cancelVisit(String id) {

        NodeReference ref = visitIndex.get(id);
        if (ref == null) return false;

        Visit v = visitList.getNodeData(ref);
        if (v == null) return false;

        // remove from queues
        if (v instanceof WalkInVisit) {
            removeFromQueue(walkInQueue, v);
        } else if (v instanceof AppointmentVisit) {
            removeFromQueue(appointmentList, v);
            AppointmentVisit vv = (AppointmentVisit)v;
            appointmentIndex.delete(vv.getSlot(), ref);
        }

        // remove from indexes
        nameIndex.delete(v.getPatient().getName(), ref);
        visitIndex.remove(id);
        dateIndex.delete(v.getTime().toLocalDate(), ref);

        // remove from main storage
        visitList.removeNode(ref);

        return true;
    }
    
    // =========== EMERGENCY PRIORITY JUMP =======
    public boolean promoteToFront(NodeReference ref) {

        Visit v = visitList.getNodeData(ref);
        if (!(v instanceof WalkInVisit)) return false;

        // remove from queue (O(n))
        removeFromQueue(walkInQueue, v);

        // insert at front (O(1))
        walkInQueue.add(1, v);

        return true;
    }
    
    
    
    // ================= COMPLETE =================

    public boolean completeVisit(NodeReference ref) {

        Visit v = visitList.getNodeData(ref);
        if (v == null) return false;
        
        // only mark complete
        v.complete();

        return true;

    /*
        // generate invoice (SHOULD BELONG TO BILLING CONTROLLER NOT HERE)
        double serviceCharge = 0;
        if (v.getService() != null) {
            serviceCharge = v.getService().getBasePrice();
        }

        double medCharge = 0;
        if (v.getPrescription() != null) {
            medCharge = v.getPrescription().calculateTotalCost();
        }

        double discount = 0;

        if (v.getPatient().getCompany() != null) {
            discount = (serviceCharge + medCharge)
                       * v.getPatient().getCompany().getDiscountRate();
        }

        Invoice inv = new Invoice(
                "INV-" + v.getVisitID(),
                serviceCharge,
                medCharge,
                discount
        );

        
        //  add items snapshot
        if (v.getPrescription() != null) {
            var it = v.getPrescription().getItemsIterator(); // get the whole prescription item (medication + quantity)

            while (it.hasNext()) {
                var item = it.next();

                inv.addItem(
                        item.getMedication().getName(),
                        item.getQuantity(),
                        item.getMedication().getSellingPrice()
                );
            }
        }

        v.setInvoice(inv);
        v.complete();

        */
    }

    // ================= VIEW =================

    public ListInterface<Visit> getAllVisits() {
        ListInterface<Visit> result = new DoublyLinkedList<>();

        var it = visitList.getIterator();
        while (it.hasNext()) {
            result.add(it.next());
        }

        return result;
    }
    
    
    // TOTAL SUMMARY, SORT BY PATIENT NAME ALPHABETICAL
    public ListInterface<Visit> getVisitsSortedByName() {

        ListInterface<NodeReference> refs = nameIndex.getInOrderList();
        ListInterface<Visit> result = new DoublyLinkedList<>();

        var it = refs.getIterator();
        while (it.hasNext()) {
            result.add(visitList.getNodeData(it.next()));
        }

        return result;
    }
    
    
    //by id, hash
    public Visit findVisitByID(String id) {
        NodeReference ref = visitIndex.get(id);
        return visitList.getNodeData(ref);
    }
    
    //by patient name, avl
    public ListInterface<Visit> findVisitByName(String name) {
            
        ListInterface<NodeReference> refs = nameIndex.search(name);
        ListInterface<Visit> result = new DoublyLinkedList<>();

        if (refs == null) return result;

        var it = refs.getIterator();
        while (it.hasNext()) {
            result.add(visitList.getNodeData(it.next()));
        }

        return result;
    }
    
    
    // retrieve appointments at specific time using avl
    public ListInterface<Visit> getAppointmentsBySlot(LocalDateTime slot) {

        ListInterface<NodeReference> refs = appointmentIndex.search(slot);
        ListInterface<Visit> result = new DoublyLinkedList<>();

        if (refs == null) return result;

        var it = refs.getIterator();
        while (it.hasNext()) {
            result.add(visitList.getNodeData(it.next()));
        }

        return result;
    }
    
    
    // ========= FILTERING VISITS ===========
    
    // by doctor, assume doctors are less, so no use avl
    public ListInterface<Visit> getVisitsByDoctor(Doctor d) {

        ListInterface<Visit> result = new DoublyLinkedList<>();

        var it = visitList.getIterator();
        while (it.hasNext()) {
            Visit v = it.next();
            if (v.getDoctor() == d) {
                result.add(v);
            }
        }

        return result;
    }
    
    // visit type walk in
    public ListInterface<Visit> getWalkInVisits() {

        ListInterface<Visit> result = new DoublyLinkedList<>();

        var it = visitList.getIterator();
        while (it.hasNext()) {
            Visit v = it.next();
            if (v instanceof WalkInVisit) {
                result.add(v);
            }
        }

        return result;
    }
    
    // by date
    public ListInterface<Visit> getVisitsByDate(LocalDate date) {

        ListInterface<NodeReference> refs = dateIndex.search(date);
        ListInterface<Visit> result = new DoublyLinkedList<>();

        if (refs == null) return result;

        var it = refs.getIterator();
        while (it.hasNext()) {
            result.add(visitList.getNodeData(it.next()));
        }

        return result;
    }
    

    

    // GENERATE SUMMARY REPORT
    
    public void generateSummary() {

        int total = 0;
        int walkIn = 0;
        int appointment = 0;

        // ================= BASIC COUNT =================
        var it = visitList.getIterator();

        // service tracking
        DoublyLinkedList<Service> serviceList = new DoublyLinkedList<>();
        DoublyLinkedList<Integer> serviceCount = new DoublyLinkedList<>();

        // doctor tracking
        DoublyLinkedList<Doctor> doctorList = new DoublyLinkedList<>();
        DoublyLinkedList<Integer> doctorCount = new DoublyLinkedList<>();

        while (it.hasNext()) {
            Visit v = it.next();
            total++;

            if (v instanceof WalkInVisit) walkIn++;
            if (v instanceof AppointmentVisit) appointment++;

            // ================= SERVICE COUNT =================
            Service s = v.getService();
            if (s != null) {

                int pos = 1;
                boolean found = false;

                var sit = serviceList.getIterator();
                while (sit.hasNext()) {
                    if (sit.next() == s) {
                        int count = serviceCount.getEntry(pos);
                        serviceCount.remove(pos);
                        serviceCount.add(pos, count + 1);
                        found = true;
                        break;
                    }
                    pos++;
                }

                if (!found) {
                    serviceList.add(s);
                    serviceCount.add(1);
                }
            }

            // ================= DOCTOR COUNT =================
            Doctor d = v.getDoctor();
            if (d != null) {

                int pos = 1;
                boolean found = false;

                var dit = doctorList.getIterator();
                while (dit.hasNext()) {
                    if (dit.next() == d) {
                        int count = doctorCount.getEntry(pos);
                        doctorCount.remove(pos);
                        doctorCount.add(pos, count + 1);
                        found = true;
                        break;
                    }
                    pos++;
                }

                if (!found) {
                    doctorList.add(d);
                    doctorCount.add(1);
                }
            }
        }

        // ================= PRINT BASIC =================
        System.out.println("===== VISIT SUMMARY =====");
        System.out.println("Total Visits: " + total);
        System.out.println("Walk-in Visits: " + walkIn);
        System.out.println("Appointments: " + appointment);

        // ================= MOST USED SERVICE =================
        int maxService = 0;
        Service mostUsed = null;

        for (int i = 1; i <= serviceList.getNumberOfEntries(); i++) {
            int count = serviceCount.getEntry(i);

            if (count > maxService) {
                maxService = count;
                mostUsed = serviceList.getEntry(i);
            }
        }

        if (mostUsed != null) {
            System.out.println("Most Used Service: " +
                    mostUsed.getServiceName() + " (" + maxService + ")");
        }

        // ================= DOCTOR WORKLOAD =================
        System.out.println("Doctor Workload:");
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            int count = doctorCount.getEntry(i);

            System.out.println(d.getName() + ": " + count + " visits");
        }

        // ================= SORTED OUTPUT (AVL) =================
        System.out.println("---- Visits Sorted by Patient Name ----");

        ListInterface<NodeReference> refs = nameIndex.getInOrderList();

        var it2 = refs.getIterator();
        while (it2.hasNext()) {
            Visit v = visitList.getNodeData(it2.next());

            if (v instanceof AppointmentVisit) {
                AppointmentVisit av = (AppointmentVisit) v;
                System.out.println(
                    v.getVisitID() + " | " +
                    v.getPatient().getName() +
                    " | Slot: " + av.getSlot()
                );
            } else {
                System.out.println(
                    v.getVisitID() + " | " +
                    v.getPatient().getName() +
                    " | Walk-in: " + v.getTime()
                );
            }
        }
    }
}
/*
 * Module 1 - Patient & Doctor Management
 * Control class: UserController.java
 */
package Control;

import ADT.AVLTree;
import ADT.DoublyLinkedList;
import ADT.Hash;
import ADT.ListInterface;
import ADT.NodeReference;
import Entity.Company;
import Entity.Doctor;
import Entity.Patient;
import Entity.Specialization;
import java.time.LocalTime;

/**
 *
 * @author KC
 */
public class UserController {

    // ================= CORE STORAGE (DLL) =================
    private DoublyLinkedList<Patient> patientList;
    private DoublyLinkedList<Doctor> doctorList;

    // ================= PRIMARY INDEX (HASH) =================
    private Hash<String, NodeReference> patientIndex;   // NRIC → ref
    private Hash<String, NodeReference> doctorIndex;    // doctorID → ref

    // ================= SECONDARY INDEX (AVL) =================
    private AVLTree<String> patientNameIndex;           // name → ref(s)
    private AVLTree<String> doctorNameIndex;            // name → ref(s)
    private AVLTree<String> specialtyIndex;             // specialty → ref(s)


    // ================= CONSTRUCTOR =================
    public UserController() {
        patientList     = new DoublyLinkedList<>();
        doctorList      = new DoublyLinkedList<>();

        patientIndex    = new Hash<>();
        doctorIndex     = new Hash<>();

        patientNameIndex = new AVLTree<>();
        doctorNameIndex  = new AVLTree<>();
        specialtyIndex   = new AVLTree<>();
    }


    // =================================================================
    //  PATIENT OPERATIONS
    // =================================================================

    /**
     * Register a new patient.
     * Prevents duplicate NRIC using Hash.
     * @return NodeReference if successful, null if NRIC already exists
     */
    public NodeReference registerPatient(Patient p) {
        if (patientIndex.contains(p.getNric())) {
            System.out.println("Error: Patient with NRIC " + p.getNric() + " already exists.");
            return null;
        }

        NodeReference ref = patientList.add(p);     // Step 1: store in DLL
        patientIndex.put(p.getNric(), ref);          // Step 2: store ref in Hash
        patientNameIndex.insert(p.getName(), ref);   // Step 3: store ref in AVL

        System.out.println("Patient registered: " + p);
        return ref;
    }

    /**
     * Find a patient by NRIC (exact match).
     * Uses Hash → O(1)
     */
    public Patient findPatientByNRIC(String nric) {
        NodeReference ref = patientIndex.get(nric);
        if (ref == null) {
            System.out.println("Patient not found for NRIC: " + nric);
            return null;
        }
        return patientList.getNodeData(ref);
    }

    /**
     * Find patient(s) by name (may return multiple if same name).
     * Uses AVL → O(log n)
     */
    public ListInterface<Patient> findPatientByName(String name) {
        ListInterface<NodeReference> refs = patientNameIndex.search(name);
        ListInterface<Patient> result = new DoublyLinkedList<>();

        if (refs == null) {
            System.out.println("No patients found with name: " + name);
            return result;
        }

        var it = refs.getIterator();
        while (it.hasNext()) {
            result.add(patientList.getNodeData(it.next()));
        }
        return result;
    }

    /**
     * Update patient contact number.
     * Uses Hash to locate patient.
     */
    public boolean updatePatientContact(String nric, String newContact) {
        Patient p = findPatientByNRIC(nric);
        if (p == null) return false;

        p.setContactNumber(newContact);
        System.out.println("Contact updated for: " + p.getName());
        return true;
    }

    /**
     * Update patient allergy info.
     * Uses Hash to locate patient.
     */
    public boolean updatePatientAllergy(String nric, String newAllergy) {
        Patient p = findPatientByNRIC(nric);
        if (p == null) return false;

        p.setAllergy(newAllergy);
        System.out.println("Allergy updated for: " + p.getName());
        return true;
    }

    /**
     * Update patient company.
     * Uses Hash to locate patient.
     */
    public boolean updatePatientCompany(String nric, Company newCompany) {
        Patient p = findPatientByNRIC(nric);
        if (p == null) return false;

        p.setCompany(newCompany);
        System.out.println("Company updated for: " + p.getName());
        return true;
    }

    /**
     * Remove a patient by NRIC.
     * Cleans up Hash + AVL + DLL.
     */
    public boolean removePatient(String nric) {
        NodeReference ref = patientIndex.get(nric);
        if (ref == null) {
            System.out.println("Patient not found for NRIC: " + nric);
            return false;
        }

        Patient p = patientList.getNodeData(ref);

        patientIndex.remove(nric);                      // Step 1: remove from Hash
        patientNameIndex.delete(p.getName(), ref);      // Step 2: remove from AVL
        patientList.removeNode(ref);                    // Step 3: remove from DLL

        System.out.println("Patient removed: " + p.getName());
        return true;
    }

    /**
     * Get all patients (unsorted, DLL order).
     */
    public ListInterface<Patient> getAllPatients() {
        ListInterface<Patient> result = new DoublyLinkedList<>();
        var it = patientList.getIterator();
        while (it.hasNext()) result.add(it.next());
        return result;
    }

    /**
     * Get all patients sorted by name (A → Z).
     * Uses AVL in-order traversal.
     */
    public ListInterface<Patient> getAllPatientsSortedByName() {
        ListInterface<NodeReference> refs = patientNameIndex.getInOrderList();
        ListInterface<Patient> result = new DoublyLinkedList<>();
        var it = refs.getIterator();
        while (it.hasNext()) result.add(patientList.getNodeData(it.next()));
        return result;
    }


    // =================================================================
    //  DOCTOR OPERATIONS
    // =================================================================

    /**
     * Register a new doctor.
     * Prevents duplicate doctorID using Hash.
     * @return NodeReference if successful, null if ID already exists
     */
    public NodeReference registerDoctor(Doctor d) {
        if (doctorIndex.contains(d.getDoctorID())) {
            System.out.println("Error: Doctor with ID " + d.getDoctorID() + " already exists.");
            return null;
        }

        NodeReference ref = doctorList.add(d);                          // Step 1: store in DLL
        doctorIndex.put(d.getDoctorID(), ref);                          // Step 2: store ref in Hash
        doctorNameIndex.insert(d.getName(), ref);                       // Step 3: store ref in AVL (name)
        specialtyIndex.insert(d.getSpecialization().name(), ref);       // Step 4: store ref in AVL (specialty)

        System.out.println("Doctor registered: " + d);
        return ref;
    }

    /**
     * Find a doctor by ID (exact match).
     * Uses Hash → O(1)
     */
    public Doctor findDoctorByID(String id) {
        NodeReference ref = doctorIndex.get(id);
        if (ref == null) {
            System.out.println("Doctor not found for ID: " + id);
            return null;
        }
        return doctorList.getNodeData(ref);
    }

    /**
     * Find doctor(s) by name (may return multiple if same name).
     * Uses AVL → O(log n)
     */
    public ListInterface<Doctor> findDoctorByName(String name) {
        ListInterface<NodeReference> refs = doctorNameIndex.search(name);
        ListInterface<Doctor> result = new DoublyLinkedList<>();

        if (refs == null) {
            System.out.println("No doctors found with name: " + name);
            return result;
        }

        var it = refs.getIterator();
        while (it.hasNext()) result.add(doctorList.getNodeData(it.next()));
        return result;
    }

    /**
     * Find doctors by specialization.
     * Uses AVL specialty index → O(log n)
     */
    public ListInterface<Doctor> findDoctorBySpecialty(Specialization spec) {
        ListInterface<NodeReference> refs = specialtyIndex.search(spec.name());
        ListInterface<Doctor> result = new DoublyLinkedList<>();

        if (refs == null) {
            System.out.println("No doctors found for specialty: " + spec);
            return result;
        }

        var it = refs.getIterator();
        while (it.hasNext()) result.add(doctorList.getNodeData(it.next()));
        return result;
    }

    /**
     * Check if a specific doctor is available at a given time.
     * Uses Hash to find doctor, then calls entity method.
     */
    public boolean isDoctorAvailable(String doctorID, LocalTime time) {
        Doctor d = findDoctorByID(doctorID);
        if (d == null) return false;
        return d.isAvailableAt(time);
    }

    /**
     * Remove a doctor by ID.
     * Cleans up Hash + both AVL trees + DLL.
     */
    public boolean removeDoctor(String id) {
        NodeReference ref = doctorIndex.get(id);
        if (ref == null) {
            System.out.println("Doctor not found for ID: " + id);
            return false;
        }

        Doctor d = doctorList.getNodeData(ref);

        doctorIndex.remove(id);                                     // Step 1: remove from Hash
        doctorNameIndex.delete(d.getName(), ref);                   // Step 2: remove from name AVL
        specialtyIndex.delete(d.getSpecialization().name(), ref);   // Step 3: remove from specialty AVL
        doctorList.removeNode(ref);                                 // Step 4: remove from DLL

        System.out.println("Doctor removed: " + d.getName());
        return true;
    }

    /**
     * Get all doctors (unsorted, DLL order).
     */
    public ListInterface<Doctor> getAllDoctors() {
        ListInterface<Doctor> result = new DoublyLinkedList<>();
        var it = doctorList.getIterator();
        while (it.hasNext()) result.add(it.next());
        return result;
    }

    /**
     * Get all doctors sorted by name (A → Z).
     * Uses AVL in-order traversal.
     */
    public ListInterface<Doctor> getAllDoctorsSortedByName() {
        ListInterface<NodeReference> refs = doctorNameIndex.getInOrderList();
        ListInterface<Doctor> result = new DoublyLinkedList<>();
        var it = refs.getIterator();
        while (it.hasNext()) result.add(doctorList.getNodeData(it.next()));
        return result;
    }


    // =================================================================
    //  SUMMARY REPORT
    // =================================================================

    /**
     * Generate a summary report for patients and doctors.
     * Uses DLL traversal for counts, AVL for sorted display.
     */
    public void generateUserSummary() {

        // -------- PATIENT SUMMARY --------
        int totalPatients  = 0;
        int withCompany    = 0;
        int withMedCard    = 0;

        var pit = patientList.getIterator();
        while (pit.hasNext()) {
            Patient p = pit.next();
            totalPatients++;
            if (p.getCompany() != null)  withCompany++;
            if (p.hasMedicalCard())      withMedCard++;
        }

        System.out.println("========================================");
        System.out.println("           PATIENT SUMMARY              ");
        System.out.println("========================================");
        System.out.println("Total Patients        : " + totalPatients);
        System.out.println("With Company          : " + withCompany);
        System.out.println("With Medical Card     : " + withMedCard);

        System.out.println("\n--- All Patients (Sorted by Name) ---");
        ListInterface<Patient> sortedPatients = getAllPatientsSortedByName();
        var spit = sortedPatients.getIterator();
        int pNum = 1;
        while (spit.hasNext()) {
            System.out.println(pNum++ + ". " + spit.next());
        }

        // -------- DOCTOR SUMMARY --------
        int totalDoctors = 0;
        int[] specCount  = new int[Specialization.values().length];

        var dit = doctorList.getIterator();
        while (dit.hasNext()) {
            Doctor d = dit.next();
            totalDoctors++;
            specCount[d.getSpecialization().ordinal()]++;
        }

        System.out.println("\n========================================");
        System.out.println("           DOCTOR SUMMARY               ");
        System.out.println("========================================");
        System.out.println("Total Doctors         : " + totalDoctors);
        System.out.println("\nDoctors by Specialization:");
        for (Specialization s : Specialization.values()) {
            System.out.println("  " + s + " : " + specCount[s.ordinal()]);
        }

        System.out.println("\n--- All Doctors (Sorted by Name) ---");
        ListInterface<Doctor> sortedDoctors = getAllDoctorsSortedByName();
        var sdit = sortedDoctors.getIterator();
        int dNum = 1;
        while (sdit.hasNext()) {
            System.out.println(dNum++ + ". " + sdit.next());
        }

        System.out.println("========================================");
    }
}
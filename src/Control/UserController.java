/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import Entity.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Iterator;

/**
 *
 * @author Bob Gan Kun Chuan
 */
public class UserController {
    
    public static final LocalTime SYSTEM_START = LocalTime.of(10, 0);       //feelfree to change this, because localtime.now need this
    public static final LocalTime SYSTEM_END   = LocalTime.of(16, 0);
    public static final int SLOT_INTERVAL = 60;
    
    private BinarySearchTreeInterface<String, Patient> patients;
    private BinarySearchTreeInterface<String, Doctor> doctors;
    private BinarySearchTreeInterface<String, Company> companies;
    
    public UserController() {
        patients = new AVLTree<>();
        doctors = new AVLTree<>();
        companies = new AVLTree<>();
        
        // Predefine companies
        loadCompanies();
    }
    
    
    private void loadCompanies() {
        companies.add("ABC Corp", new Company("ABC Corp", "Singapore", "Corporate", 0.10));
        companies.add("XYZ Ltd", new Company("XYZ Ltd", "Singapore", "SME", 0.05));
        companies.add("MediCare", new Company("MediCare", "Singapore", "Healthcare", 0.15));
        companies.add("TechSys", new Company("TechSys", "Singapore", "Technology", 0.08));
    }
    public Iterator<Company> getAllCompanies() {
        return companies.inOrderIterator();
    }
    public Company findCompanyByName(String name) {  //company name as key
        return companies.get(name);
    }
    
    

    // =========================================================
    // ================= PATIENT OPERATIONS =====================
    // =========================================================
    
    // REGISTER
    public boolean registerPatient(String patientID, String name, String nric, int age,
                                   boolean hasMedicalCard, String allergy,
                                   String contactNumber, Company company, LocalDate datetime) {

        if (patients.contains(nric)) return false;

        Patient p = new Patient(patientID, name, nric, age,
                hasMedicalCard, allergy, contactNumber, company,
                datetime);

        return patients.add(nric, p);
    }
    
    
    //TWO PATIENT FINDER METHODS (find patient -> select actions (update/delete))
    
    // FIND BY NRIC, o(log n) faster (nric the key is ordered)
    public Patient findPatientByNRIC(String nric) {
        return patients.get(nric);
    }
     // FIND BY NAME (O(n)) slower (name is not key, have to traverse each node one by one to check value)
    public Iterator<Patient> findPatientByName(String name) {
        return new Iterator<>() {

            private final Iterator<Patient> it = patients.inOrderIterator();
            private Patient nextMatch = findNext();

            private Patient findNext() {
                while (it.hasNext()) {
                    Patient p = it.next();
                    if (p.getName().equalsIgnoreCase(name)) {
                        return p;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Patient next() {
                Patient current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
    
    
    // Generic update methods
    
    public boolean updatePatientContactNumber(Patient p, String newContact) {
        if (p == null) return false;
        if (newContact == null || newContact.isEmpty()) return false;
        p.setContactNumber(newContact);
        return true;
    }
    public boolean updatePatientAllergy(Patient p, String allergy) {
        if (p == null) return false;
        p.setAllergy(allergy);
        return true;
    }
    public boolean updatePatientCompany(Patient p, Company company) {
        if (p == null) return false;
        p.setCompany(company);
        return true;
    }
    public boolean updatePatientMedicalCard(Patient p, boolean hasMedicalCard) {
        if (p == null) return false;
        p.setHasMedicalCard(hasMedicalCard);
        return true;
    }
    
    // GENERIC DELETE  (while generic type T can be replaced with bool, better to show results)
    public Patient deletePatient(Patient p) {
        if (p == null) return null;
        return patients.remove(p.getNric());        // return actual patient data instead of boolean only
    }
    
    
    // GET ALLs
    public int getTotalPatients() {
        return patients.size();
    }

    public Iterator<Patient> getAllPatients() {
        return patients.inOrderIterator();
    }
    
    public Iterator<Patient> getNewPatientsToday(LocalDate targetDate) {
        return new Iterator<>() {

            private final Iterator<Patient> it = patients.inOrderIterator();
            private Patient nextMatch = findNext();

            private Patient findNext() {
                while (it.hasNext()) {
                    Patient p = it.next();
                    if (p.getRegisterDate().equals(targetDate)) {
                        return p;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Patient next() {
                Patient current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
    public Iterator<Patient> getNewPatientsByMonth(Month month) {
        return new Iterator<>() {

            private final Iterator<Patient> it = patients.inOrderIterator();
            private Patient nextMatch = findNext();

            private Patient findNext() {
                while (it.hasNext()) {
                    Patient p = it.next();
                    if (p.getRegisterDate().getMonth() == month) {
                        return p;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Patient next() {
                Patient current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
    
    public Iterator<Patient> getPatientsByCompany(String companyName) {
        return new Iterator<>() {

            private final Iterator<Patient> it = patients.inOrderIterator();
            private Patient nextMatch = findNext();

            private Patient findNext() {
                while (it.hasNext()) {
                    Patient p = it.next();

                    if (p.getCompany() != null &&
                        p.getCompany().getCompanyName().equalsIgnoreCase(companyName)) {
                        return p;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Patient next() {
                Patient current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
    
    
    
    // =========================================================
    // ================= DOCTOR OPERATIONS ======================
    // =========================================================

    public boolean registerDoctor(String id, String name, Specialization spec) {

        if (doctors.contains(id)) return false;
        Doctor d = new Doctor(id, name, spec);
        return doctors.add(id, d);
    }
    
    public Doctor findDoctorByID(String id) {
        return doctors.get(id);
    }

    public Iterator<Doctor> findDoctorByName(String name) {
        return new Iterator<>() {

            private final Iterator<Doctor> it = doctors.inOrderIterator();
            private Doctor nextMatch = findNext();

            private Doctor findNext() {
                while (it.hasNext()) {
                    Doctor d = it.next();
                    if (d.getName().equalsIgnoreCase(name)) {
                        return d;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Doctor next() {
                Doctor current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
    
    
    public Iterator<Doctor> getDoctorsBySpecialization(Specialization spec) {       // still O(n), key not specialization
        return new Iterator<>() {

            private final Iterator<Doctor> it = doctors.inOrderIterator();
            private Doctor nextMatch = findNext();

            private Doctor findNext() {
                while (it.hasNext()) {
                    Doctor d = it.next();
                    if (d.getSpecialization() == spec) {
                        return d;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextMatch != null;
            }

            @Override
            public Doctor next() {
                Doctor current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }
 
    public int getTotalDoctors() {
        return doctors.size();
    }

    public Iterator<Doctor> getAllDoctors() {
        return doctors.inOrderIterator();
    }
    
    
    // GENERIC DELETE  (while generic type T can be replaced with bool, better to show results)
    public Doctor deleteDoctor(Doctor d) {
        if (d == null) return null;
        return doctors.remove(d.getDoctorID());        // return actual patient data instead of boolean only
    }

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author user
 */
public class UserController {

    // ================= PATIENT =================

    public NodeReference registerPatient(String nric, String name);

    public Patient findPatientByNRIC(String nric);     // Hash

    public ListInterface<Patient> findPatientByName(String name); // AVL

    public boolean removePatient(String nric);

    public ListInterface<Patient> getAllPatients();


    // ================= DOCTOR =================

    public NodeReference registerDoctor(String id, String name, String specialty);

    public Doctor findDoctorByID(String id);

    public ListInterface<Doctor> findDoctorByName(String name);

    public ListInterface<Doctor> findDoctorBySpecialty(String specialty);

    public boolean removeDoctor(String id);

    public ListInterface<Doctor> getAllDoctors();


    // ================= SUMMARY =================

    public void generateUserSummary();
}

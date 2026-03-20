package dsa_assignment;
import dsa_assignment.Doctor.Specialization;

public class PatientDoctorControl {
    private DoublyLinkedList<Patient> patientList;
    private DoublyLinkedList<Doctor> doctorList;
    private Set<String> nricSet; // duplicate prevention

    // Patient operations
    public void addPatient(Patient p) { }        // check NRIC duplicate first
    public void updatePatient(String id) { }
    public void removePatient(String id) { }     // only if no visit history
    public Patient searchPatient(String nric) {
        return null;
    }
    public void listAllPatients() { }
    public void filterPatients() { }

    // Doctor operations
    public void addDoctor(Doctor d) { }
    public boolean isDoctorAvailable(String doctorId, int hour) {
        return false;
    }
    public void listDoctorsBySpecialization(Specialization s) { }
    public void listAllDoctors() { }
}

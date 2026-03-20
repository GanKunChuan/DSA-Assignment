package dsa_assignment;

public class Doctor {

    private String doctorId;
    private String name;
    private Specialization specialization; // ENUM
    private int workingStartHour;
    private int workingEndHour;
    
    public enum Specialization {
        GENERAL, CARDIOLOGY, GYNECOLOGY, PEDIATRICS, MINOR_SURGERY
    }
    
}

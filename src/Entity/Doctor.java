/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author user
 */
import java.time.LocalTime;

public class Doctor {

    private String doctorID;
    private String name;
    private Specialization specialization;

    private LocalTime workingStartHour;
    private LocalTime workingEndHour;

    public Doctor(String doctorID, String name, Specialization specialization,
                  LocalTime workingStartHour, LocalTime workingEndHour) {
        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
        this.workingStartHour = workingStartHour;
        this.workingEndHour = workingEndHour;
    }

    // ================= GETTERS =================

    public String getDoctorID() {
        return doctorID;
    }

    public String getName() {
        return name;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public LocalTime getWorkingStartHour() {
        return workingStartHour;
    }

    public LocalTime getWorkingEndHour() {
        return workingEndHour;
    }

    // ================= LOGIC (light, allowed in entity) =================

    public boolean isAvailableAt(LocalTime time) {
        return !time.isBefore(workingStartHour) && !time.isAfter(workingEndHour);
    }

    @Override
    public String toString() {
        return doctorID + " - " + name + " (" + specialization + ")";
    }

}
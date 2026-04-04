/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalTime;

/**
 *
 * @author user
 */
public class Doctor {

    private String doctorID;
    private String name;
    private Specialization specialization;


    public Doctor(String doctorID, String name, Specialization specialization) {
        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
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


    @Override
    public String toString() {
        return doctorID + " - " + name + " (" + specialization + ")";
    }

}

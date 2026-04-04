/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author user
 */
public class Service {

    private String serviceID;
    private String serviceName;

    private boolean requiresEquipment;          // appointment vs walk-in
    private double basePrice;

    private Specialization specialization;

    public Service(String serviceID, String serviceName,
                   boolean requiresEquipment, double basePrice,
                   Specialization specialization) {

        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.requiresEquipment = requiresEquipment;
        this.basePrice = basePrice;
        this.specialization = specialization;
    }

    // ================= GETTERS =================

    public String getServiceID() {
        return serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean requiresEquipment() {
        return requiresEquipment;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    // ================= LIGHT LOGIC =================

    public boolean isCompatibleWith(Doctor doctor) {
        return doctor.getSpecialization() == specialization;
    }

    @Override
    public String toString() {
        return serviceName + " (RM " + basePrice + ")";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author user
 */
public class AppointmentVisit extends Visit {
    
    private LocalDateTime slot;
    private double depositAmount;
    private AppointmentStatus apptStatus;     // specific
    
    public AppointmentVisit(String visitID, Patient patient, Service service,
                            LocalDateTime time, LocalDateTime slot) {
        super(visitID, patient, service, time);
        this.slot = slot;
        this.apptStatus = AppointmentStatus.PENDING_DEPOSIT;
    }

    public LocalDateTime getSlot() { return slot; }

    public void setDeposit(double amount) {
        this.depositAmount = amount;
        this.apptStatus = AppointmentStatus.CONFIRMED;
    }

    public AppointmentStatus getStatus() {
        return apptStatus;
    }


    @Override
    public String getVisitType() {
        return "Appointment";
    }
}

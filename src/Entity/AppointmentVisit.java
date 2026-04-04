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
    
    public AppointmentVisit(String visitID, Patient patient, Service service,
                            LocalDateTime time, LocalDateTime slot, double depositAmount) {
        super(visitID, patient, service, time);
        this.slot = slot;
        this.depositAmount = depositAmount;  // Fixed required amount
        this.setStatus(VisitStatus.REGISTERED);  // Changed from PENDING_DEPOSIT
    }

    public LocalDateTime getSlot() { return slot; }

    public void payDeposit() {
        if (getStatus() != VisitStatus.REGISTERED)
            throw new IllegalStateException("Invalid visit state");
        this.setStatus(VisitStatus.CONFIRMED);
    }

    public double getDepositAmount() {
        return depositAmount;
    }



    @Override
    public String getVisitType() {
        return "Appointment";
    }
}


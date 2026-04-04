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
public abstract class Visit {
    protected String visitID;
    protected Patient patient;
    protected Doctor doctor;
    protected Service service;
    protected Prescription prescription;
    protected LocalDateTime time;
    protected Invoice invoice;
    protected VisitStatus status;
    
    
    public Visit(String visitID, Patient patient, Service service, LocalDateTime time) {
        this.visitID = visitID;
        this.patient = patient;
        this.service = service;
        this.time = time;
        this.status = VisitStatus.REGISTERED;
    }
    
    public String getVisitID() { return visitID; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public Service getService() { return service; }
    public LocalDateTime getTime() { return time; }
    public Prescription getPrescription() { return prescription; }
    
   
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice != null) {
            throw new IllegalStateException("Invoice already set");
        }
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    
    public void setDoctor(Doctor doctor) {
        if (status == VisitStatus.COMPLETED) {
            throw new IllegalStateException("Cannot modify completed visit");
        }
        this.doctor = doctor;
    }
    
    
    public abstract String getVisitType();

    
    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }
    
    
}

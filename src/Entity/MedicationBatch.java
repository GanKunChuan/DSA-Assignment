/*
 * @author Shaft (Module 3 - Prescription, Inventory & Billing)
 */
package Entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MedicationBatch {

    private String batchID;
    private Medication medication;
    private int quantityAdded;
    private String supplierName;
    private LocalDate receivedDate;
    private LocalDate expiryDate;

    private static final int EXPIRY_WARNING_DAYS = 30;

    public MedicationBatch(String batchID, Medication medication,
                           int quantityAdded, String supplierName,
                           LocalDate receivedDate, LocalDate expiryDate) {
        this.batchID = batchID;
        this.medication = medication;
        this.quantityAdded = quantityAdded;
        this.supplierName = supplierName;
        this.receivedDate = receivedDate;
        this.expiryDate = expiryDate;
    }

    public String getBatchID()        { return batchID; }
    public Medication getMedication() { return medication; }
    public int getQuantityAdded()     { return quantityAdded; }
    public String getSupplierName()   { return supplierName; }
    public LocalDate getReceivedDate(){ return receivedDate; }
    public LocalDate getExpiryDate()  { return expiryDate; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon() {
        long daysLeft = daysUntilExpiry();
        return daysLeft >= 0 && daysLeft <= EXPIRY_WARNING_DAYS;
    }

    public long daysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MedicationBatch)) return false;
        MedicationBatch other = (MedicationBatch) obj;
        return this.batchID.equals(other.batchID);
    }

    @Override
    public String toString() {
        String status;
        if (isExpired()) {
            status = "*** EXPIRED ***";
        } else if (isExpiringSoon()) {
            status = "! Expiring in " + daysUntilExpiry() + " days";
        } else {
            status = "OK (" + daysUntilExpiry() + " days left)";
        }
        return String.format("[%s] %s | Supplier: %s | Expiry: %s | %s",
                batchID, medication.getName(), supplierName, expiryDate, status);
    }
}
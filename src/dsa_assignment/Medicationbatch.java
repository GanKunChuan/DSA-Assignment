package dsa_assignment;
import java.time.LocalDate;

public class Medicationbatch {
    private String batchID;
    private Medication medication;
    private int quantityAdded;
    private String supplierName;
    private LocalDate receivedDate;
    private LocalDate expiryDate;

    public boolean isExpired() { return false; }
    public boolean isExpiringSoon() { return false; }  // within 30 days
    public long daysUntilExpiry() { return 0; }
}
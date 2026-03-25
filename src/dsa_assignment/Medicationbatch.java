package dsa_assignment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Medicationbatch {
    private String batchID;
    private Medication medication;
    private int quantityAdded;
    private String supplierName;
    private LocalDate receivedDate;
    private LocalDate expiryDate;

    public boolean isExpired() { return LocalDate.now().isAfter(expiryDate); }
    public boolean isExpiringSoon() { return !isExpired() && daysUntilExpiry() <= 30; }
    public long daysUntilExpiry() { return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate); }
}
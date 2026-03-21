package dsa_assignment;
import java.time.LocalDate;

public class Invoice {
    private String invoiceID;
    private Object visit;           // reference to Visit (from LZ's module)
    private Prescription prescription;
    private double serviceCharge;
    private double discountedAmt;
    private boolean isPaid;
    private LocalDate dateGenerated;

    public double getTotalPayable() { return 0; } // serviceCharge + prescription.getTotalCost() - discountedAmt
    public void applyDiscount(double rate) { }
}
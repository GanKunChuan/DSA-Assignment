package dsa_assignment;
import java.time.LocalDateTime;

public class Payment {
    private String paymentID;
    private Invoice invoice;         // reference
    private double amountPaid;
    private PaymentMethod method;
    private LocalDateTime timestamp;
    private String receivedBy;

    public enum PaymentMethod { CASH, CARD, INSURANCE }

    public void markInvoicePaid() { }           // sets invoice.isPaid = true
    public double getChange() { return 0; }     // amountPaid - invoice.getTotalPayable()
}
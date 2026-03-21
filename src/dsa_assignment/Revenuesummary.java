package dsa_assignment;
import java.time.LocalDate;

public class Revenuesummary {
    private String summaryID;
    private LocalDate date;
    private double totalRevenue;
    private int totalInvoices;
    private int unpaidCount;
    private double totalCostOfMeds;

    // computed snapshot, not a stored record
    public void generate(DoublyLinkedList<Invoice> invoiceList) { }
    public DoublyLinkedList<Invoice> getUnpaidInvoices(DoublyLinkedList<Invoice> list) { return null; }
    public double getProfit() { return 0; } // totalRevenue - totalCostOfMeds
}
package dsa_assignment;
import java.time.LocalDate;

public class Prescription {
    private String prescriptionID;
    private DoublyLinkedList<Prescribeditem> items;  // nested ADT
    private String notes;
    private LocalDate dateIssued;

    public void addItem(Prescribeditem item) { }
    public void removeItem(String itemID) { }
    public double getTotalCost() { return 0; }   // iterates items, sums getSubtotal()
    public void applyToStock() { }               // iterates items, calls medication.reduceStock()
}
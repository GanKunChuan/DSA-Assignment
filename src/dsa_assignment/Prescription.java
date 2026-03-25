package dsa_assignment;

import java.time.LocalDate;

public class Prescription {
    private String prescriptionID;
    private DoublyLinkedList<Prescribeditem> items;  // nested ADT
    private String notes;
    private LocalDate dateIssued;

    public void addItem(Prescribeditem item) { items.add(item); }
    public void removeItem(String itemID) { }   // iterate items, match itemID, remove
    public double getTotalCost() { return 0; }  // iterate items, sum getSubtotal()
    public void applyToStock() { }              // iterate items, call medication.reduceStock()
}
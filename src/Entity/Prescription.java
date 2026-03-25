/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import ADT.DoublyLinkedList;
import ADT.ListInterface;
import java.util.Iterator;

/**
 *
 * @author user
 */
public class Prescription {

    private String prescriptionID;

    private ListInterface<PrescriptionItem> items;
    private String note;

    public Prescription(String prescriptionID, String note) {
        this.prescriptionID = prescriptionID;
        this.note = note;
        this.items = new DoublyLinkedList<>();
    }

    // ================= INNER ITEM =================

    public static class PrescriptionItem { //if private cannot access
        private Medication medication;
        private int quantity;

        PrescriptionItem(Medication medication, int quantity) {
            this.medication = medication;
            this.quantity = quantity;
        }

        public Medication getMedication() {
            return medication;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // ================= METHODS =================

    public void addMedication(Medication med, int quantity) {
        if (med != null && quantity > 0) {
            items.add(new PrescriptionItem(med, quantity));
        }
    }

    public double calculateTotalCost() {
        double total = 0;

        var it = items.getIterator();
        while (it.hasNext()) {
            PrescriptionItem item = it.next();
            total += item.medication.getSellingPrice() * item.quantity;
        }

        return total;
    }

    public ListInterface<Medication> getMedications() {
        ListInterface<Medication> result = new DoublyLinkedList<>();

        var it = items.getIterator();
        while (it.hasNext()) {
            result.add(it.next().medication);
        }

        return result;
    }
    
    public Iterator<PrescriptionItem> getItemsIterator() {
        return items.getIterator();
    }

    public String getNote() {
        return note;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }
}

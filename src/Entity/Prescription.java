/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import java.util.Iterator;

/**
 *
 * @author user
 */
public class Prescription {

    private String prescriptionID;
    private String note;
    
    private BinarySearchTreeInterface<Integer, PrescriptionItem> items;
    private int itemCounter;
    private boolean locked;

    public Prescription(String prescriptionID, String note) {
        this.prescriptionID = prescriptionID;
        this.note = note;
        
        this.items = new AVLTree<>();
        this.itemCounter = 0;
        this.locked = false;
    }

    // ================= INNER ITEM =================

    public static class PrescriptionItem { //if private cannot access
        private String medID;
        private String medName;
        private int quantity;
        private String dosage;

        public PrescriptionItem(String medID, String medName, int quantity, String dosage) {
            this.medID = medID;
            this.medName = medName;
            this.quantity = quantity;
            this.dosage = dosage;
        }

        public String getMedID() {
            return medID;
        }
        public String getMedName() { return medName; }
        public int getQuantity() { return quantity; }
        public String getDosage() { return dosage; }
    }

    // ================= METHODS =================

    public void addMedication(String medID, String medName, int quantity, String dosage) {
        if (locked) throw new IllegalStateException("Prescription locked");

        items.add(itemCounter++, new PrescriptionItem(medID, medName, quantity, dosage));
    }
    
    public void lock() {
        locked = true;
    }

    
    public Iterator<PrescriptionItem> getItems() {
        return items.inOrderIterator();
    }

    public String getNote() {
        return note;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }
}


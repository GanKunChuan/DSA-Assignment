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
public class Invoice {

    private String invoiceID;
    private double serviceCharge;
    private double medicationCharge;
    private double discountAmount;
    
    private int itemCounter;  // unique identifier as key, multiple items cannot use invoiceid key msut be unique
    private BinarySearchTreeInterface<Integer, InvoiceItem> items;  //itemcounter + item
    private boolean locked;
    
    
    //inner class since we make invoice a static entity for storage purpose (like frozen snapshot of transaction)
    // contents of an invoice will not change in the future even details like name or price of medication changes 
    public static class InvoiceItem {   //if private cannot access
        private String name;
        private int quantity;
        private double price;

        public InvoiceItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            
        }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }

    public Invoice(String invoiceID,
                   double serviceCharge,
                   double discountAmount) {

        this.invoiceID = invoiceID;
        this.serviceCharge = serviceCharge;
        this.discountAmount = discountAmount;
        this.medicationCharge = 0;
        this.items = new AVLTree<>();
        this.itemCounter = 0;
        this.locked = false;
    }

    // ================= GETTERS =================

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getMedicationCharge() {
        return medicationCharge;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotalAmount() {
        return serviceCharge + medicationCharge - discountAmount;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void addItem(String name, int quantity, double price) {
        if (locked) throw new IllegalStateException("Invoice locked");
        
        items.add(itemCounter++, new InvoiceItem(name, quantity, price));
        
        medicationCharge += price * quantity;
    }
    
        // ================= BUILD FROM PRESCRIPTION =================
    public void generateFromPrescription(Prescription prescription,
                                         BinarySearchTreeInterface<String, Medication> medStore) {

        var it = prescription.getItems();

        while (it.hasNext()) {
            var pItem = it.next();

            Medication med = medStore.get(pItem.getMedID());

            double price = med.getSellingPrice(); // snapshot price

            addItem(pItem.getMedName(), pItem.getQuantity(), price);
        }
    }
    
    public void setDiscount(double discount) {
        if (locked) throw new IllegalStateException("Invoice locked");
        this.discountAmount = discount;
    }

    
    public Iterator<InvoiceItem> getItems() {
        return items.inOrderIterator();
    }
    
    public void lock() {
        locked = true;
    }
    
    @Override
    public String toString() {
        return "Invoice " + invoiceID +
               " | Total: RM " + getTotalAmount();
    }
}
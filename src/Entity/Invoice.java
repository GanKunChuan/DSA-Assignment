/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import ADT.DoublyLinkedList;
import ADT.ListInterface;

/**
 *
 * @author user
 */
public class Invoice {

    private String invoiceID;
    
    private ListInterface<InvoiceItem> items;

    private double serviceCharge;
    private double medicationCharge;
    private double discountAmount;
    
    
    //inner class since we make invoice a static entity for storage purpose (like frozen snapshot of transaction)
    // contents of an invoice will not change in the future even details like name or price of medication changes 
    public static class InvoiceItem {   //if private cannot access
        String name;
        int quantity;
        double price;

        InvoiceItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            
        }
        
        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    public Invoice(String invoiceID,
                   double serviceCharge,
                   double medicationCharge,
                   double discountAmount) {

        this.invoiceID = invoiceID;
        this.serviceCharge = serviceCharge;
        this.medicationCharge = medicationCharge;
        this.discountAmount = discountAmount;
        this.items = new DoublyLinkedList<>();
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
        items.add(new InvoiceItem(name, quantity, price));
    }
    
    @Override
    public String toString() {
        return "Invoice " + invoiceID +
               " | Total: RM " + getTotalAmount();
    }
}
    


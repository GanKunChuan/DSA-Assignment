/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author user
 */
public class Medication {

    private String medID;
    private String name;
    private int quantity;          // current stock

    private double costPrice;
    private double sellingPrice;

    public Medication(String medID, String name, int quantity,
                      double costPrice, double sellingPrice) {
        this.medID = medID;
        this.name = name;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    // ================= GETTERS =================

    public String getMedID() {
        return medID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    
    //no setters not even for prices, assume they are always fixed
    
    // ================= STOCK LOGIC =================

    public boolean reduceStock(int amount) {
        if (amount <= 0 || amount > quantity) {
            return false;
        }
        quantity -= amount;
        return true;
    }

    public void restock(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    @Override
    public String toString() {
        return name + " (Stock: " + quantity + ")";
    }
}
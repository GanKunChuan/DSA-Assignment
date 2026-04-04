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
    
    private int lowStockThreshold;

    public Medication(String medID, String name, int quantity,
                      double costPrice, double sellingPrice, int lowStockThreshold) {
        this.medID = medID;
        this.name = name;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.lowStockThreshold = lowStockThreshold;
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

    public int getLowStockThreshold()  { return lowStockThreshold; }
    

    public void updateSellingPrice(double newPrice) {
        if (newPrice > 0) {
            this.sellingPrice = newPrice;
        }
    }
    public void updateCostPrice(double newPrice) {
        if (newPrice > 0) {
            this.costPrice = newPrice;
        }
    }
    
    public void setLowStockThreshold(int threshold) {
        if (threshold >= 0) {
            this.lowStockThreshold = threshold;
        }
    }
    
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
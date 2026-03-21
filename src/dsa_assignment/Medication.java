package dsa_assignment;

public class Medication {
    private String medID;
    private String name;
    private int quantity;
    private int lowStockThreshold;
    private double costPrice;
    private double sellingPrice;

    public boolean isLowStock() { return quantity < lowStockThreshold; }
    public void reduceStock(int qty) { }
    public void restock(int qty) { }
}
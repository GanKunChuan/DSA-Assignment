/*
 * @author Shaft (Module 3 - Prescription, Inventory & Billing)
 */
package Entity; //

public class Medication {

    private String medID;
    private String name;
    private int quantity;
    private int lowStockThreshold;
    private double costPrice;
    private double sellingPrice;

    public Medication(String medID, String name, int quantity,
                      int lowStockThreshold,
                      double costPrice, double sellingPrice) {
        this.medID = medID;
        this.name = name;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getMedID()           { return medID; }
    public String getName()            { return name; }
    public int getQuantity()           { return quantity; }
    public int getLowStockThreshold()  { return lowStockThreshold; }
    public double getCostPrice()       { return costPrice; }
    public double getSellingPrice()    { return sellingPrice; }

    public void setLowStockThreshold(int threshold) {
        if (threshold >= 0) {
            this.lowStockThreshold = threshold;
        }
    }

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

    public boolean isLowStock() {
        return quantity <= lowStockThreshold;
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Medication)) return false;
        Medication other = (Medication) obj;
        return this.medID.equals(other.medID);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Stock: %d | Sell: RM%.2f%s",
                medID, name, quantity, sellingPrice,
                isLowStock() ? " *** LOW STOCK ***" : "");
    }
}

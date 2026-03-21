package dsa_assignment;

public class Prescribeditem {
    private String itemID;
    private Medication medication;   // reference, not copy
    private int quantityPrescribed;
    private String dosage;
    private String frequency;

    public double getSubtotal() {
        return 0; // quantityPrescribed × medication.sellingPrice
    }
}
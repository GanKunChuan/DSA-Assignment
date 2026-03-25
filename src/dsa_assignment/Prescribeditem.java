package dsa_assignment;

public class Prescribeditem {
    private String itemID;
    private Medication medication;
    private int quantityPrescribed;
    private String dosage;
    private String frequency;

    public double getSubtotal() { return quantityPrescribed * medication.getSellingPrice(); }
}
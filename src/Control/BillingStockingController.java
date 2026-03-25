/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

/**
 *
 * @author user
 */
public class BillingStockingController {

    // ================= PRESCRIPTION =================

    public boolean createPrescription(NodeReference visitRef, String note);

    public boolean addMedication(NodeReference visitRef,
                                 Medication med, int quantity);

    public Prescription getPrescription(NodeReference visitRef);


    // ================= INVENTORY =================

    public boolean addMedicationStock(Medication med, int qty);

    public boolean reduceMedicationStock(Medication med, int qty);

    public ListInterface<Medication> getAllMedications();

    public Medication findMedicationByName(String name);


    // ================= BILLING =================

    public Invoice generateInvoice(NodeReference visitRef);

    public double calculateTotal(NodeReference visitRef);

    public boolean applyDiscount(NodeReference visitRef);


    // ================= SUMMARY =================

    public void generateTreatmentSummary();
}

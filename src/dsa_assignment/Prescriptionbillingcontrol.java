package dsa_assignment;

public class Prescriptionbillingcontrol {
    private DoublyLinkedList<Medication> medicationList;   // master store
    private DoublyLinkedList<Medicationbatch> batchList;
    private DoublyLinkedList<Invoice> invoiceList;
    private Set<String> medicationNameSet;                 // duplicate prevention (Jeff's instruction)

    // Prescription
    public void createPrescription(String visitID) { }
    public void addPrescribedItem(String prescriptionID, Prescribeditem item) { }

    // Inventory
    public void addMedication(Medication m) { }            // check name duplicate via Set first
    public void restockMedication(String medID, int qty) { }
    public void addBatch(Medicationbatch batch) { }
    public void checkLowStock() { }                        // iterates list, calls isLowStock()
    public void checkExpiringSoon() { }                    // iterates batchList, calls isExpiringSoon()

    // Billing
    public void generateInvoice(String visitID) { }
    public void processPayment(String invoiceID, double amount, Payment.PaymentMethod method) { }
    public void filterUnpaidInvoices() { }

    // Reporting
    public void generateDailySummary() { }
    public void showPrescriptionCostBreakdown(String prescriptionID) { }

    // Search / filter
    public Medication searchMedication(String medID) { return null; }
    public void listAllMedications() { }
    public Invoice searchInvoice(String invoiceID) { return null; }
}
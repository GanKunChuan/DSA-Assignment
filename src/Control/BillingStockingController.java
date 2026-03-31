/*
 * @author Shaft (Module 3 - Prescription, Inventory & Billing)
 */
package Control; //

import ADT.AVLTree;
import ADT.DoublyLinkedList;
import ADT.Hash;
import ADT.ListInterface;
import ADT.NodeReference;
import Entity.Company;
import Entity.Invoice;
import Entity.Medication;
import Entity.MedicationBatch;
import Entity.Prescription;
import Entity.Visit;

public class BillingStockingController {

    // PRIMARY STORAGE (DLL)
    private DoublyLinkedList<Medication> medicationList;
    private DoublyLinkedList<MedicationBatch> batchList;
    private DoublyLinkedList<Visit> visitRef;

    // PRIMARY INDEX (Hash) — O(1) exact lookup
    private Hash<String, NodeReference> medicationIndex;

    // SECONDARY INDEX (AVL) — O(log n) sorted lookup
    private AVLTree<String> medicationNameIndex;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public BillingStockingController(DoublyLinkedList<Visit> sharedVisitList) {
        medicationList      = new DoublyLinkedList<>();
        batchList           = new DoublyLinkedList<>();
        medicationIndex     = new Hash<>();
        medicationNameIndex = new AVLTree<>();
        this.visitRef       = sharedVisitList;
    }

    // =====================================================
    // SECTION 1: INVENTORY
    // =====================================================

    public boolean addMedication(Medication med) {
        if (med == null) return false;

        if (medicationIndex.contains(med.getMedID())) {
            System.out.println("[INVENTORY] Medication ID already exists: " + med.getMedID());
            return false;
        }

        NodeReference ref = medicationList.add(med);
        medicationIndex.put(med.getMedID(), ref);
        medicationNameIndex.insert(med.getName(), ref);

        System.out.println("[INVENTORY] Added: " + med);
        return true;
    }

    public boolean addMedicationStock(String medID, int qty) {
        if (qty <= 0) return false;

        NodeReference ref = medicationIndex.get(medID);
        if (ref == null) {
            System.out.println("[INVENTORY] Medication not found: " + medID);
            return false;
        }

        Medication med = medicationList.getNodeData(ref);
        if (med == null) return false;

        med.restock(qty);
        System.out.println("[INVENTORY] Restocked " + med.getName()
                + " by " + qty + " | New stock: " + med.getQuantity());
        return true;
    }

    public boolean reduceMedicationStock(String medID, int qty) {
        NodeReference ref = medicationIndex.get(medID);
        if (ref == null) return false;

        Medication med = medicationList.getNodeData(ref);
        if (med == null) return false;

        boolean success = med.reduceStock(qty);
        if (!success) {
            System.out.println("[INVENTORY] Insufficient stock for: " + med.getName());
            return false;
        }

        if (med.isLowStock()) {
            System.out.println("[ALERT] Low stock: " + med.getName()
                    + " | Remaining: " + med.getQuantity());
        }

        return true;
    }

    public Medication findMedicationByID(String medID) {
        NodeReference ref = medicationIndex.get(medID);
        if (ref == null) return null;
        return medicationList.getNodeData(ref);
    }

    public ListInterface<Medication> findMedicationByName(String name) {
        ListInterface<NodeReference> refs = medicationNameIndex.search(name);
        ListInterface<Medication> result = new DoublyLinkedList<>();

        if (refs == null) return result;

        var it = refs.getIterator();
        while (it.hasNext()) {
            Medication med = medicationList.getNodeData(it.next());
            if (med != null) result.add(med);
        }
        return result;
    }

    public ListInterface<Medication> getAllMedicationsSorted() {
        ListInterface<NodeReference> refs = medicationNameIndex.getInOrderList();
        ListInterface<Medication> result = new DoublyLinkedList<>();

        var it = refs.getIterator();
        while (it.hasNext()) {
            Medication med = medicationList.getNodeData(it.next());
            if (med != null) result.add(med);
        }
        return result;
    }

    public ListInterface<Medication> getAllMedications() {
        ListInterface<Medication> result = new DoublyLinkedList<>();
        var it = medicationList.getIterator();
        while (it.hasNext()) result.add(it.next());
        return result;
    }

    public boolean removeMedication(String medID) {
        NodeReference ref = medicationIndex.get(medID);
        if (ref == null) return false;

        Medication med = medicationList.getNodeData(ref);
        if (med == null) return false;

        medicationIndex.remove(medID);
        medicationNameIndex.delete(med.getName(), ref);
        medicationList.removeNode(ref);

        System.out.println("[INVENTORY] Removed: " + med.getName());
        return true;
    }

    // =====================================================
    // SECTION 2: BATCH / EXPIRY
    // =====================================================

    public boolean receiveMedicationBatch(MedicationBatch batch) {
        if (batch == null) return false;

        batch.getMedication().restock(batch.getQuantityAdded());
        batchList.add(batch);

        System.out.println("[BATCH] Received: " + batch);
        return true;
    }

    // =====================================================
    // SECTION 3: PRESCRIPTION
    // =====================================================

    public boolean createPrescription(Visit visit, String prescriptionID, String note) {
        if (visit == null) return false;
        if (visit.getPrescription() != null) {
            System.out.println("[PRESCRIPTION] Visit already has a prescription.");
            return false;
        }

        Prescription prescription = new Prescription(prescriptionID, note);
        visit.setPrescription(prescription);
        System.out.println("[PRESCRIPTION] Created: " + prescriptionID);
        return true;
    }

    public boolean addMedicationToPrescription(Visit visit, String medID, int quantity) {
        if (visit == null) return false;

        Prescription prescription = visit.getPrescription();
        if (prescription == null) {
            System.out.println("[PRESCRIPTION] No prescription found. Create one first.");
            return false;
        }

        Medication med = findMedicationByID(medID);
        if (med == null) {
            System.out.println("[PRESCRIPTION] Medication not found: " + medID);
            return false;
        }

        if (med.getQuantity() < quantity) {
            System.out.println("[PRESCRIPTION] Insufficient stock for: " + med.getName());
            return false;
        }

        prescription.addMedication(med, quantity);
        System.out.println("[PRESCRIPTION] Added " + quantity + "x " + med.getName());
        return true;
    }

    public Prescription getPrescription(Visit visit) {
        if (visit == null) return null;
        return visit.getPrescription();
    }

    public boolean applyPrescriptionToStock(Visit visit) {
        if (visit == null) return false;

        Prescription prescription = visit.getPrescription();
        if (prescription == null) return false;

        var it = prescription.getItemsIterator();
        while (it.hasNext()) {
            Prescription.PrescriptionItem item = it.next();
            Medication med = item.getMedication();
            int qty = item.getQuantity();

            boolean reduced = med.reduceStock(qty);
            if (!reduced) {
                System.out.println("[STOCK] Could not reduce: " + med.getName()
                        + " (needed: " + qty + ", available: " + med.getQuantity() + ")");
            } else if (med.isLowStock()) {
                System.out.println("[ALERT] Low stock after dispensing: " + med.getName()
                        + " | Remaining: " + med.getQuantity());
            }
        }
        return true;
    }

    // =====================================================
    // SECTION 4: BILLING
    // =====================================================

    public Invoice generateInvoice(Visit visit, String invoiceID) {
        if (visit == null) return null;
        if (visit.getInvoice() != null) {
            System.out.println("[BILLING] Invoice already generated.");
            return visit.getInvoice();
        }

        double serviceCharge = (visit.getService() != null)
                ? visit.getService().getBasePrice() : 0;

        double medicationCharge = (visit.getPrescription() != null)
                ? visit.getPrescription().calculateTotalCost() : 0;

        double discountAmount = 0;
        Company company = visit.getPatient().getCompany();
        if (company != null) {
            discountAmount = (serviceCharge + medicationCharge) * company.getDiscountRate();
        }

        Invoice invoice = new Invoice(invoiceID, serviceCharge, medicationCharge, discountAmount);

        if (visit.getPrescription() != null) {
            var it = visit.getPrescription().getItemsIterator();
            while (it.hasNext()) {
                Prescription.PrescriptionItem item = it.next();
                invoice.addItem(
                        item.getMedication().getName(),
                        item.getQuantity(),
                        item.getMedication().getSellingPrice()
                );
            }
        }

        visit.setInvoice(invoice);
        System.out.println("[BILLING] Invoice generated: " + invoice);
        return invoice;
    }

    public double calculateTotal(Visit visit) {
        if (visit == null) return 0;

        double serviceCharge = (visit.getService() != null)
                ? visit.getService().getBasePrice() : 0;

        double medicationCharge = (visit.getPrescription() != null)
                ? visit.getPrescription().calculateTotalCost() : 0;

        double discountAmount = 0;
        Company company = visit.getPatient().getCompany();
        if (company != null) {
            discountAmount = (serviceCharge + medicationCharge) * company.getDiscountRate();
        }

        return serviceCharge + medicationCharge - discountAmount;
    }

    public boolean applyDiscount(Visit visit, double discountAmount) {
        if (visit == null) return false;
        Invoice invoice = visit.getInvoice();
        if (invoice == null) {
            System.out.println("[BILLING] No invoice found.");
            return false;
        }
        System.out.println("[BILLING] Discount of RM" + discountAmount
                + " noted for: " + invoice.getInvoiceID());
        return true;
    }

    // =====================================================
    // SECTION 5: SUMMARY REPORTS
    // =====================================================

    public void generateTreatmentSummary() {

        System.out.println("\n========================================");
        System.out.println("     TREATMENT & BILLING SUMMARY        ");
        System.out.println("========================================");

        // REPORT 1: LOW STOCK ALERT
        System.out.println("\n--- [1] LOW STOCK ALERT ---");
        int lowCount = 0;
        var medIt = medicationList.getIterator();
        while (medIt.hasNext()) {
            Medication med = medIt.next();
            if (med.isLowStock()) {
                System.out.println("  " + med);
                lowCount++;
            }
        }
        if (lowCount == 0) System.out.println("  All medications adequately stocked.");

        // REPORT 2: EXPIRY ALERT
        System.out.println("\n--- [2] EXPIRY ALERT ---");
        int expiryCount = 0;
        var batchIt = batchList.getIterator();
        while (batchIt.hasNext()) {
            MedicationBatch batch = batchIt.next();
            if (batch.isExpired() || batch.isExpiringSoon()) {
                System.out.println("  " + batch);
                expiryCount++;
            }
        }
        if (expiryCount == 0) System.out.println("  No expiry issues detected.");

        // REPORT 3: REVENUE SUMMARY
        System.out.println("\n--- [3] REVENUE SUMMARY ---");
        double totalRevenue   = 0;
        double totalDiscount  = 0;
        double highestInvoice = 0;
        double totalMedCost   = 0;
        int totalInvoices     = 0;

        var visitIt = visitRef.getIterator();
        while (visitIt.hasNext()) {
            Visit v = visitIt.next();
            Invoice inv = v.getInvoice();
            if (inv != null) {
                double total = inv.getTotalAmount();
                totalRevenue  += total;
                totalDiscount += inv.getDiscountAmount();
                totalMedCost  += inv.getMedicationCharge();
                totalInvoices++;
                if (total > highestInvoice) highestInvoice = total;
            }
        }

        System.out.printf("  Total Invoices    : %d%n", totalInvoices);
        System.out.printf("  Total Revenue     : RM%.2f%n", totalRevenue);
        System.out.printf("  Total Discounts   : RM%.2f%n", totalDiscount);
        System.out.printf("  Total Med Revenue : RM%.2f%n", totalMedCost);
        System.out.printf("  Highest Invoice   : RM%.2f%n", highestInvoice);
        if (totalInvoices > 0) {
            System.out.printf("  Avg Bill/Visit    : RM%.2f%n", totalRevenue / totalInvoices);
        }

        // REPORT 4: MEDICATION USAGE
        System.out.println("\n--- [4] MEDICATION USAGE ---");

        DoublyLinkedList<Medication> trackedMeds = new DoublyLinkedList<>();
        DoublyLinkedList<Integer> usageCounts    = new DoublyLinkedList<>();

        var visitIt2 = visitRef.getIterator();
        while (visitIt2.hasNext()) {
            Visit v = visitIt2.next();
            if (v.getPrescription() == null) continue;

            var presIt = v.getPrescription().getItemsIterator();
            while (presIt.hasNext()) {
                Prescription.PrescriptionItem item = presIt.next();
                Medication med = item.getMedication();
                int qty = item.getQuantity();

                int pos = 1;
                boolean found = false;
                var trackIt = trackedMeds.getIterator();
                while (trackIt.hasNext()) {
                    if (trackIt.next() == med) {
                        int current = usageCounts.getEntry(pos);
                        usageCounts.remove(pos);
                        usageCounts.add(pos, current + qty);
                        found = true;
                        break;
                    }
                    pos++;
                }
                if (!found) {
                    trackedMeds.add(med);
                    usageCounts.add(qty);
                }
            }
        }

        int maxUsage = 0;
        Medication mostPrescribed = null;
        for (int i = 1; i <= trackedMeds.getNumberOfEntries(); i++) {
            int count = usageCounts.getEntry(i);
            System.out.println("  " + trackedMeds.getEntry(i).getName()
                    + ": " + count + " units dispensed");
            if (count > maxUsage) {
                maxUsage = count;
                mostPrescribed = trackedMeds.getEntry(i);
            }
        }

        if (mostPrescribed != null) {
            System.out.println("  Most Prescribed: " + mostPrescribed.getName()
                    + " (" + maxUsage + " units)");
        } else {
            System.out.println("  No prescriptions issued yet.");
        }

        // REPORT 5: SORTED MEDICATION LIST (AVL)
        System.out.println("\n--- [5] ALL MEDICATIONS (SORTED A-Z) ---");
        ListInterface<Medication> sorted = getAllMedicationsSorted();
        var sortedIt = sorted.getIterator();
        while (sortedIt.hasNext()) {
            System.out.println("  " + sortedIt.next());
        }

        System.out.println("\n========================================\n");
    }
}
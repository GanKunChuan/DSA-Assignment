/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import Entity.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
/**
 *
 * @author Kudzaishe Masunda Blessed
 */
public class BillingStockController  {
    private BinarySearchTreeInterface<String, Medication> medications;
    private BinarySearchTreeInterface<String, Invoice> invoices;
    
    private VisitController visitController;
    
    private int prescriptionCounter = 0;
    private int invoiceCounter = 0;
    
    
    public BillingStockController(VisitController visitController) {
        this.visitController = visitController;
        medications = new AVLTree<>();
        invoices = new AVLTree<>();
    }

    
    
    // ============= ADD NEW MEDICATION ==============
    public boolean addNewMedication(Medication m) {
        return medications.add(m.getMedID(), m);    
    }
    
    // ============= DELETE MEDICATION ==============
    public boolean removeMedication(String medID) {
        return medications.remove(medID) != null;
    }
    
    
    // GENERIC MEDICATION FINDERS
    public Medication findMedicationByID(String id) {
        return medications.get(id);
    }
    
    public Medication findMedicationByName(String name) {

        Iterator<Medication> it = medications.inOrderIterator();

        while (it.hasNext()) {
            Medication m = it.next();
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }
    
    public Iterator<Medication> getAllMedications() {
        return medications.inOrderIterator();
    }
    
    // ============= MEDICATION PRICING/COST CHANGE ==============
    public boolean updateMedicationSellingPrice(String medID, double newPrice) {
        Medication m = medications.get(medID);
        if (m == null) return false;

        m.updateSellingPrice(newPrice);
        return true;
    }
    
    public boolean updateMedicationCostPrice(String medID, double newPrice) {
        Medication m = medications.get(medID);
        if (m == null) return false;

        m.updateCostPrice(newPrice);
        return true;
    }
    
    
    // ============= STOCK NUMBER CHANGE ==============
    public boolean addStock(String medID, int amount) {
        Medication m = medications.get(medID);
        if (m == null) return false;

        m.restock(amount);
        return true;
    }
    
    public boolean reduceStock(String medID, int amount) {
        Medication m = medications.get(medID);
        if (m == null) return false;

        return m.reduceStock(amount);
    }
    
    // ============= LOW STOCK ALERT ==============
    public Iterator<Medication> getLowStockMedications() {

        Iterator<Medication> it = medications.inOrderIterator();

        return new Iterator<>() {

            private Medication next = findNext();

            private Medication findNext() {
                while (it.hasNext()) {
                    Medication m = it.next();
                    if (m.getQuantity() <= m.getLowStockThreshold()) {
                        return m;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() { return next != null; }

            @Override
            public Medication next() {
                Medication current = next;
                next = findNext();
                return current;
            }
        };
    }
    
    
    // ============= DOCTOR GENERATE PRESCRIPTION DURING CONSULT ==============
    public boolean createPrescription(String visitID, String note) {
        
        Visit v = visitController.findVisitByID(visitID);
        
        if (v == null) {
            System.out.println(" Visit not found: " + visitID);
            return false;
        }
        if (v.getStatus() != VisitStatus.IN_PROGRESS) {
            System.out.println(" Visit must be IN_PROGRESS to create prescription. Current status: " + v.getStatus());
            return false;
        }
        if (v.getPrescription() != null) {
            System.out.println(" Prescription already exists for this visit.");
            return false;
        }
        
        Prescription p = new Prescription(generatePrescriptionID(), note);
        v.setPrescription(p);
        System.out.println(" Prescription created: " + p.getPrescriptionID());
        return true;
        
    }
    
    private String generatePrescriptionID() {
        return "P" + (++prescriptionCounter);
    }
    // ============= DOCTOR ADD MEDICATION TO CREATED DESCRIPTION ==============
    public boolean addMedicationToPrescription(String visitID,
                                           String medID,
                                           int qty,
                                           String dosage) {

        Visit v = visitController.findVisitByID(visitID);
        if (v == null) return false;
        Prescription p = v.getPrescription();
        if (p == null) return false;
        
        Medication med = findMedicationByID(medID);
        if (med == null) return false;
        if (med.getQuantity() < qty) return false;

        p.addMedication(medID, med.getName(), qty, dosage); 
        return true;
    }
    
    
    public boolean applyPrescriptionToStock(Prescription p) {

        Iterator<Prescription.PrescriptionItem> it = p.getItems();  //adt is implemented at prescription entity

        while (it.hasNext()) {
            var item = it.next();

            Medication m = medications.get(item.getMedID());
            if (m == null || !m.reduceStock(item.getQuantity())) {
                return false;
            }
        }

        p.lock();
        return true;
    }
    
    public Invoice processCompletedVisit(Visit visit) {

        if (visit == null){
            System.out.println(" Visit is null.");
            return null;
        }
        if (visit.getStatus() != VisitStatus.COMPLETED){
            System.out.println(" Visit not completed yet. Status: " + visit.getStatus());
            return null;
        }
        if (visit.getInvoice() != null){
            System.out.println(" Invoice already generated.");
            return visit.getInvoice();
        }
        if (visit.getPrescription() != null) {
            applyPrescriptionToStock(visit.getPrescription());
        }

        return generateInvoice(visit);
    }
    
    public Invoice generateInvoice(Visit visit) {

        double serviceCharge = visit.getService().getBasePrice();
        
        String invoiceID = String.format("I%04d", ++invoiceCounter);  // I0001
        Invoice inv = new Invoice(invoiceID,
                              serviceCharge,
                              0); // temp discount

        if (visit.getPrescription() != null) {
            inv.generateFromPrescription(visit.getPrescription(), medications);
            //adt is implemented at invoice entity
        }
        
        
        //compute discount on FULL amount
        double totalBeforeDiscount = inv.getServiceCharge() + inv.getMedicationCharge();
        double discount = 0;

        if (visit.getPatient().getCompany() != null) {
            discount = totalBeforeDiscount * visit.getPatient().getCompany().getDiscountRate();
        }
        
        inv.setDiscount(discount);  
        inv.lock();
        visit.setInvoice(inv);
        
        invoices.add(inv.getInvoiceID(), inv);      // Store invoice for later retrieval

        return inv;
    }
    
    
    // Get invoice by ID
    public Invoice findInvoiceByID(String invoiceID) {
        return invoices.get(invoiceID);
    }

    // Get all invoices
    public Iterator<Invoice> getAllInvoices() {
        return invoices.inOrderIterator();
    }

    
    public double getRevenueByDate(LocalDate date, VisitController visitController) {

        Iterator<Visit> it = visitController.getVisitByDate(date);

        double total = 0;

        while (it.hasNext()) {
            Visit v = it.next();

            if (v.getInvoice() != null) {
                total += v.getInvoice().getTotalAmount();
            }
        }

        return total;
    }
    public double getRevenueByRange(LocalDate start, LocalDate end, VisitController vc) {

        double total = 0;

        LocalDate current = start;

        while (!current.isAfter(end)) {
            total += getRevenueByDate(current, vc);
            current = current.plusDays(1);
        }

        return total;
    }

    
}   
    
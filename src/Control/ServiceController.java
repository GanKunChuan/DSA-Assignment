/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import ADT.AVLTree;
import ADT.BinarySearchTreeInterface;
import Entity.*;
import java.util.Iterator;
/**
 *
 * @author Soh Lian Ze
 * 
 */
public class ServiceController {
    private BinarySearchTreeInterface<String, Service> services;
    
    public ServiceController() {
        services = new AVLTree<>();
        loadServices();
    }
    
    private void loadServices() {
        // ID, Name, RequiresEquipment, Price, RequiredSpecialization
        addService("UR001", "Urine Screening", false, 25.00, Specialization.GENERAL);
        addService("COV001", "Covid & Influenza Screening", false, 80.00, Specialization.GENERAL);
        addService("ECG001", "ECG", true, 120.00, Specialization.CARDIOLOGY);
        addService("VAC001", "Vaccines & Immunisation", false, 45.00, Specialization.GENERAL);
        addService("DRS001", "Advance Dressing", false, 35.00, Specialization.GENERAL);
        addService("MIN001", "Minor Surgery", true, 200.00, Specialization.MINOR_SURGERY);
        addService("WND001", "Wound Care", false, 40.00, Specialization.GENERAL);
        addService("PAP001", "Pap Smear", true, 90.00, Specialization.GYNECOLOGY);
        addService("CON001", "Contraceptions", false, 50.00, Specialization.GYNECOLOGY);
        addService("PED001", "Child Vaccination", false, 60.00, Specialization.PEDIATRICS);
        addService("PED002", "Growth & Development Assessment", false, 80.00, Specialization.PEDIATRICS);
    }
    
    private void addService(String id, String name, boolean requiresEquipment, 
                            double price, Specialization spec) {
        services.add(id, new Service(id, name, requiresEquipment, price, spec));
    }
    
    public Service findService(String id) {
        return services.get(id);
    }
    
    public boolean addNewService(String id, String name, boolean requiresEquipment, 
                             double price, Specialization spec) {
        if (services.contains(id)) {
            System.out.println("Service ID already exists.");
            return false;
        }
        addService(id, name, requiresEquipment, price, spec);
        System.out.println("Service added: " + name);
        return true;
    }
    
    public Iterator<Service> getAllServices() {
        return services.inOrderIterator();
    }
    
    public Iterator<Service> getServicesBySpecialization(Specialization spec) {
        return new Iterator<>() {
            private final Iterator<Service> it = services.inOrderIterator();
            private Service nextMatch = findNext();
            
            private Service findNext() {
                while (it.hasNext()) {
                    Service s = it.next();
                    if (s.getSpecialization() == spec) {
                        return s;
                    }
                }
                return null;
            }
            
            @Override
            public boolean hasNext() { return nextMatch != null; }
            
            @Override
            public Service next() {
                Service current = nextMatch;
                nextMatch = findNext();
                return current;
            }
        };
    }

}

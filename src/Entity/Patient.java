/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDate;

/**
 *
 * @author user
 */
public class Patient {

    private String patientID;
    private String name;
    private String nric;
    private int age;

    private boolean hasMedicalCard;
    private String allergy;
    private String contactNumber;
    private Company company;
    
    private LocalDate registerDate;

    public Patient(String patientID, String name, String nric, int age,
                   boolean hasMedicalCard, String allergy,
                   String contactNumber, Company company, LocalDate registerDate) {

        this.patientID = patientID;
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.hasMedicalCard = hasMedicalCard;
        this.allergy = allergy;
        this.contactNumber = contactNumber;
        this.company = company;
        this.registerDate = registerDate;
    }

    // ================= GETTERS =================

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public boolean hasMedicalCard() {
        return hasMedicalCard;
    }

    public String getAllergy() {
        return allergy;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Company getCompany() {
        return company;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }
    
    
    
    // ================= LIMITED MUTATORS =================

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setHasMedicalCard(boolean hasMedicalCard) {
        this.hasMedicalCard = hasMedicalCard;
    }

    @Override
    public String toString() {
        return patientID + " - " + name + " (" + nric + ")";
    }
}

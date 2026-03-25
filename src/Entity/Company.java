/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author user
 */
public class Company {

    private String companyName;
    private String companyLocation;
    private String companyType;
    private double discountRate;

    public Company(String name, String location, String type, double discountRate) {
        this.companyName = name;
        this.companyLocation = location;
        this.companyType = type;
        this.discountRate = discountRate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public String toString() {
        return companyName + " (" + (discountRate * 100) + "% discount)";
    }
}

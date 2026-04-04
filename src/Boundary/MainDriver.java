/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

/**
 *
 * @author Soh Lian Ze
 */
import Control.BillingStockController;
import Control.ServiceController;
import Control.UserController;
import Control.VisitController;
import Helper.InputHelper;
import java.time.LocalDate;

public class MainDriver {
    public static void main(String[] args) {
        
        // Create controllers (single instance shared across UIs)
        UserController userController = new UserController();
        ServiceController serviceController = new ServiceController();
        VisitController visitController = new VisitController(userController, serviceController);
        BillingStockController billingController = new BillingStockController(visitController);
        AdminUI adminUI = new AdminUI(billingController, visitController, serviceController, userController);
        
        // Create UIs with dependency injection
        ManageUserUI userUI = new ManageUserUI(userController);
        ReceptionistUI receptionistUI = new ReceptionistUI(visitController, serviceController);
        DoctorUI doctorUI = new DoctorUI(visitController, billingController);
        
        // Run main menu
        while (true) {
            System.out.println("\n----------------------------");
            System.out.println("|        CLINIC SYSTEM          |");
            System.out.println("|---------------------------");
            System.out.println("| 1. User Management            |");
            System.out.println("| 2. Receptionist               |");
            System.out.println("| 3. Doctor                     |");
            System.out.println("| 4. Admin (Inventory & Reports)|");
            System.out.println("| 5. Set System Date (for testing)");  
            // does not affect localdate.now (the real world system time)
            System.out.println("| 0. Exit                       |");
            System.out.println("----------------------------");
            
            int choice = InputHelper.readInt("Choose option: ");
            
            switch (choice) {
                case 1 -> userUI.start();
                case 2 -> receptionistUI.menu();
                case 3 -> doctorUI.menu();
                case 4 -> adminUI.menu();
                case 5 -> {
                    LocalDate newDate = InputHelper.readDate("Enter system date (yyyy-MM-dd): ");
                    VisitController.setSystemDate(newDate);
                    System.out.println("System date set to: " + newDate);
                }
                case 0 -> {
                    System.out.println("Thank you for using the system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

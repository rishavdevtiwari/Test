/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package checkinnproject;

import checkinn.controller.RedirectToRegcontroller;
import checkinn.view.LoginView;
/**
 *
 * @author ACER NITRO
 */
public class CheckInnProject {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        // TODO code application logic here
        LoginView loginView=new LoginView();
        loginView.setVisible(true);
        
        java.awt.EventQueue.invokeLater(() -> {
        new RedirectToRegcontroller(loginView); // Initialize controller
        loginView.setVisible(true);
    });
        
        
        
    }
    
}

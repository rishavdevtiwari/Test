/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package checkinn.controller;

import checkinn.view.LoginView;
import checkinn.view.RegistrationView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RedirectToRegcontroller {
    
    private final LoginView loginView;
    private RegistrationView registrationView;
    
    public RedirectToRegcontroller(LoginView loginView) {
        this.loginView = loginView;
        initController();
    }
    
    private void initController() {
        // Add mouse listener to the redirect label
        loginView.getRedirectToReg().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToRegistration();
            }
        });
    }
    
    private void redirectToRegistration() {
        // Hide the login view
        loginView.setVisible(false);
        
        // Create and show the registration view
        if (registrationView == null) {
            registrationView = new RegistrationView();
            // You might want to pass the loginView reference to registrationView
            // so you can return to it later
            registrationView.setLoginViewReference(loginView);
            registrationView.setLocationRelativeTo(null);
        }
        registrationView.setVisible(true);
    }
}

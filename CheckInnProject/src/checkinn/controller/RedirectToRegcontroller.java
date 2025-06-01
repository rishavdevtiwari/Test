package checkinn.controller;

import checkinn.view.LoginView;
import checkinn.view.RegistrationView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RedirectToRegController {
    private final LoginView loginView;
    
    public RedirectToRegController(LoginView loginView) {
        this.loginView = loginView;
        initController();
    }
    
    private void initController() {
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
        
        // Create and show registration view
        RegistrationView registrationView = new RegistrationView();
        registrationView.setLocationRelativeTo(null);
        
        // Set up the back navigation
        new RedirectToLoginController(registrationView, loginView);
        
        registrationView.setVisible(true);
    }
}
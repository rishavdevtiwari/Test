package checkinn.controller;

import checkinn.view.LoginView;
import checkinn.view.RegistrationView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RedirectToLoginController {
    private final RegistrationView registrationView;
    private final LoginView loginView;
    
    public RedirectToLoginController(RegistrationView registrationView, LoginView loginView) {
        this.registrationView = registrationView;
        this.loginView = loginView;
        initController();
    }
    
    private void initController() {
        registrationView.getRedirectToLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                redirectToLogin();
            }
        });
    }
    
    private void redirectToLogin() {
        // Hide the registration view
        registrationView.setVisible(false);
        
        // Show the login view
        loginView.setLocationRelativeTo(null);
        loginView.setVisible(true);
    }
}
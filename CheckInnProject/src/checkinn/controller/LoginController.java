package checkinn.controller;

import checkinn.controller.mail.SMTPSMailSender;
import checkinn.dao.UserDao;
import checkinn.model.LoginRequest;
import checkinn.model.ResetPasswordRequest; 
import checkinn.view.AdminDashboard;
import checkinn.view.DashboardView;
import checkinn.view.LoginView;
import checkinn.view.RegistrationView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class LoginController {
    private final LoginView loginView;
    private final UserDao userDao;
    private String generatedOTP;
    private String otpEmail;

    public LoginController(LoginView loginView, UserDao userDao) {
        this.loginView = loginView;
        this.userDao = userDao;
        this.loginView.addLoginListener(new LoginListener());
        this.loginView.addRegisterNavigationListener(new RegisterNavigationListener());
        this.loginView.addForgotPasswordListener(new ForgotPasswordListener());
    }

    public void open() {
        loginView.setLocationRelativeTo(null); // Center the login view
        loginView.setVisible(true);
    }

    public void close() {
        loginView.dispose();
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = loginView.getEmail();
            String password = loginView.getPassword();
            
            if (email.isEmpty() || password.isEmpty()) {
                loginView.showError("Please enter both email and password.");
                return;
            }
            // First, check for the hardcoded admin credentials.
            if (email.equals("admin") && password.equals("admin@")) {
                
                loginView.showMessage("Admin login successful!");
                close(); // Close the login view

                // Create and show the new Admin Dashboard
                AdminDashboard adminView = new AdminDashboard();
                AdminDashboardController adminController = new AdminDashboardController(adminView);
                adminController.showView();
            
            } else {
                // If not admin, proceed with the database check for regular users.
LoginRequest request = new LoginRequest(email, password);
            if (!request.isValid()) {
                loginView.showError("Invalid email format.");
                return;
            }

            // Check if email is a Gmail address and does not exist
            if (email.endsWith("@gmail.com") && !userDao.emailExists(email)) {
                int choice = javax.swing.JOptionPane.showConfirmDialog(
                    loginView,
                    "This Gmail is not registered. Would you like to create a new account?",
                    "Register New Account",
                    javax.swing.JOptionPane.YES_NO_OPTION
                );
                if (choice == javax.swing.JOptionPane.YES_OPTION) {
                    close();
                    RegistrationView registrationView = new RegistrationView();
                    RegistrationController registrationController = new RegistrationController(registrationView, userDao);
                    registrationController.open();
                }
                return;
            }

            // Now check credentials
            boolean authenticated = userDao.authenticateUser(email, password);
            if (authenticated) {
                loginView.showMessage("Login successful!");
                close();

                DashboardView dashboardView = new DashboardView();
                DashboardController dashboardController = new DashboardController(dashboardView, email);
                dashboardController.open();
            } else {
                loginView.showError("Invalid email or password.");
            }
        }
    }
    
    }

    class RegisterNavigationListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            close();
            RegistrationView registrationView = new RegistrationView();
            RegistrationController registrationController = new RegistrationController(registrationView, userDao);
            registrationController.open();
        }
    }

    class ForgotPasswordListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            String email = loginView.promptForEmail();
            if (email == null || email.trim().isEmpty()) {
                loginView.showError("Email is required.");
                return;
            }
            if (!userDao.emailExists(email)) {
                loginView.showError("No user found with this email.");
                return;
            }
            
            // Generate OTP
            generatedOTP = String.format("%06d", new Random().nextInt(999999));
            otpEmail = email;
            
            // Send OTP
            boolean sent = SMTPSMailSender.sendOtpEmail(email, generatedOTP);
            if (!sent) {
                loginView.showError("Failed to send OTP. Please try again.");
                return;
            }
            loginView.showMessage("OTP sent to your email.");

            // Prompt for OTP
            String enteredOtp = loginView.promptForOTP();
            if (enteredOtp == null || !enteredOtp.equals(generatedOTP)) {
                loginView.showError("Invalid OTP.");
                return;
            }

            // Prompt for new password
            String newPassword = loginView.promptForNewPassword();
            if (newPassword == null || newPassword.trim().isEmpty()) {
                loginView.showError("Password cannot be empty.");
                return;
            }

            ResetPasswordRequest resetRequest = new ResetPasswordRequest(email, newPassword);
            boolean reset = userDao.resetPassword(resetRequest.getEmail(), resetRequest.getNewPassword());
            if (reset) {
                loginView.showMessage("Password reset successful! Please login with your new password.");
            } else {
                loginView.showError("Failed to reset password. Please try again.");
            }
        }
    }
}
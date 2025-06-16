package checkinn.controller;

import checkinn.dao.UserDao;
import checkinn.model.RegistrationRequest;
import checkinn.view.LoginView;
import checkinn.view.RegistrationView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class RegistrationController {
    private final RegistrationView registrationView;
    private final UserDao userDao;

    public RegistrationController(RegistrationView registrationView, UserDao userDao) {
        this.registrationView = registrationView;
        this.userDao = userDao;
        this.registrationView.addRegisterListener(new RegisterListener());
        this.registrationView.addLoginNavigationListener(new LoginNavigationListener());
    }

    public void open() {
        registrationView.setVisible(true);
    }

    public void close() {
        registrationView.dispose();
    }

    class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = registrationView.getFirstName();
            String lastName = registrationView.getLastName();
            String email = registrationView.getEmail();
            String password = registrationView.getPassword();
            String phoneNumber = registrationView.getPhoneNumber();

            RegistrationRequest request = new RegistrationRequest(
                firstName, lastName, email, password, phoneNumber
            );

            if (!request.isValid()) {
                registrationView.showError("Please fill all fields.");
                return;
            }

            boolean registered = userDao.registerUser(request);
            if (registered) {
                registrationView.showMessage("Registration successful! Please login.");
                close();
                LoginView loginView = new LoginView();
                UserDao userDao = new UserDao();
                LoginController loginController = new LoginController(loginView, userDao);
                loginController.open();
            } else {
                registrationView.showError("Registration failed. Email may already be in use.");
            }
        }
    }
    
        class LoginNavigationListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            close();
        LoginView loginView = new LoginView();
        UserDao userDao = new UserDao();
        LoginController loginController = new LoginController(loginView, userDao);
        loginController.open();
        loginView.setLocationRelativeTo(null);
        }

    }
    
}
package checkinn.controller;

import checkinn.dao.UserDao;
import checkinn.model.UserData;
import checkinn.view.UserProfileView;

public class UserProfileController {
    private final UserProfileView userProfileView;
    private final DashboardController dashboardController;
    private final UserData user;

    public UserProfileController(UserProfileView userProfileView, DashboardController dashboardController, UserData user) {
        this.userProfileView = userProfileView;
        this.dashboardController = dashboardController;
        this.user = user;
        

        initializeListeners();
    }

    private void initializeListeners() {
        userProfileView.addBackToDashboardListener(e -> {
            userProfileView.dispose();
            
           
            dashboardController.open();
        });

        userProfileView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Object parentController = null;
                if (parentController instanceof checkinn.controller.DashboardController dashboardController) {
                    dashboardController.open();
        } else if (parentController instanceof checkinn.controller.BookingHistoryController bookingHistoryController) {
            bookingHistoryController.open();
        }
    }
});
    
    userProfileView.addDeleteAccountListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                userProfileView,
                "Are you sure you want to delete your account? This action cannot be undone.",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                UserDao userDao = new UserDao();
                boolean deleted = userDao.deleteUserByEmail(user.getEmail());
                if (deleted) {
                    javax.swing.JOptionPane.showMessageDialog(userProfileView, "Account deleted successfully.", "Deleted", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    userProfileView.dispose();
                    dashboardController.close();
                    // Open login view
                    checkinn.view.LoginView loginView = new checkinn.view.LoginView();
                    checkinn.dao.UserDao newUserDao = new checkinn.dao.UserDao();
                    checkinn.controller.LoginController loginController = new checkinn.controller.LoginController(loginView, newUserDao);
                    loginController.open();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(userProfileView, "Failed to delete account.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

 public void open() {
    String fullName = user.getFirstName() + " " + user.getLastName();
    String phone = user.getPhoneNumber();
    userProfileView.setUserProfileData(fullName, phone);
    userProfileView.setLocationRelativeTo(null);
    userProfileView.setVisible(true);
}

  
}
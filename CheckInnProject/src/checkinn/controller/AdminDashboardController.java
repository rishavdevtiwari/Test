package checkinn.controller;

import checkinn.dao.UserDao;
import checkinn.view.AdminClientView;
import checkinn.view.AdminDashboard; // IMPORTANT: Make sure this class name matches your view file!
import checkinn.view.LoginView;
import javax.swing.JOptionPane;

public class AdminDashboardController {
    
    private final AdminDashboard view;
    // private final RoomStatusDao roomStatusDao; 

    public AdminDashboardController(AdminDashboard view) {
        this.view = view;
        // this.roomStatusDao = new RoomStatusDao(); // database work
        
        initializeListeners();
        loadInitialRoomStatuses();
    }
    
//initial state setting of dashboard
    private void loadInitialRoomStatuses() {
        view.setSingleRoomStatus("Vacant");
        view.setDoubleRoomStatus("Occupied");
        view.setDeluxeRoomStatus("Out of Order");
        view.setSuiteRoomStatus("Vacant");
    }

//Action listeners for set status buttons
    private void initializeListeners() {
        //Single room status listners
        view.addSingleVacantListener(e -> updateRoomStatus("Single", "Vacant"));
        view.addSingleOccupiedListener(e -> updateRoomStatus("Single", "Occupied"));
        view.addSingleOutOfOrderListener(e -> updateRoomStatus("Single", "Out of Order"));

        //Double room status listeners
        view.addDoubleVacantListener(e -> updateRoomStatus("Double", "Vacant"));
        view.addDoubleOccupiedListener(e -> updateRoomStatus("Double", "Occupied"));
        view.addDoubleOutOfOrderListener(e -> updateRoomStatus("Double", "Out of Order"));
        
        //Deluxe room status listeners
        view.addDeluxeVacantListener(e -> updateRoomStatus("Deluxe", "Vacant"));
        view.addDeluxeOccupiedListener(e -> updateRoomStatus("Deluxe", "Occupied"));
        view.addDeluxeOutOfOrderListener(e -> updateRoomStatus("Deluxe", "Out of Order"));

        //Suite room status listeners
        view.addSuiteVacantListener(e -> updateRoomStatus("Suite", "Vacant"));
        view.addSuiteOccupiedListener(e -> updateRoomStatus("Suite", "Occupied"));
        view.addSuiteOutOfOrderListener(e -> updateRoomStatus("Suite", "Out of Order"));

        //Admin dashboard navigation listeners
        view.addLogoutListener(e -> logout());
        view.addAdminClientButtonListener(e -> openAdminClientView());
        
        view.addDashboardRefreshListener(e -> {
            loadInitialRoomStatuses();
            JOptionPane.showMessageDialog(view, "Dashboard has been refreshed.", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        
        view.addBookingHistoryListener(e -> {
            JOptionPane.showMessageDialog(view, "Booking History feature will be added later!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * A single, reusable method to handle all status update logic.
     * @param roomType The type of room to update (e.g., "Single")
     * @param newStatus The new status to set (e.g., "Vacant")
     */
    private void updateRoomStatus(String roomType, String newStatus) {
        System.out.println("Admin is setting " + roomType + " room status to: " + newStatus);

        // Update the correct label on the UI
        switch (roomType) {
            case "Single":
                view.setSingleRoomStatus(newStatus);
                break;
            case "Double":
                view.setDoubleRoomStatus(newStatus);
                break;
            case "Deluxe":
                view.setDeluxeRoomStatus(newStatus);
                break;
            case "Suite":
                view.setSuiteRoomStatus(newStatus);
                break;
        }

        // TODO: In the future, you will save this change to your database here.
        // E.g., roomStatusDao.updateStatus(roomType, newStatus);
        
        JOptionPane.showMessageDialog(view, roomType + " room status has been set to '" + newStatus + "'");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose(); // Close the admin dashboard
            
            // Re-open the login screen
            LoginView loginView = new LoginView();
            UserDao userDao = new UserDao();
            new LoginController(loginView, userDao).open();
        }
    }
    private void openAdminClientView() {
    AdminClientView adminClientView = new AdminClientView();
    AdminClientController adminClientController = new AdminClientController(adminClientView, this);
    view.setVisible(false); 
    adminClientController.open();
}
    /**
     * Makes the admin dashboard visible.
     */
    public void showView() {
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }
}
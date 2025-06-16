package checkinn.controller;

import checkinn.dao.UserDao;
import checkinn.model.Room; 
import checkinn.model.UserData;
import checkinn.view.BookingHistoryView;
import checkinn.view.DashboardView;
import checkinn.view.LoginView;
import checkinn.view.RoomDetailsView;
import checkinn.view.UserProfileView;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class DashboardController {
    private final DashboardView dashboardView;
    private final String userEmail;
    private UserData user;

    public DashboardController(DashboardView dashboardView, String userEmail) {
        this.dashboardView = dashboardView;
        this.userEmail = userEmail;
        loadUserData();
        initialize();
    }

    private void loadUserData() {
        UserDao userDao = new UserDao();
        this.user = userDao.getUserByEmail(userEmail);
    }

    private void initialize() {
        // Set user name and welcome message on dashboard if available
        if (user != null && user.getFirstName() != null) {
            dashboardView.setUserName(user.getFirstName());
            dashboardView.setWelcomeMessage("Welcome, " + user.getFirstName() + "!");
        } else {
            dashboardView.setUserName("Guest");
            dashboardView.setWelcomeMessage("Welcome, Guest!");
        }

        // --- EDITED SECTION: Create Room objects to hold all data ---
        Room singleRoom = new Room(1,"Single Room", 2000.00, "Perfect for the solo traveler, our Single Room offers a peaceful and efficient space to unwind. It features a comfortable single bed, a dedicated work desk, and modern amenities to ensure a productive and restful stay. The room provides a quiet sanctuary, ideal for relaxing after a busy day of exploring the city or attending meetings. Enjoy a blend of comfort and convenience tailored just for you..", "/images/singleroom.jpg");
        Room doubleRoom = new Room(2,"Double Room", 3000.00, "\"Ideal for couples or friends traveling together, our Double Room provides ample space and comfort. It is furnished with a plush double bed and a cozy seating area, creating a warm and inviting atmosphere. The room is equipped with all the essential amenities needed for a memorable stay. Whether you're starting your day with a fresh coffee or winding down in the evening, this room is your perfect home away from home..", "/images/doubleroom.jpg");
        Room deluxeRoom = new Room(3,"Deluxe Room", 5000.00, "Indulge in an elevated experience in our spacious Deluxe Room, where modern luxury meets exceptional comfort. This room features premium furnishings, a king-sized bed, and large windows offering stunning city views. The elegant decor and enhanced amenities, including a minibar and plush bathrobes, create a truly sophisticated environment. It's the perfect choice for guests seeking an extra touch of class and a more memorable, relaxing stay.", "/images/Deluxe.jpg");
        Room suiteRoom = new Room(4, "Executive Suite", 8000.00, "Experience the pinnacle of luxury and sophistication in our Executive Suite. This expansive suite offers a private world of comfort, featuring a separate living area for entertaining or relaxing, and a master bedroom with a luxurious king sized bed. Enjoy exclusive access to premium services, state-of-the-art entertainment systems, and breathtaking panoramic views. The suite is meticulously designed for discerning guests who demand the utmost in space, privacy, and elegance..", "/images/executive room.jpg");
        
        // --- EDITED SECTION: Assign listeners using the Room objects ---
        dashboardView.addSingleRoomListener((e) -> openRoomDetailsPage(singleRoom));
        dashboardView.addDoubleRoomListener((e) -> openRoomDetailsPage(doubleRoom));
        dashboardView.addDeluxeRoomListener((e) -> openRoomDetailsPage(deluxeRoom));
        dashboardView.addSuiteRoomListener((e) -> openRoomDetailsPage(suiteRoom));


dashboardView.addBookingHistoryListener(e -> openBookingHistoryView());

        dashboardView.addDashboardListener((ActionEvent e) -> {
            refreshDashboard();
        });
        
        dashboardView.addUserProfileRedirectionListener((ActionEvent e) -> {
        openUserProfileView();
    });

        dashboardView.addLogoutListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                dashboardView,
                "Are you sure you want to logout?",
                "Confirm Logout | CheckInn",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                close();
                JOptionPane.showMessageDialog(null, "Logged out successfully", "Logout | CheckInn", JOptionPane.INFORMATION_MESSAGE);
                LoginView loginView = new LoginView();
                UserDao userDao = new UserDao();
                LoginController loginController = new LoginController(loginView, userDao);
                loginController.open();
            }
        });
    }

    private void openRoomDetailsPage(Room room) {
    RoomDetailsView roomDetailsView = new RoomDetailsView();
    RoomDetailsController roomDetailsController = new RoomDetailsController(
        roomDetailsView,
        dashboardView,
        user,
        room
    );
    dashboardView.setVisible(false); // Hide dashboard
    roomDetailsController.open();
}
    
private void openUserProfileView() {
    UserProfileView userProfileView = new UserProfileView(user);
    UserProfileController userProfileController = new UserProfileController(userProfileView, this, user);
    userProfileView.setParentController(this);
    dashboardView.setVisible(false);
    userProfileController.open();
}

private void openBookingHistoryView() {
    BookingHistoryView bookingHistoryView = new BookingHistoryView();
    BookingHistoryController bookingHistoryController = new BookingHistoryController(bookingHistoryView, user, this);
    bookingHistoryController.open();
    dashboardView.setVisible(false);
}

    private void refreshDashboard() {
        if (user != null && user.getFirstName() != null) {
            dashboardView.setUserName(user.getFirstName());
        } else {
            dashboardView.setUserName("Guest");
        }
        JOptionPane.showMessageDialog(
            dashboardView,
            "Dashboard refreshed",
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void open() {
        dashboardView.setVisible(true);
    }

    public void close() {
        dashboardView.dispose();
    }
}
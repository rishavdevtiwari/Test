package checkinn.controller;

import checkinn.dao.BookingDao;
import checkinn.dao.InvoiceDao;
import checkinn.dao.MenuItemDao;
import checkinn.model.Booking;
import checkinn.model.Invoice;
import checkinn.model.MenuItem;
import checkinn.model.UserData;
import checkinn.view.BookingHistoryView;
import checkinn.view.InvoiceView;
import checkinn.view.UserProfileView;

import java.util.*;

public class BookingHistoryController {
    private final BookingHistoryView view;
    private final UserData user;
    private final DashboardController dashboardController;
    private Map<String, List<Booking>> roomBookings;

    public BookingHistoryController(BookingHistoryView view, UserData user, DashboardController dashboardController) {
        this.view = view;
        this.user = user;
        this.dashboardController = dashboardController;
        loadBookingHistory();
        initializeListeners();
    }

    private String getRoomKey(int roomId, Date checkInDate, Date checkOutDate) {
        return roomId + "|" + checkInDate.getTime() + "|" + checkOutDate.getTime();
    }

    private void loadBookingHistory() {
        BookingDao bookingDao = new BookingDao();
        List<Booking> bookings = bookingDao.getBookingsForUser(user.getUserId());
        roomBookings = new HashMap<>();
        for (Booking booking : bookings) {
            String key = getRoomKey(booking.getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate());
            roomBookings.computeIfAbsent(key, k -> new ArrayList<>()).add(booking);
        }
        for (int roomId = 1; roomId <= 4; roomId++) {
            Booking latest = null;
            String latestKey = null;
            for (Map.Entry<String, List<Booking>> entry : roomBookings.entrySet()) {
                List<Booking> group = entry.getValue();
                if (!group.isEmpty() && group.get(0).getRoomId() == roomId) {
                    if (latest == null || group.get(0).getCheckInDate().after(latest.getCheckInDate())) {
                        latest = group.get(0);
                        latestKey = entry.getKey();
                    }
                }
            }
            if (latest != null && latestKey != null) {
                view.setRoomPanelData(roomId, latest.getCheckInDate(), latest.getCheckOutDate(), true);
            } else {
                view.setRoomPanelData(roomId, null, null, false);
            }
        }
    }

private void openInvoiceForRoom(int roomId) {
  
    Date checkIn = null;
    Date checkOut = null;
    switch (roomId) {
        case 1:
            checkIn = view.getCheckInDateForRoom(1);
            checkOut = view.getCheckOutDateForRoom(1);
            break;
        case 2:
            checkIn = view.getCheckInDateForRoom(2);
            checkOut = view.getCheckOutDateForRoom(2);
            break;
        case 3:
            checkIn = view.getCheckInDateForRoom(3);
            checkOut = view.getCheckOutDateForRoom(3);
            break;
        case 4:
            checkIn = view.getCheckInDateForRoom(4);
            checkOut = view.getCheckOutDateForRoom(4);
            break;
    }
    if (checkIn == null || checkOut == null) return;

    BookingDao bookingDao = new BookingDao();
    List<Booking> bookings = bookingDao.getBookingsForUserRoomAndDates(
        user.getUserId(), roomId, checkIn, checkOut
    );

    if (bookings == null || bookings.isEmpty()) return;

    MenuItemDao menuItemDao = new MenuItemDao();
    List<MenuItem> menuItems = new ArrayList<>();
    for (Booking b : bookings) {
        MenuItem item = menuItemDao.getMenuItemById(b.getMenuId());
        if (item != null) menuItems.add(item);
    }

    Booking booking = bookings.get(0);
    InvoiceDao invoiceDao = new InvoiceDao();
    Invoice invoice = invoiceDao.getInvoiceByBookingId(booking.getBookingId());
    if (invoice != null) {
        String roomName = getRoomNameById(roomId);
        InvoiceView invoiceView = new InvoiceView();
        invoiceView.setParentController(this);
        invoiceView.setInvoiceData(
            roomName,
            user.getFirstName() + " " + user.getLastName(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            menuItems,
            invoice.getTotalAmount()
        );
        InvoiceController invoiceController = new InvoiceController(invoiceView);
        invoiceController.open();
    }
}

    private String getRoomNameById(int roomId) {
        return switch (roomId) {
            case 1 -> "Single Room";
            case 2 -> "Double Room";
            case 3 -> "Deluxe Room";
            case 4 -> "Executive Suite";
            default -> "Unknown Room";
        };
    }

    private void initializeListeners() {
        view.addBackToDashboardListener(e -> {
            view.dispose();
            dashboardController.open();
        });

view.addUserProfileRedirectionListener(e -> {
    view.dispose();
    UserProfileView userProfileView = new UserProfileView(user);
    UserProfileController userProfileController = new UserProfileController(userProfileView, dashboardController, user);
    userProfileView.setParentController(this);
    userProfileController.open();
});

        view.addLogoutListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to logout?",
                "Confirm Logout | CheckInn",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                view.dispose();
                javax.swing.JOptionPane.showMessageDialog(null, "Logged out successfully", "Logout | CheckInn", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                checkinn.view.LoginView loginView = new checkinn.view.LoginView();
                checkinn.dao.UserDao userDao = new checkinn.dao.UserDao();
                checkinn.controller.LoginController loginController = new checkinn.controller.LoginController(loginView, userDao);
                loginController.open();
            }
        });

        view.addRoomInvoiceButtonListener(1, e -> openInvoiceForRoom(1));
        view.addRoomInvoiceButtonListener(2, e -> openInvoiceForRoom(2));
        view.addRoomInvoiceButtonListener(3, e -> openInvoiceForRoom(3));
        view.addRoomInvoiceButtonListener(4, e -> openInvoiceForRoom(4));
    }

    public void open() {
        loadBookingHistory();
            view.setUserNameLabel(user.getFirstName() + " " + user.getLastName());
        view.setLocationRelativeTo(null);
        view.setVisible(true);

            view.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
            dashboardController.open();
        }
    });
    }

    public void close() {
        view.dispose();
    }

}
package checkinn.controller;

import checkinn.view.InvoiceView;

public class InvoiceController {
    private final InvoiceView invoiceView;

    public InvoiceController(InvoiceView invoiceView) {
        this.invoiceView = invoiceView;
        initializeListeners();
    }

    private void initializeListeners() {
        invoiceView.addExitInvoiceListener(e -> {
            invoiceView.dispose();
Object parent = invoiceView.getParentController();
            if (parent instanceof DashboardController dashboardController) {
                dashboardController.open();
            } else if (parent instanceof BookingHistoryController bookingHistoryController) {
                bookingHistoryController.open();
            } else if (parent instanceof BookingFormController bookingFormController) {
                bookingFormController.open();
            }
        });
    }

    public void open() {
        invoiceView.setLocationRelativeTo(null);
        invoiceView.setVisible(true);
    }

    public void close() {
        invoiceView.dispose();
    }
}
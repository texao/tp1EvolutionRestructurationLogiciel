package tp2hai913.tp2hai913.classAanalyser;

public class Order {
    private int orderId;
    private String customerName;
    private double totalPrice;
    private Payment payment;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = 0.0;
    }

    public void addProduct(Product product, int quantity) {
        double productPrice = product.getPrice() * quantity;
        this.totalPrice += productPrice;
        System.out.println("Added " + quantity + " of " + product.getName() + " to the order.");
    }

    public void removeProduct(Product product, int quantity) {
        double productPrice = product.getPrice() * quantity;
        if (this.totalPrice >= productPrice) {
            this.totalPrice -= productPrice;
            System.out.println("Removed " + quantity + " of " + product.getName() + " from the order.");
        } else {
            System.out.println("Error: Unable to remove product, price exceeds total.");
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void processPayment(Payment payment) {
        if (payment.validate()) {
            this.payment = payment;
            payment.deductAmount(this.totalPrice);
            System.out.println("Payment of " + this.totalPrice + " processed for order " + this.orderId);
        } else {
            System.out.println("Invalid payment. Order could not be processed.");
        }
    }

    public void printOrderDetails() {
        System.out.println("Order ID: " + this.orderId);
        System.out.println("Customer: " + this.customerName);
        System.out.println("Total Price: " + this.totalPrice);
        if (this.payment != null) {
            System.out.println("Payment: " + this.payment.getPaymentDetails());
        } else {
            System.out.println("No payment processed yet.");
        }
    }

    // Nouvelle méthode pour demander un remboursement
    public void requestRefund(double amount) {
        if (payment != null) {
            if (payment.refund(amount)) {
                System.out.println("Refund of " + amount + " has been processed for order " + orderId);
                this.totalPrice -= amount;
            } else {
                System.out.println("Refund failed for order " + orderId);
            }
        } else {
            System.out.println("No payment found for this order. Cannot process refund.");
        }
    }
}

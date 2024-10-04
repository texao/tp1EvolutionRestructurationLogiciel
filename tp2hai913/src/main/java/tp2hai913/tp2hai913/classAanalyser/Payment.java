package tp2hai913.tp2hai913.classAanalyser;

public class Payment {
    private String paymentMethod;
    private double balance;
    private boolean valid;

    public Payment(String paymentMethod, double initialBalance) {
        this.paymentMethod = paymentMethod;
        this.balance = initialBalance;
        this.valid = initialBalance > 0;
    }

    public boolean validate() {
        if (this.balance > 0) {
            System.out.println("Payment method: " + this.paymentMethod + " is valid.");
            this.valid = true;
        } else {
            System.out.println("Payment method: " + this.paymentMethod + " is invalid.");
            this.valid = false;
        }
        return this.valid;
    }

    public void deductAmount(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            System.out.println("Deducted " + amount + " from balance. Remaining balance: " + this.balance);
        } else {
            System.out.println("Insufficient balance for this payment.");
        }
    }

    public String getPaymentDetails() {
        return "Method: " + this.paymentMethod + ", Balance: " + this.balance;
    }

    public void addFunds(double amount) {
        this.balance += amount;
        System.out.println(amount + " added to the payment. New balance: " + this.balance);
    }

    // Nouvelle méthode pour traiter un remboursement
    public boolean refund(double amount) {
        if (amount <= this.balance) {
            this.balance += amount;
            System.out.println("Refunded " + amount + " to balance. New balance: " + this.balance);
            return true;
        } else {
            System.out.println("Refund amount exceeds balance. Refund failed.");
            return false;
        }
    }

    // Nouvelle méthode pour vérifier le solde avant de traiter un paiement
    public void checkBalanceBeforePayment(Order order) {
        if (this.balance < order.getTotalPrice()) {
            System.out.println("Insufficient balance for processing payment of order " + order.getTotalPrice());
        } else {
            System.out.println("Sufficient balance for processing payment of order " + order.getTotalPrice());
        }
    }
}

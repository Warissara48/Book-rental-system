package customer;

public class PremiumCustomer extends Customer {
    private double discountRate;

    public PremiumCustomer(String name, String email, String phoneNumber, double discountRate) {
        super(name, email, phoneNumber);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public String toString() {
        return super.toString() + ", Discount Rate: " + discountRate;
    }

    public double applyDiscount(double originalFee) {
        return originalFee * (1 - discountRate);
    }
}
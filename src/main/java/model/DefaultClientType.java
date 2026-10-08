package model;

public class DefaultClientType extends ClientType {
    @Override
    public double applyDiscount(double price) {
        return price;
    }

    @Override
    public String getInfo() {
        return "Domyślny typ klienta";
    }
}

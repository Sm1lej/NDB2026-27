package model;

public class GoldClientType extends ClientType {

    @Override
    public double applyDiscount(double price) {
        return price * 0.90;
    }

    @Override
    public String getInfo() {
        return "Złoty typ klienta";
    }
}

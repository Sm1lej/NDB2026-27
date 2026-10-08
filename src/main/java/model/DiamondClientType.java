package model;

public class DiamondClientType extends ClientType {
    @Override
    public double applyDiscount(double price) {
        return price * 0.75;
    }

    @Override
    public String getInfo() {
        return "Diamentowy typ klienta";
    }
}

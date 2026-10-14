package model;

public class Address {
    private String city;
    private String street;
    private String number;

    public Address(String city, String street, String number) {
        if (city.isEmpty() || street.isEmpty() || number.isEmpty()) {
            throw new RuntimeException("Puste wartości");
        }

        if (city.length() > 50 || street.length() > 50 || number.length() > 50) {
            throw new RuntimeException("Za długie wartości");
        }

        this.city = city;
        this.street = street;
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        if (city.isEmpty()) {
            throw new RuntimeException("Pusta wartość");
        }

        if (city.length() > 50) {
            throw new RuntimeException("Za długie wartości");
        }

        this.city = city;
    }

    public void setStreet(String street) {
        if (street.isEmpty()) {
            throw new RuntimeException("Pusta wartość");
        }

        if (street.length() > 50) {
            throw new RuntimeException("Za długie wartości");
        }

        this.street = street;
    }

    public void setNumber(String number) {
        if (number.isEmpty()) {
            throw new RuntimeException("Pusta wartość");
        }

        if (number.length() > 50) {
            throw new RuntimeException("Za długie wartości");
        }

        this.number = number;
    }

    public String getInfo() {
       StringBuilder sb = new StringBuilder();
       sb.append("Miasto: ").append(getCity())
               .append(" Ulica: ").append(getStreet())
               .append(" Numer: ").append(getNumber());

       return sb.toString();
    }
}

package model;

import java.util.ArrayList;

public class Client {
    private String name;
    private String lastName;
    private String pesel;
    private String email;
    private String phoneNumber;
    private Address address;
    private ClientType clientType;
    //private ArrayList<Vehicle> vehicles;
    private boolean isArchive = false;

    public Client(String name, String lastName, String pesel, String email,
                  String phoneNumber, Address address) {
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || pesel.isEmpty())
            throw new RuntimeException("Pole nie może być puste");

        if (name.length() > 40)
            throw new RuntimeException("Imię przekracza maksymalną długość 40 znaków");

        if (lastName.length() > 40)
            throw new RuntimeException("Nazwisko przekracza maksymalną długość 40 znaków");

        if (email.length() > 255)
            throw new RuntimeException("Email przekracza maksymalną długość 255 znaków");

        if (!email.contains("@"))
            throw new RuntimeException("Niepoprawny format adresu email (brak znaku @)");

        for (int i = 0; i < pesel.length(); i++) {
            if (!Character.isDigit(pesel.charAt(i)))
                throw new RuntimeException("PESEL zawiera niedozwolone znaki (wymagane tylko cyfry)");
        }
        if (pesel.length() != 11)
            throw new RuntimeException("PESEL musi składać się z dokładnie 11 cyfr");

        if (phoneNumber.length() > 13)
            throw new RuntimeException("Numer telefonu jest za długi");

        if (phoneNumber.charAt(0) != '+' && !Character.isDigit(phoneNumber.charAt(0)))
            throw new RuntimeException("Niepoprawny prefiks numeru telefonu");

        for (int i = 1; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i)))
                throw new RuntimeException("Numer telefonu zawiera niedozwolone znaki");
        }

        this.name = name;
        this.lastName = lastName;
        this.pesel = pesel;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;

        if (this.address == null)
            throw new RuntimeException("Adres nie może być nullem");
        clientType = new DefaultClientType();
    }
    public String getPesel() {
        return pesel;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public Address getAddress() {
        return address;
    }

    public String getInfo() {
        StringBuilder ss = new StringBuilder();
        ss.append("Imię: ").append(getName()).append(", nazwisko: ").append(getLastName()).append("\n")
                .append("Pesel: ").append(getPesel()).append(", Adres email: ").append(getEmail()).append("\n")
                .append("Numer telefonu: ").append(getPhoneNumber()).append(" i jest to ").append(clientType.getInfo()).append("\n")
                .append("Adres zamieszkania: ").append(address.getInfo()).append("\n");

        return ss.toString();
    }

    // // Klasa Vehicle jeszcze nie istnieje
    // public List<Vehicle> getVehicles() {
    //     return vehicles;
    // }


    public void setClientType(ClientType clientType) {
        if (clientType == null)
            throw new RuntimeException("Typ klienta nie może być nullem");
        if (clientType.equals(this.clientType))
            throw new RuntimeException("Próba ustawienia tego samego typu klienta");
        this.clientType = clientType;
    }

    public void setArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            throw new RuntimeException("Numer telefonu nie może być pusty");

        if (phoneNumber.charAt(0) != '+' && !Character.isDigit(phoneNumber.charAt(0)))
            throw new RuntimeException("Niepoprawny prefiks numeru telefonu");

        if (phoneNumber.length() > 13)
            throw new RuntimeException("Numer telefonu jest za długi (max 13 znaków)");

        for (int i = 1; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i)))
                throw new RuntimeException("Numer telefonu zawiera niedozwolone znaki");
        }

        if (phoneNumber.equals(this.phoneNumber))
            throw new RuntimeException("Próba nadpisania numeru telefonu tą samą wartością");

        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty() || email.length() > 100)
            throw new RuntimeException("Niepoprawna długość adresu email (max 100 znaków i nie może być pusty)");

        if (!email.contains("@"))
            throw new RuntimeException("Niepoprawny format adresu email (brak znaku @)");

        this.email = email;
    }

    public void setAddress(Address address) {
        if (address == null)
            throw new RuntimeException("Adres nie może być nullem");
        if (address.equals(this.address))
            throw new RuntimeException("Próba nadpisania adresu tą samą wartością");

        this.address = address;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty() || name.length() > 40)
            throw new RuntimeException("Niepoprawna długość imienia (max 40 znaków i nie może być puste)");
        if (name.equals(this.name))
            throw new RuntimeException("Próba nadpisania imienia tą samą wartością");

        this.name = name;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty() || lastName.length() > 40)
            throw new RuntimeException("Niepoprawna długość nazwiska (max 40 znaków i nie może być puste)");
        if (lastName.equals(this.lastName))
            throw new RuntimeException("Próba nadpisania nazwiska tą samą wartością");

        this.lastName = lastName;
    }


    // public void addVehicle(Vehicle vehicle) {
    //     if (vehicle == null)
    //         throw new RuntimeException("Pojazd nie może być nullem");

    //     this.vehicles.add(vehicle);
    // }


//    public void removeVehicle(Vehicle vehicle) {
//        if (vehicle == null) {
//            throw new RuntimeException("Pojazd nie może być nullem");
//        }
//
//        // Metoda List.remove(Object) zwraca 'true', jeśli obiekt był na liście i został usunięty.
//        // Jeśli go nie było, zwraca 'false', więc możemy od razu rzucić wyjątek.
//        boolean removed = this.vehicles.remove(vehicle);
//
//        if (!removed) {
//            throw new RuntimeException("Nie znaleziono pojazdu u tego klienta");
//        }
//    }

}

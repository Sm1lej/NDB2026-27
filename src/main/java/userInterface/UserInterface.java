package userInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import managers.ClientManager;
import managers.ServiceManager;
import managers.VehicleManager;
import managers.AppointmentManager;
import model.*;

/**
 * Klasa interfejsu użytkownika obsługująca interakcję z konsolą.
 */
public class UserInterface {
    private ClientManager clientManager;
    private VehicleManager vehicleManager;
    private ServiceManager serviceManager;
    private AppointmentManager appointmentManager;
    private Scanner scanner;

    public UserInterface(ClientManager clientManager,
                         VehicleManager vehicleManager,
                         ServiceManager serviceManager,
                         AppointmentManager appointmentManager) {
        this.clientManager = clientManager;
        this.vehicleManager = vehicleManager;
        this.serviceManager = serviceManager;
        this.appointmentManager = appointmentManager;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        String input;
        int choice = 7;
        do {
            displayMainMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch(choice) {
                case 1: handleClientOperations(); break;
                case 2: handleVehicleOperations(); break;
                case 3: handleServiceOperations(); break;
                case 4: handleAppointmentOperations(); break;
                case 0: System.out.println("Zamykanie aplikacji...\n"); break;
                default: System.out.println("Nieprawidłowy wybór!\n");
            }
        } while(choice != 0);
    }

    public static boolean isValidPesel(String pesel) {
        if(pesel.length() != 11) {
            System.out.println("PESEL musi miec 11 cyfr.\n");
            return false;
        }
        for(char c : pesel.toCharArray()) {
            if(!Character.isDigit(c)) {
                System.out.println("PESEL może zawierać tylko cyfry.\n");
                return false;
            }
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        if(!email.contains("@") || !email.contains(".")) {
            System.out.println("Nieprawidłowy adres email. Musi zawierać znak '@' oraz domenę.\n");
            return false;
        }
        return true;
    }

    public boolean isValidPhoneNumber(String phone) {
        if(phone.isEmpty()) {
            System.out.println("Numer telefonu nie może być pusty.\n");
            return false;
        }
        for(char c : phone.toCharArray()) {
            if(!Character.isDigit(c) && c != '+' && c != '-' && c != ' ') {
                System.out.println("Numer telefonu zawiera niedozwolone znaki.\n");
                return false;
            }
        }
        return true;
    }

    public boolean isPositiveNumber(String input) {
        if(input.isEmpty()) {
            System.out.println("Pole nie może byc puste.\n");
            return false;
        }
        for(char c : input.toCharArray()) {
            if(!Character.isDigit(c)) {
                System.out.println("Wartość musi być liczbą dodatnią.\n");
                return false;
            }
        }
        return true;
    }

    public boolean isPositiveDouble(String input) {
        if(input.isEmpty()) {
            System.out.println("Pole nie może być puste.\n");
            return false;
        }
        boolean dotFound = false;
        for(char c : input.toCharArray()) {
            if(!Character.isDigit(c)) {
                if(c == '.' && !dotFound) {
                    dotFound = true;
                } else {
                    System.out.println("Wartość musi byc liczbą dodatnią.\n");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch(DateTimeParseException e) {
            System.out.println("Nieprawidłowy format daty. Użyj formatu RRRR-MM-DD.\n");
            return false;
        }
    }

    public boolean isValidTime(String timeStr) {
        try {
            LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("H:mm"));
            return true;
        } catch(DateTimeParseException e) {
            System.out.println("Nieprawidłowy format czasu. Użyj formatu GG:MM.\n");
            return false;
        }
    }

    public boolean isValidDateTime(String dateTimeStr) {
        try {
            LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return true;
        } catch(DateTimeParseException e) {
            System.out.println("Nieprawidłowy format daty i czasu. Użyj formatu RRRR-MM-DD GG:MM:SS.\n");
            return false;
        }
    }

    public void handleClientOperations() {
        String input;
        int choice = 7;
        do {
            displayClientMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch(choice) {
                case 1: addClient(); break;
                case 2: findClient(); break;
                case 3: removeClient(); break;
                case 4: listAllClients(); break;
                case 5: changeClientType(); break;
                case 0: break;
                default: System.out.println("Nieprawidłowy wybór!\n");
            }
        } while(choice != 0);
    }

    public void addClient() {
        String name = "", lastName = "", email = "", phoneNumber = "", pesel = "", clientType = "";
        System.out.println("\n--- Dodawanie nowego klienta ---\n");

        // Walidacja w locie dla imienia
        do {
            try {
                System.out.println("Podaj imię: ");
                name = scanner.nextLine();
                if (name.isEmpty()) {
                    throw new RuntimeException("Pole nie może być puste");
                }
                if (name.length() > 40) {
                    throw new RuntimeException("Imię przekracza maksymalną długość 40 znaków");
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Walidacja w locie dla nazwiska
        do {
            try {
                System.out.println("Podaj nazwisko: ");
                lastName = scanner.nextLine();
                if (lastName.isEmpty()) {
                    throw new RuntimeException("Pole nie może być puste");
                }
                if (lastName.length() > 40) {
                    throw new RuntimeException("Nazwisko przekracza maksymalną długość 40 znaków");
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Walidacja w locie dla adresu email
        do {
            try {
                System.out.println("Podaj adres email: ");
                email = scanner.nextLine();
                if (email.isEmpty() || email.length() > 255) {
                    throw new RuntimeException("Niepoprawna długość adresu email (max 255 znaków i nie może być pusty)");
                }
                if (!email.contains("@")) {
                    throw new RuntimeException("Niepoprawny format adresu email (brak znaku @)");
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Walidacja w locie dla numeru telefonu
        do {
            try {
                System.out.println("Podaj numer telefonu: ");
                phoneNumber = scanner.nextLine();
                if (phoneNumber.isEmpty()) {
                    throw new RuntimeException("Numer telefonu nie może być pusty");
                }
                if (phoneNumber.length() > 13) {
                    throw new RuntimeException("Numer telefonu jest za długi");
                }
                if (phoneNumber.charAt(0) != '+' && !Character.isDigit(phoneNumber.charAt(0))) {
                    throw new RuntimeException("Niepoprawny prefiks numeru telefonu");
                }
                for (int i = 1; i < phoneNumber.length(); i++) {
                    if (!Character.isDigit(phoneNumber.charAt(i))) {
                        throw new RuntimeException("Numer telefonu zawiera niedozwolone znaki");
                    }
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Walidacja w locie dla numeru PESEL
        do {
            try {
                System.out.println("Podaj PESEL (11 cyfr): ");
                pesel = scanner.nextLine();
                if (pesel.isEmpty()) {
                    throw new RuntimeException("PESEL nie może być pusty");
                }
                if (pesel.length() != 11) {
                    throw new RuntimeException("PESEL musi składać się z dokładnie 11 cyfr");
                }
                for (int i = 0; i < pesel.length(); i++) {
                    if (!Character.isDigit(pesel.charAt(i))) {
                        throw new RuntimeException("PESEL zawiera niedozwolone znaki (wymagane tylko cyfry)");
                    }
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Walidacja typu klienta
        do {
            try {
                System.out.println("Podaj typ klienta (Default | Gold | Diamond): ");
                clientType = scanner.nextLine();
                if (clientType.isEmpty()) {
                    throw new RuntimeException("Pole nie może być puste");
                }
                if (!clientType.equals("Default") && !clientType.equals("Gold") && !clientType.equals("Diamond")) {
                    throw new RuntimeException("Nieprawidłowy typ klienta!");
                }
                break;
            } catch (RuntimeException e) {
                System.err.println("Błąd: " + e.getMessage() + "\nSpróbuj ponownie.\n");
            }
        } while (true);

        // Końcowa rejestracja klienta po pomyślnym zweryfikowaniu wszystkich pól w locie
        try {
            clientManager.registerClient(name, lastName, pesel, email, phoneNumber, clientType);
            System.out.println("Klient został dodany pomyślnie.\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void findClient() {
        String input;
        int choice = -1;
        do {
            displayClientFindMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }
            switch(choice) {
                case 1: {
                    String imie, nazwisko;
                    do {
                        System.out.println("Podaj imię klienta: ");
                        imie = scanner.nextLine();
                        if(imie.isEmpty()) System.out.println("Imię nie może być puste.\n");
                    } while (imie.isEmpty());

                    do {
                        System.out.println("Podaj nazwisko klienta: ");
                        nazwisko = scanner.nextLine();
                        if(nazwisko.isEmpty()) System.out.println("Nazwisko nie może być puste.\n");
                    } while(nazwisko.isEmpty());

                    List<Client> found = clientManager.findByNameAndLastName(imie, nazwisko);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klientów o podanym imieniu i nazwisku\n");
                        break;
                    }
                    System.out.println("Klienci o imieniu: " + imie + " i nazwisku " + nazwisko + "\n");
                    int i = 1;
                    for (Client client : found) {
                        if (client != null) {
                            System.out.println(i + ". " + client.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }
                case 2: {
                    String phoneNumber;
                    do {
                        System.out.println("Podaj numer telefonu klienta: ");
                        phoneNumber = scanner.nextLine();
                    } while (!isValidPhoneNumber(phoneNumber));

                    List<Client> found = clientManager.findByPhoneNumber(phoneNumber);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klientów o podanym numerze telefonu\n");
                        break;
                    }
                    System.out.println("Klienci o numerze telefonu: " + phoneNumber + " :\n");
                    int i = 1;
                    for (Client client : found) {
                        if (client != null) {
                            System.out.println(i + ". " + client.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }
                case 3: {
                    String pesel;
                    do {
                        System.out.println("Podaj pesel klienta (11cyfr): ");
                        pesel = scanner.nextLine();
                    } while (!isValidPesel(pesel));

                    Optional<Client> found = clientManager.findByPesel(pesel);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klienta\n");
                        break;
                    }
                    Client client = found.get();
                    System.out.println("Klient o podanym numerze pesel: " + pesel + " :\n");
                    System.out.println(client.getInfo() + '\n');
                    break;
                }
                case 4: {

                    List<Client> found = clientManager.findByArchive();
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono archiwalnych klientów\n");
                        break;
                    }
                    System.out.println("Archiwalni klienci :\n");
                    int i = 1;
                    for (Client client : found) {
                        if (client != null) {
                            System.out.println(i + ". " + client.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }

                case 5:
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór!\n");
                    break;
            }
        } while (choice != 5);
    }

    public void removeClient() {
        String pesel;
        do {
            System.out.println("Podaj PESEL klienta do usunięcia: ");
            pesel = scanner.nextLine();
        } while(!isValidPesel(pesel));

        Optional<Client> clients = clientManager.findByPesel(pesel);
        if(clients.isEmpty()) {
            System.out.println("Nie znaleziono klienta.\n");
            return;
        }

        try {
            clientManager.unregisterClient(pesel);
            System.out.println("Klient został zarchiwizowany.\n");
        } catch(RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void listAllClients() {
        List<Client> clients = clientManager.findAll();
        if(clients.isEmpty()) {
            System.out.println("Brak aktywnych klientów.\n");
            return;
        }

        System.out.println("Klienci:\n");
        for(Client client : clients) {
            if (client.isArchive())
                continue;
            System.out.println(client.getInfo() + "\n");
        }
    }

    public void changeClientType() {
        String input;
        int choice = 11;
        do {
            displayClientTypeMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch (choice) {
                case 1: {
                    String pesel;
                    do {
                        System.out.println("Podaj pesel klienta, dla którego ustawiamy domyślny typ (11cyfr): ");
                        pesel = scanner.nextLine();
                    } while (!isValidPesel(pesel));

                    Optional<Client> found = clientManager.findByPesel(pesel);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klienta\n");
                        break;
                    }

                    clientManager.changeClientType(pesel, "Default");
                    System.out.println("Domyślny typ klienta ustawiony pomyślnie\n");
                    break;
                }
                case 2: {
                    String pesel;
                    do {
                        System.out.println("Podaj pesel klienta, dla którego ustawiamy złoty typ (11cyfr): ");
                        pesel = scanner.nextLine();
                    } while (!isValidPesel(pesel));

                    Optional<Client> found = clientManager.findByPesel(pesel);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klienta\n");
                        break;
                    }

                    clientManager.changeClientType(pesel, "Gold");
                    System.out.println("Złoty typ klienta ustawiony pomyślnie\n");
                    break;
                }
                case 3: {
                    String pesel;
                    do {
                        System.out.println("Podaj pesel klienta, dla którego ustawiamy typ (11cyfr): ");
                        pesel = scanner.nextLine();
                    } while (!isValidPesel(pesel));

                    Optional<Client> found = clientManager.findByPesel(pesel);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono klienta\n");
                        break;
                    }

                    clientManager.changeClientType(pesel, "Diamond");
                    System.out.println("Diamentowy typ klienta ustawiony pomyślnie\n");
                    break;
                }
                case 4: break;
                default:
                    System.out.println("Wybierz poprawną opcję\n");
                    break;
            }
        } while (choice != 4);
    }

    public void handleVehicleOperations() {
        String input;
        int choice = -1;
        do {
            displayVehicleMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch(choice) {
                case 1: registerCar(); break;
                case 2: registerMotorbike(); break;
                case 3: findVehicle(); break;
                case 4: removeVehicle(); break;
                case 0: break;
                default: System.out.println("Nieprawidłowy wybór!\n");
            }
        } while(choice != 0);
    }

    public void registerCar() {
        String ownerPesel, vin, regNum, brand, model, color, bodyType, fuelType;
        String yearStr, displacementStr, powerStr, weightStr, doorsStr;
        int year, displacement, power, weight, doors;

        System.out.println("\n--- Rejestracja samochodu ---\n");

        do {
            System.out.println("Podaj PESEL właściciela: ");
            ownerPesel = scanner.nextLine();
        } while(!isValidPesel(ownerPesel));

        do {
            System.out.println("Podaj numer VIN (17 znakow): ");
            vin = scanner.nextLine();
            if(vin.isEmpty()) System.out.println("Numer VIN nie może być pusty.\n");
        } while(vin.isEmpty());

        do {
            System.out.println("Podaj numer rejestracyjny: ");
            regNum = scanner.nextLine();
            if(regNum.isEmpty()) System.out.println("Numer rejestracyjny nie może być pusty.\n");
        } while(regNum.isEmpty());

        do {
            System.out.println("Podaj rok produkcji: ");
            yearStr = scanner.nextLine();
        } while(!isPositiveNumber(yearStr));
        year = Integer.parseInt(yearStr);

        do {
            System.out.println("Podaj markę: ");
            brand = scanner.nextLine();
            if(brand.isEmpty()) System.out.println("Marka nie może być pusta.\n");
        } while(brand.isEmpty());

        do {
            System.out.println("Podaj model: ");
            model = scanner.nextLine();
            if(model.isEmpty()) System.out.println("Model nie może być pusty.\n");
        } while(model.isEmpty());

        do {
            System.out.println("Podaj kolor: ");
            color = scanner.nextLine();
            if(color.isEmpty()) System.out.println("Kolor nie może być pusty.\n");
        } while(color.isEmpty());

        do {
            System.out.println("Podaj pojemność silnika (cm3): ");
            displacementStr = scanner.nextLine();
        } while(!isPositiveNumber(displacementStr));
        displacement = Integer.parseInt(displacementStr);

        do {
            System.out.println("Podaj moc silnika (KM): ");
            powerStr = scanner.nextLine();
        } while(!isPositiveNumber(powerStr));
        power = Integer.parseInt(powerStr);

        do {
            System.out.println("Podaj masę pojazdu (kg): ");
            weightStr = scanner.nextLine();
        } while(!isPositiveNumber(weightStr));
        weight = Integer.parseInt(weightStr);

        do {
            System.out.println("Podaj rodzaj nadwozia: ");
            bodyType = scanner.nextLine();
            if(bodyType.isEmpty()) System.out.println("Rodzaj nadwozia nie może być pusty.\n");
        } while(bodyType.isEmpty());

        do {
            System.out.println("Podaj rodzaj paliwa: ");
            fuelType = scanner.nextLine();
            if(fuelType.isEmpty()) System.out.println("Rodzaj paliwa nie może być pusty.\n");
        } while(fuelType.isEmpty());

        do {
            System.out.println("Podaj liczbę drzwi: ");
            doorsStr = scanner.nextLine();
        } while(!isPositiveNumber(doorsStr));
        doors = Integer.parseInt(doorsStr);

        try {
            vehicleManager.registerCar(ownerPesel, vin, regNum, year, brand, model, color,
                    displacement, power, weight, bodyType, fuelType, doors);
            System.out.println("Samochód zarejestrowany pomyślnie!\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void registerMotorbike() {
        String ownerPesel, vin, regNum, brand, model, color, brakeSystem;
        String yearStr, displacementStr, powerStr, weightStr;
        int year, displacement, power, weight;

        System.out.println("\n--- Rejestracja motocyklu ---\n");

        do {
            System.out.println("Podaj PESEL właściciela: ");
            ownerPesel = scanner.nextLine();
        } while(!isValidPesel(ownerPesel));

        do {
            System.out.println("Podaj numer VIN: ");
            vin = scanner.nextLine();
            if(vin.isEmpty()) System.out.println("Numer VIN nie może być pusty.\n");
        } while(vin.isEmpty());

        do {
            System.out.println("Podaj numer rejestracyjny: ");
            regNum = scanner.nextLine();
            if(regNum.isEmpty()) System.out.println("Numer rejestracyjny nie może być pusty.\n");
        } while(regNum.isEmpty());

        do {
            System.out.println("Podaj rok produkcji: ");
            yearStr = scanner.nextLine();
        } while(!isPositiveNumber(yearStr));
        year = Integer.parseInt(yearStr);

        do {
            System.out.println("Podaj markę: ");
            brand = scanner.nextLine();
            if(brand.isEmpty()) System.out.println("Marka nie może być pusta.\n");
        } while(brand.isEmpty());

        do {
            System.out.println("Podaj model: ");
            model = scanner.nextLine();
            if(model.isEmpty()) System.out.println("Model nie może być pusty.\n");
        } while(model.isEmpty());

        do {
            System.out.println("Podaj kolor: ");
            color = scanner.nextLine();
            if(color.isEmpty()) System.out.println("Kolor nie może być pusty.\n");
        } while(color.isEmpty());

        do {
            System.out.println("Podaj pojemność silnika (cm3): ");
            displacementStr = scanner.nextLine();
        } while(!isPositiveNumber(displacementStr));
        displacement = Integer.parseInt(displacementStr);

        do {
            System.out.println("Podaj moc silnika (KM): ");
            powerStr = scanner.nextLine();
        } while(!isPositiveNumber(powerStr));
        power = Integer.parseInt(powerStr);

        do {
            System.out.println("Podaj masę pojazdu (kg): ");
            weightStr = scanner.nextLine();
        } while(!isPositiveNumber(weightStr));
        weight = Integer.parseInt(weightStr);

        do {
            System.out.println("Podaj rodzaj hamulców: ");
            brakeSystem = scanner.nextLine();
            if(brakeSystem.isEmpty()) System.out.println("Rodzaj hamulców nie może być pusty.\n");
        } while(brakeSystem.isEmpty());

        try {
            vehicleManager.registerMotorBike(ownerPesel, vin, regNum, year, brand, model, color,
                    displacement, power, weight, brakeSystem);
            System.out.println("Motocykl zarejestrowany pomyślnie!\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void findVehicle() {
        String input;
        int choice = -1;
        do {
            displayVehicleFindMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }
            switch(choice) {
                case 1: {
                    String pesel;
                    do {
                        System.out.println("Podaj pesel klienta: ");
                        pesel = scanner.nextLine();
                        if(pesel.isEmpty()) System.out.println("Pesel nie może być pusty.\n");
                    } while (pesel.isEmpty());

                    List<Vehicle> found = vehicleManager.findByClientPesel(pesel);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono pojazdów, z klientem o takim numerze pesel\n");
                        break;
                    }
                    System.out.println("Pojazdy z klientem o takim numerze pesel: " + pesel + " :\n");
                    int i = 1;
                    for (Vehicle vehicle : found) {
                        if (vehicle != null) {
                            System.out.println(i + ". " + vehicle.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }
                case 2:{
                    String vinNumber;
                    do {
                        System.out.println("Podaj numer vin pojazdu: ");
                        vinNumber = scanner.nextLine();
                        if(vinNumber.isEmpty()) System.out.println("Numer VIN nie może być pusty.\n");
                    } while (vinNumber.isEmpty());

                    List<Vehicle> found = vehicleManager.findByVinNumber(vinNumber);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono pojazdu\n");
                        break;
                    }
                    System.out.println("Znaleziony pojazd:\n");
                    for (Vehicle vehicle : found) {
                        if (vehicle != null) {
                            System.out.println(vehicle.getInfo() + '\n');
                        }
                    }
                    break;
                }
                case 3:{
                    String plate;
                    do {
                        System.out.println("Podaj numery tablicy rejestracyjnej: ");
                        plate = scanner.nextLine();
                        if(plate.isEmpty()) System.out.println("Numer rejestracyjny nie może być pusty.\n");
                    } while (plate.isEmpty());

                    List<Vehicle> found = vehicleManager.findByRegistrationNumber(plate);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono pojazdu\n");
                        break;
                    }
                    System.out.println("Znaleziony pojazd:\n");
                    for (Vehicle vehicle : found) {
                        if (vehicle != null) {
                            System.out.println(vehicle.getInfo() + '\n');
                        }
                    }
                    break;
                }
                case 4: {
                    String brand, model;
                    do {
                        System.out.println("Podaj marke pojazdu: ");
                        brand = scanner.nextLine();
                        if(brand.isEmpty()) System.out.println("Marka nie może być pusta.\n");
                    } while (brand.isEmpty());

                    do {
                        System.out.println("Podaj model pojazdu: ");
                        model = scanner.nextLine();
                        if(model.isEmpty()) System.out.println("Model nie może być pusty.\n");
                    } while (model.isEmpty());

                    List<Vehicle> found = vehicleManager.findByBrandAndModel(brand, model);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono pojazdów o powyższej marce i modelu\n");
                        break;
                    }
                    System.out.println("Znalezione pojazdy:\n");
                    int i = 1;
                    for (Vehicle vehicle : found) {
                        if (vehicle != null) {
                            System.out.println(i + ". " + vehicle.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }
                case 5:{
                    String engineDisplacementStr;
                    do {
                        System.out.println("Podaj pojemność silnika pojazdu: ");
                        engineDisplacementStr = scanner.nextLine();
                    } while (!isPositiveNumber(engineDisplacementStr));

                    int engineDisplacement = Integer.parseInt(engineDisplacementStr);

                    List<Vehicle> found = vehicleManager.findByEngineDisplacement(engineDisplacement);
                    if (found.isEmpty()){
                        System.out.println("Nieznaleziono pojazdów, z taką pojemnością silnika\n");
                        break;
                    }
                    System.out.println("Znalezione pojazdy:\n");
                    int i = 1;
                    for (Vehicle vehicle : found) {
                        if (vehicle != null) {
                            System.out.println(i + ". " + vehicle.getInfo() + '\n');
                            i++;
                        }
                    }
                    break;
                }
                case 6: break;
                default:
                    System.out.println("Wybierz poprawną opcję\n");
                    break;
            }
        } while (choice != 6);
    }

    public void removeVehicle() {
        String vin;
        do {
            System.out.println("Podaj VIN pojazdu do usunięcia: ");
            vin = scanner.nextLine();
            if(vin.isEmpty()) System.out.println("Numer VIN nie może być pusty.\n");
        } while(vin.isEmpty());

        List<Vehicle> vehicles = vehicleManager.findByVinNumber(vin);
        if(vehicles.isEmpty()) {
            System.out.println("Nie znaleziono pojazdu.\n");
            return;
        }

        try {
            vehicleManager.unregisterVehicle(vin);
            System.out.println("Pojazd został usunięty.\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void handleServiceOperations() {
        String input;
        int choice = -1;
        do {
            displayServiceMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch(choice) {
                case 1: registerRepairService(); break;
                case 2: registerDiagnosticService(); break;
                case 3: findService(); break;
                case 4: removeService(); break;
                case 0: break;
                default: System.out.println("Nieprawidłowy wybór!\n");
            }
        } while(choice != 0);
    }

    public void registerRepairService() {
        String description, basePriceStr, durationStr;
        String changeTiresStr, fillFluidsStr, fixBrakeStr;
        double basePrice;
        Duration duration;
        boolean changeTires, fillFluids, fixBrake;

        System.out.println("\n--- Rejestracja usługi naprawczej ---\n");

        do {
            System.out.println("Podaj opis usługi: ");
            description = scanner.nextLine();
            if(description.isEmpty()) System.out.println("Opis nie może być pusty.\n");
        } while(description.isEmpty());

        do {
            System.out.println("Podaj cenę bazową: ");
            basePriceStr = scanner.nextLine();
        } while(!isPositiveDouble(basePriceStr));
        basePrice = Double.parseDouble(basePriceStr);

        do {
            System.out.println("Podaj szacowany czas trwania (format GG:MM): ");
            durationStr = scanner.nextLine();
        } while(!isValidTime(durationStr));

        LocalTime parsedTime = LocalTime.parse(durationStr, DateTimeFormatter.ofPattern("H:mm"));
        duration = Duration.ofHours(parsedTime.getHour()).plusMinutes(parsedTime.getMinute());

        do {
            System.out.println("Czy wymiana opon? (t/n): ");
            changeTiresStr = scanner.nextLine().toLowerCase();
            if(!changeTiresStr.equals("t") && !changeTiresStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!changeTiresStr.equals("t") && !changeTiresStr.equals("n"));
        changeTires = changeTiresStr.equals("t");

        do {
            System.out.println("Czy uzupełnianie płynów? (t/n): ");
            fillFluidsStr = scanner.nextLine().toLowerCase();
            if(!fillFluidsStr.equals("t") && !fillFluidsStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!fillFluidsStr.equals("t") && !fillFluidsStr.equals("n"));
        fillFluids = fillFluidsStr.equals("t");

        do {
            System.out.println("Czy naprawa hamulców? (t/n): ");
            fixBrakeStr = scanner.nextLine().toLowerCase();
            if(!fixBrakeStr.equals("t") && !fixBrakeStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!fixBrakeStr.equals("t") && !fixBrakeStr.equals("n"));
        fixBrake = fixBrakeStr.equals("t");

        try {
            Service saved = serviceManager.registerRepairService(description, basePrice,
                    duration, changeTires, fillFluids, fixBrake);
            System.out.println("Usługa naprawcza zarejestrowana pomyślnie! ID: " + saved.getId() + "\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void registerDiagnosticService() {
        String description, basePriceStr, durationStr;
        String checkTiresStr, checkBrakeStr, checkFluidsStr;
        double basePrice;
        Duration duration;
        boolean checkTires, checkBrake, checkFluids;

        System.out.println("\n--- Rejestracja usługi diagnostycznej ---\n");

        do {
            System.out.println("Podaj opis usługi: ");
            description = scanner.nextLine();
            if(description.isEmpty()) System.out.println("Opis nie może być pusty.\n");
        } while(description.isEmpty());

        do {
            System.out.println("Podaj cenę bazową: ");
            basePriceStr = scanner.nextLine();
        } while(!isPositiveDouble(basePriceStr));
        basePrice = Double.parseDouble(basePriceStr);

        do {
            System.out.println("Podaj szacowany czas trwania (format GG:MM): ");
            durationStr = scanner.nextLine();
        } while(!isValidTime(durationStr));

        LocalTime parsedTime = LocalTime.parse(durationStr, DateTimeFormatter.ofPattern("H:mm"));
        duration = Duration.ofHours(parsedTime.getHour()).plusMinutes(parsedTime.getMinute());

        do {
            System.out.println("Czy sprawdzać opony? (t/n): ");
            checkTiresStr = scanner.nextLine().toLowerCase();
            if(!checkTiresStr.equals("t") && !checkTiresStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!checkTiresStr.equals("t") && !checkTiresStr.equals("n"));
        checkTires = checkTiresStr.equals("t");

        do {
            System.out.println("Czy sprawdzać hamulce? (t/n): ");
            checkBrakeStr = scanner.nextLine().toLowerCase();
            if(!checkBrakeStr.equals("t") && !checkBrakeStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!checkBrakeStr.equals("t") && !checkBrakeStr.equals("n"));
        checkBrake = checkBrakeStr.equals("t");

        do {
            System.out.println("Czy sprawdzać płyny? (t/n): ");
            checkFluidsStr = scanner.nextLine().toLowerCase();
            if(!checkFluidsStr.equals("t") && !checkFluidsStr.equals("n")) {
                System.out.println("Wpisz 't' lub 'n'.\n");
            }
        } while(!checkFluidsStr.equals("t") && !checkFluidsStr.equals("n"));
        checkFluids = checkFluidsStr.equals("t");

        try {
            Service saved = serviceManager.registerDiagnosticService(description, basePrice,
                    duration, checkTires, checkFluids, checkBrake);
            System.out.println("Usługa diagnostyczna zarejestrowana pomyślnie! ID: " + saved.getId() + "\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void findService() {
        String idStr;
        int id;

        do {
            System.out.println("Podaj ID usługi: ");
            idStr = scanner.nextLine();
        } while(!isPositiveNumber(idStr));
        id = Integer.parseInt(idStr);

        Optional<Service> service = serviceManager.findById(id);
        if(service.isEmpty()) {
            System.out.println("Nie znaleziono usługi.\n");
            return;
        }

        System.out.println("Usługa:\n" + service.get().getInfo() + "\n");
    }

    public void removeService() {
        String idStr;
        int id;

        do {
            System.out.println("Podaj ID usługi do usunięcia: ");
            idStr = scanner.nextLine();
        } while(!isPositiveNumber(idStr));
        id = Integer.parseInt(idStr);

        Optional<Service> service = serviceManager.findById(id);
        if(service.isEmpty()) {
            System.out.println("Nie znaleziono usługi.\n");
            return;
        }

        try {
            serviceManager.unregisterService(service.get());
            System.out.println("Usługa została usunięta.\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void handleAppointmentOperations() {
        String input;
        int choice = -1;
        do {
            displayAppointmentMenu();
            input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy wybór! Wpisz liczbę.\n");
                continue;
            }

            switch(choice) {
                case 1: scheduleAppointment(); break;
                case 2: findAppointment(); break;
                case 3: cancelAppointment(); break;
                case 4: archiveAppointment(); break;
                case 0: break;
                default: System.out.println("Nieprawidłowy wybór!\n");
            }
        } while(choice != 0);
    }

    public void scheduleAppointment() {
        String clientPesel, dateTimeStr;
        LocalDateTime dateTime;
        List<Service> services = new ArrayList<>();

        System.out.println("\n--- Umawianie wizyty ---\n");

        do {
            System.out.println("Podaj PESEL klienta: ");
            clientPesel = scanner.nextLine();
        } while(!isValidPesel(clientPesel));

        do {
            System.out.println("Podaj date i czas wizyty (format RRRR-MM-DD GG:MM:SS): ");
            dateTimeStr = scanner.nextLine();
        } while(!isValidDateTime(dateTimeStr));
        dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        System.out.println("\nDostępne usługi:\n");
        List<Service> allServices = serviceManager.findAll();
        for(Service service : allServices) {
            System.out.println(service.getInfo() + '\n');
        }

        System.out.println("\nPodaj ID usług oddzielone przecinkami (np. 1,3,5): ");
        String servicesInput = scanner.nextLine();

        String[] serviceIds = servicesInput.split(",");

        for(String idStr : serviceIds) {
            idStr = idStr.trim();
            if(isPositiveNumber(idStr)) {
                int id = Integer.parseInt(idStr);
                Optional<Service> foundService = serviceManager.findById(id);
                foundService.ifPresent(services::add);
            }
        }

        if(services.isEmpty()) {
            System.out.println("Nie wybrano żadnych usług.\n");
            return;
        }

        try {
            Appointment saved = appointmentManager.registerAppointment(services, dateTime, clientPesel);
            System.out.println("Wizyta umówiona pomyślnie! ID: " + saved.getId() + "\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void findAppointment() {
        String pesel;
        do {
            System.out.println("Podaj PESEL klienta: ");
            pesel = scanner.nextLine();
        } while(!isValidPesel(pesel));

        Optional<Client> clients = clientManager.findByPesel(pesel);
        if(clients.isEmpty()) {
            System.out.println("Nie znaleziono klienta.\n");
            return;
        }

        List<Appointment> appointments = appointmentManager.findByClientPesel(clients.get().getPesel());
        if(appointments.isEmpty()) {
            System.out.println("Brak wizyt dla tego klienta.\n");
            return;
        }

        for(Appointment appointment : appointments) {
            System.out.println("ID: " + appointment.getId()
                    + ", Data: " + appointment.getDate()
                    + ", Klient: " + appointment.getClient().getName() + " " + appointment.getClient().getLastName() + "\n");
        }
    }

    public void cancelAppointment() {
        String idStr;
        int id;

        do {
            System.out.println("Podaj ID wizyty do anulowania: ");
            idStr = scanner.nextLine();
        } while(!isPositiveNumber(idStr));
        id = Integer.parseInt(idStr);

        Optional<Appointment> appointment = appointmentManager.findById(id);
        if(appointment.isEmpty()) {
            System.out.println("Nie znaleziono wizyty.\n");
            return;
        }

        try {
            appointmentManager.cancelAppointment(appointment.get());
            System.out.println("Wizyta anulowana pomyślnie.\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void archiveAppointment() {
        String idStr;
        int id;

        do {
            System.out.println("Podaj ID wizyty do archiwizacji: ");
            idStr = scanner.nextLine();
        } while(!isPositiveNumber(idStr));
        id = Integer.parseInt(idStr);

        Optional<Appointment> appointment = appointmentManager.findById(id);
        if(appointment.isEmpty()) {
            System.out.println("Nie znaleziono wizyty.\n");
            return;
        }

        try {
            appointmentManager.unregisterAppointment(appointment.get());
            System.out.println("Wizyta zarchiwizowana pomyślnie.\n");
        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage() + "\n");
        }
    }

    public void displayMainMenu() {
        System.out.println("===== MENU GLOWNE =====");
        System.out.println("1. Zarządzanie klientami");
        System.out.println("2. Zarządzanie pojazdami");
        System.out.println("3. Zarządzanie usługami");
        System.out.println("4. Zarządzanie wizytami");
        System.out.println("0. Wyjście");
        System.out.println("Wybierz opcje:");
    }

    public void displayClientMenu() {
        System.out.println("===== ZARZADZANIE KLIENTAMI =====");
        System.out.println("1. Dodaj klienta");
        System.out.println("2. Wyszukaj klienta");
        System.out.println("3. Usuń klienta");
        System.out.println("4. Wyświetl wszystkich klientów");
        System.out.println("5. Ustaw typ klienta");
        System.out.println("0. Powrót");
        System.out.println("Wybierz opcje:");
    }

    public void displayVehicleMenu() {
        System.out.println("===== ZARZADZANIE POJAZDAMI =====");
        System.out.println("1. Zarejestruj samochód");
        System.out.println("2. Zarejestruj motocykl");
        System.out.println("3. Wyszukaj pojazd");
        System.out.println("4. Usuń pojazd");
        System.out.println("0. Powrót");
        System.out.println("Wybierz opcję: ");
    }

    public void displayServiceMenu() {
        System.out.println("===== ZARZADZANIE USLUGAMI =====");
        System.out.println("1. Dodaj usługę naprawcza");
        System.out.println("2. Dodaj usługę diagnostyczna");
        System.out.println("3. Wyszukaj usługę");
        System.out.println("4. Usuń usługę");
        System.out.println("0. Powrót");
        System.out.println("Wybierz opcję: ");
    }

    public void displayAppointmentMenu() {
        System.out.println("===== ZARZADZANIE WIZYTAMI =====");
        System.out.println("1. Umów wizytę");
        System.out.println("2. Wyszukaj wizytę");
        System.out.println("3. Anuluj wizytę");
        System.out.println("4. Archiwizuj wizytę");
        System.out.println("0. Powrót");
        System.out.println("Wybierz opcję: ");
    }

    public void displayClientFindMenu() {
        System.out.println("Szukaj klienta po:");
        System.out.println("1. Imieniu i Nazwisku");
        System.out.println("2. Numerze telefonu");
        System.out.println("3. Numerze Pesel");
        System.out.println("4. Archiwalni klienci");
        System.out.println("5. Powrót");
        System.out.println("Wybierz opcję: ");
    }

    public void displayVehicleFindMenu() {
        System.out.println("Szukaj pojazd po:");
        System.out.println("1. Peselu klienta");
        System.out.println("2. Numerze vin");
        System.out.println("3. Tablicy rejestracyjnej");
        System.out.println("4. Marce i modelu pojazdu");
        System.out.println("5. Po pojemności silnika");
        System.out.println("6. Powrót");
        System.out.println("Wybierz opcję: ");
    }

    public void displayClientTypeMenu() {
        System.out.println("Jaki typ klienta chcesz ustawić?");
        System.out.println("1. Domyślny (większość klientów już go posiada, brak zniżek)");
        System.out.println("2. Złoty (10% zniżki)");
        System.out.println("3. Diamentowy (25% zniżki)");
        System.out.println("4. Powrót");
        System.out.println("Wybierz opcję: ");
    }
}
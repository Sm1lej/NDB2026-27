package managers;

import java.util.List;
import repositories.VehicleRepository;
import repositories.ClientRepository;
import model.Vehicle;
import model.Client;
import model.Car;
import model.Motorbike;

public class VehicleManager {
    private VehicleRepository vehicleRepository;
    private ClientRepository clientRepository;

    /**
     * Konstruktor klasy VehicleManager, tworzący obiekt menedżera z podanymi zależnościami.
     *
     * @param vehicleRepository Repozytorium pojazdów
     * @param clientRepository Repozytorium klientów
     */
    public VehicleManager(VehicleRepository vehicleRepository, ClientRepository clientRepository) {
        if (vehicleRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium pojazdów.");
        }
        if (clientRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium klientów.");
        }
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Rejestruje nowy samochód w systemie.
     */
    public void registerCar(String ownerPesel, String vinNumber, String registrationNumber, int yearOfProduction,
                            String brand, String model, String color, int engineDisplacement, int enginePower,
                            int weight, String bodyType, String fuelType, int doorsCount) {

        List<Client> existingClients = clientRepository.findByPesel(ownerPesel);
        if (existingClients.isEmpty()) {
            throw new RuntimeException("Nie znaleziono klienta o podanym numerze PESEL.");
        }

        List<Vehicle> existingVehiclesVin = vehicleRepository.findByVinNumber(vinNumber);
        if (!existingVehiclesVin.isEmpty()) {
            throw new RuntimeException("Pojazd o podanym numerze VIN już istnieje.");
        }

        List<Vehicle> existingVehiclesRegistrationNumber = vehicleRepository.findByRegistrationNumber(registrationNumber);
        if (!existingVehiclesRegistrationNumber.isEmpty()) {
            throw new RuntimeException("Pojazd o podanym numerze rejestracyjnym już istnieje.");
        }

        Client client = existingClients.get(0);

        try {
            Vehicle car = new Car(client, vinNumber, registrationNumber, yearOfProduction, brand, model,
                    color, engineDisplacement, enginePower, weight, bodyType, fuelType, doorsCount);

            vehicleRepository.addElement(car);
            client.addVehicle(car);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Rejestruje nowy motocykl w systemie.
     */
    public void registerMotorBike(String ownerPesel, String vinNumber, String registrationNumber, int yearOfProduction,
                                  String brand, String model, String color, int engineDisplacement, int enginePower,
                                  int weight, String brakeSystem) {

        List<Client> existingClients = clientRepository.findByPesel(ownerPesel);
        if (existingClients.isEmpty()) {
            throw new RuntimeException("Nie znaleziono klienta o podanym numerze PESEL.");
        }

        List<Vehicle> existingVehiclesVin = vehicleRepository.findByVinNumber(vinNumber);
        if (!existingVehiclesVin.isEmpty()) {
            throw new RuntimeException("Pojazd o podanym numerze VIN już istnieje.");
        }

        List<Vehicle> existingVehiclesRegistrationNumber = vehicleRepository.findByRegistrationNumber(registrationNumber);
        if (!existingVehiclesRegistrationNumber.isEmpty()) {
            throw new RuntimeException("Pojazd o podanym numerze rejestracyjnym już istnieje.");
        }

        Client client = existingClients.get(0);

        try {
            Vehicle motorBike = new Motorbike(client, vinNumber, registrationNumber, yearOfProduction, brand,
                    model, color, engineDisplacement, enginePower, weight, brakeSystem);

            vehicleRepository.addElement(motorBike);
            client.addVehicle(motorBike);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Usuwa pojazd z repozytorium na podstawie numeru VIN.
     */
    public void unregisterVehicle(String vinNumber) {
        List<Vehicle> existingVehiclesVin = vehicleRepository.findByVinNumber(vinNumber);
        if (existingVehiclesVin.isEmpty()) {
            throw new RuntimeException("Nie znaleziono pojazdu o podanym numerze VIN.");
        }

        if (existingVehiclesVin.get(0) == null) {
            throw new RuntimeException("Nieprawidłowy obiekt pojazdu.");
        }

        vehicleRepository.removeElement(existingVehiclesVin.get(0));
    }

    public List<Vehicle> findByClientPesel(String clientPesel) {
        return vehicleRepository.findByClientPesel(clientPesel);
    }

    public List<Vehicle> findByVinNumber(String vinNumber) {
        return vehicleRepository.findByVinNumber(vinNumber);
    }

    public List<Vehicle> findByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber);
    }

    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        return vehicleRepository.findByBrandAndModel(brand, model);
    }

    public List<Vehicle> findByEngineDisplacement(int engineDisplacement) {
        return vehicleRepository.findByEngineDisplacement(engineDisplacement);
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public String report() {
        return vehicleRepository.report();
    }
}

package managers;

import java.util.List;
import repositories.VehicleRepository;
import repositories.ClientRepository;
import model.Vehicle;
import model.Client;
import model.Car;
import model.Motorbike;

public class VehicleManager {
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    public VehicleManager(VehicleRepository vehicleRepository, ClientRepository clientRepository) {
        if (vehicleRepository == null || clientRepository == null) {
            throw new IllegalArgumentException("Repozytoria nie mogą być null.");
        }
        this.vehicleRepository = vehicleRepository;
        this.clientRepository = clientRepository;
    }

    public Car registerCar(String ownerPesel, String vinNumber, String registrationNumber,
                           int yearOfProduction, String brand, String model, String color,
                           int engineDisplacement, int enginePower, int weight, String bodyType,
                           String fuelType, int doorsCount) {
        Client client = getExistingClient(ownerPesel);
        validateUniqueVehicle(vinNumber, registrationNumber);

        Car car = new Car(client, vinNumber, registrationNumber, yearOfProduction,
                brand, model, color, engineDisplacement, enginePower, weight,
                bodyType, fuelType, doorsCount);
        return (Car) vehicleRepository.save(car);
    }

    public Motorbike registerMotorBike(String ownerPesel, String vinNumber,
                                       String registrationNumber, int yearOfProduction, String brand,
                                       String model, String color, int engineDisplacement, int enginePower,
                                       int weight, String brakeSystem) {
        Client client = getExistingClient(ownerPesel);
        validateUniqueVehicle(vinNumber, registrationNumber);

        Motorbike motorBike = new Motorbike(client, vinNumber, registrationNumber,
                yearOfProduction, brand, model, color, engineDisplacement,
                enginePower, weight, brakeSystem);
        return (Motorbike) vehicleRepository.save(motorBike);
    }

    private Client getExistingClient(String pesel) {
        return clientRepository.findById(pesel)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono klienta o podanym numerze PESEL."));
    }

    private void validateUniqueVehicle(String vinNumber, String registrationNumber) {
        if (!vehicleRepository.findByVinNumber(vinNumber).isEmpty()) {
            throw new IllegalArgumentException("Pojazd o podanym numerze VIN już istnieje.");
        }
        if (!vehicleRepository.findByRegistrationNumber(registrationNumber).isEmpty()) {
            throw new IllegalArgumentException(
                    "Pojazd o podanym numerze rejestracyjnym już istnieje.");
        }
    }

    public void unregisterVehicle(String vinNumber) {
        if (vehicleRepository.findByVinNumber(vinNumber).isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono pojazdu o podanym numerze VIN.");
        }
        vehicleRepository.deleteById(vinNumber);
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
}

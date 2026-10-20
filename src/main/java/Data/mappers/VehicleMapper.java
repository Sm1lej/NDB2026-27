package Data.mappers;

import Data.entity.CarEnt;
import Data.entity.ClientEnt;
import Data.entity.MotorbikeEnt;
import Data.entity.VehicleEnt;
import model.Vehicle;
import model.Motorbike;
import model.Car;
import model.Client;

public class VehicleMapper {
    private final ClientMapper clientMapper = new ClientMapper();

    public VehicleEnt toEntity(Vehicle vehicle, ClientEnt owner) {
        if (vehicle == null)
            return null;
        if (owner == null)
            throw new NullPointerException("właściciel nie może być null");

        if (vehicle instanceof Car car) {
            return new CarEnt(
                    owner,
                    car.getVinNumber(),
                    car.getRegistrationNumber(),
                    car.getYearOfProduction(),
                    car.getBrand(),
                    car.getModel(),
                    car.getColor(),
                    car.getEngineDisplacement(),
                    car.getEnginePower(),
                    car.getWeight(),
                    car.getBodyType(),
                    car.getFuelType(),
                    car.getDoorsCount()
            );
        }
        if (vehicle instanceof Motorbike motorbike) {
            return new MotorbikeEnt(
                    owner,
                    motorbike.getVinNumber(),
                    motorbike.getRegistrationNumber(),
                    motorbike.getYearOfProduction(),
                    motorbike.getBrand(),
                    motorbike.getModel(),
                    motorbike.getColor(),
                    motorbike.getEngineDisplacement(),
                    motorbike.getEnginePower(),
                    motorbike.getWeight(),
                    motorbike.getBrakeSystem()
            );
        }
        throw new IllegalArgumentException(
                "Nieznany typ pojazdu: " + vehicle.getClass().getName()
        );
    }

    public Vehicle toDomain(VehicleEnt vehicleEnt) {
        if (vehicleEnt == null)
            return null;

        Client owner = clientMapper.toDomain(vehicleEnt.getOwner());
        if (vehicleEnt instanceof CarEnt carEnt) {
            return new Car(
                    owner,
                    carEnt.getVinNumber(),
                    carEnt.getRegistrationNumber(),
                    carEnt.getYearOfProduction(),
                    carEnt.getBrand(),
                    carEnt.getModel(),
                    carEnt.getColor(),
                    carEnt.getEngineDisplacement(),
                    carEnt.getEnginePower(),
                    carEnt.getWeight(),
                    carEnt.getBodyType(),
                    carEnt.getFuelType(),
                    carEnt.getDoorsCount()

            );
        }
        if (vehicleEnt instanceof MotorbikeEnt motorbikeEnt) {
            return new Motorbike(
                    owner,
                    motorbikeEnt.getVinNumber(),
                    motorbikeEnt.getRegistrationNumber(),
                    motorbikeEnt.getYearOfProduction(),
                    motorbikeEnt.getBrand(),
                    motorbikeEnt.getModel(),
                    motorbikeEnt.getColor(),
                    motorbikeEnt.getEngineDisplacement(),
                    motorbikeEnt.getEnginePower(),
                    motorbikeEnt.getWeight(),
                    motorbikeEnt.getBrakeSystem()
            );
        }

        throw new IllegalArgumentException(
                "Nieznany typ encji pojazdu: " + vehicleEnt.getClass().getName()
        );
    }
}

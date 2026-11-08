package Data.MongoDB.mappers;

import Data.MongoDB.documents.CarDocument;
import Data.MongoDB.documents.MotorbikeDocument;
import Data.MongoDB.documents.VehicleDocument;
import model.Car;
import model.Client;
import model.Motorbike;
import model.Vehicle;

public class VehicleMapper {

    public Vehicle toDomain(VehicleDocument document, Client owner) {
        if (document == null) {
            return null;
        }

        if (document instanceof CarDocument carDoc) {
            return new Car(
                    owner,
                    carDoc.getVinNumber(),
                    carDoc.getRegistrationNumber(),
                    carDoc.getYearOfProduction(),
                    carDoc.getBrand(),
                    carDoc.getModel(),
                    carDoc.getColor(),
                    carDoc.getEngineDisplacement(),
                    carDoc.getEnginePower(),
                    carDoc.getWeight(),
                    carDoc.getBodyType(),
                    carDoc.getFuelType(),
                    carDoc.getDoorsCount()
            );
        }

        if (document instanceof MotorbikeDocument motorbikeDoc) {
            return new Motorbike(
                    owner,
                    motorbikeDoc.getVinNumber(),
                    motorbikeDoc.getRegistrationNumber(),
                    motorbikeDoc.getYearOfProduction(),
                    motorbikeDoc.getBrand(),
                    motorbikeDoc.getModel(),
                    motorbikeDoc.getColor(),
                    motorbikeDoc.getEngineDisplacement(),
                    motorbikeDoc.getEnginePower(),
                    motorbikeDoc.getWeight(),
                    motorbikeDoc.getBrakeSystem()
            );
        }

        throw new IllegalArgumentException(
                "Nieobsługiwany typ dokumentu pojazdu: " + document.getClass().getName()
        );
    }

    public VehicleDocument toDocument(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        VehicleDocument vehicleDoc;

        if (vehicle instanceof Car car) {
            CarDocument carDocument = new CarDocument();
            carDocument.setBodyType(car.getBodyType());
            carDocument.setFuelType(car.getFuelType());
            carDocument.setDoorsCount(car.getDoorsCount());
            vehicleDoc = carDocument;
        } else if (vehicle instanceof Motorbike motorbike) {
            MotorbikeDocument motorbikeDocument = new MotorbikeDocument();
            motorbikeDocument.setBrakeSystem(motorbike.getBrakeSystem());
            vehicleDoc = motorbikeDocument;
        } else {
            throw new IllegalArgumentException(
                    "Nieobsługiwany typ pojazdu: " + vehicle.getClass().getName()
            );
        }
        vehicleDoc.setVinNumber(vehicle.getVinNumber());

        if (vehicle.getOwner() != null) {
            vehicleDoc.setOwnerPesel(vehicle.getOwner().getPesel());
        }

        vehicleDoc.setRegistrationNumber(vehicle.getRegistrationNumber());
        vehicleDoc.setYearOfProduction(vehicle.getYearOfProduction());
        vehicleDoc.setBrand(vehicle.getBrand());
        vehicleDoc.setModel(vehicle.getModel());
        vehicleDoc.setColor(vehicle.getColor());
        vehicleDoc.setEngineDisplacement(vehicle.getEngineDisplacement());
        vehicleDoc.setEnginePower(vehicle.getEnginePower());
        vehicleDoc.setWeight(vehicle.getWeight());

        return vehicleDoc;
    }
}
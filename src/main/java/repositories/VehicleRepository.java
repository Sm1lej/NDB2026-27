package repositories;
import model.Vehicle;
import java.util.List;

public interface VehicleRepository extends MainRepository<Vehicle, String> {

     List<Vehicle> findByClientPesel(String clientPesel);

     List<Vehicle> findByVinNumber(String vinNumber);

     List<Vehicle> findByRegistrationNumber(String registrationNumber);

     List<Vehicle> findByBrandAndModel(String brand, String model);

     List<Vehicle> findByEngineDisplacement(int engineDisplacement);


}

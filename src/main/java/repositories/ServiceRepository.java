package repositories;

import java.util.List;
import model.Service;

public interface ServiceRepository extends MainRepository<Service, Integer> {

    List<Service> findByBasePrice(double basePrice);
}

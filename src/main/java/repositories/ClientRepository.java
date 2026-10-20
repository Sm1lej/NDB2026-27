package repositories;
import java.util.List;
import model.Client;

public interface ClientRepository extends MainRepository<Client, String> {

     List<Client> findByNameAndLastName(String name, String lastName);

     List<Client> findByPhoneNumber(String phoneNumber);

     List<Client> findByArchive();


}

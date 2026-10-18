package repositories;
import java.util.List;
import model.Client;

public interface ClientRepository extends MainRepository<Client, String> {

     List<Client> findByLastName(String lastName);

     List<Client> findByNameAndLastName(String name, String lastName);

     List<Client> findByEmailAddress(String email);

     List<Client> findByPhoneNumber(String phoneNumber);


     List<Client> findByArchive();


}

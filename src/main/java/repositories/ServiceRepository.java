package repositories;

import java.util.List;
import model.Service;

public class ServiceRepository extends MainRepository<Service> {

    /**
     * Wyszukuje usługi na podstawie identyfikatora.
     *
     * @param id Identyfikator usługi
     * @return Lista usług o podanym identyfikatorze
     */
    public List<Service> findById(int id) {
        return findBy(service -> service.getId() == id);
    }

    /**
     * Generuje raport dotyczący usług w repozytorium.
     *
     * @return Tekstowa reprezentacja danych raportu jako łańcuch znaków
     */
    public String report() {
        StringBuilder info = new StringBuilder();
        info.append("Ilość serwisów: ").append(getSize()).append("\n");

        int repoSize = getSize();


        for (int i = 0; i < repoSize; i++) {
            info.append("Usługa ").append(i + 1).append(". ")
                .append(getElement(i).getInfo()).append("\n");
        }

        return info.toString();
    }
}

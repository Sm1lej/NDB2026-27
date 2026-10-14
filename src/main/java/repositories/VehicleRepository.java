package repositories;
import model.Vehicle;
import java.util.List;

public class VehicleRepository extends MainRepository<Vehicle> {

    /**
     * Wyszukuje pojazdy powiązane z określonym klientem po numerze PESEL właściciela.
     *
     * @param clientPesel Numer PESEL właściciela pojazdu
     * @return Lista pojazdów powiązanych z klientem
     */
    public List<Vehicle> findByClientPesel(String clientPesel) {
        return findBy(vehicle -> vehicle.getOwner() != null
                && clientPesel.equals(vehicle.getOwner().getPesel()));
    }

    /**
     * Wyszukuje pojazdy na podstawie numeru VIN.
     *
     * @param vinNumber Numer VIN pojazdu
     * @return Lista pojazdów o podanym numerze VIN
     */
    public List<Vehicle> findByVinNumber(String vinNumber) {
        return findBy(vehicle -> vinNumber.equals(vehicle.getVinNumber()));
    }

    /**
     * Wyszukuje pojazdy na podstawie numeru rejestracyjnego.
     *
     * @param registrationNumber Numer rejestracyjny pojazdu
     * @return Lista pojazdów o podanym numerze rejestracyjnym
     */
    public List<Vehicle> findByRegistrationNumber(String registrationNumber) {
        return findBy(vehicle -> registrationNumber.equals(vehicle.getRegistrationNumber()));
    }

    /**
     * Wyszukuje pojazdy na podstawie marki i modelu.
     *
     * @param brand Marka pojazdu
     * @param model Model pojazdu
     * @return Lista pojazdów pasujących do podanej marki i modelu
     */
    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        return findBy(vehicle -> brand.equals(vehicle.getBrand())
                && model.equals(vehicle.getModel()));
    }

    /**
     * Wyszukuje pojazdy na podstawie pojemności silnika.
     *
     * @param engineDisplacement Pojemność silnika
     * @return Lista pojazdów o podanej pojemności silnika
     */
    public List<Vehicle> findByEngineDisplacement(int engineDisplacement) {
        return findBy(vehicle -> vehicle.getEngineDisplacement() == engineDisplacement);
    }

    /**
     * Generuje raport dotyczący pojazdów w repozytorium.
     *
     * @return Tekstowa reprezentacja danych raportu jako łańcuch znaków
     */
    public String report() {
        StringBuilder info = new StringBuilder();

        info.append("Ilość pojazdów: ").append(getSize()).append("\n");

        int repoSize = getSize();


        for (int i = 0; i < repoSize; i++) {
            info.append("Pojazd ").append(i + 1).append(". ")
                .append(getElement(i).getInfo()).append("\n");
        }

        return info.toString();
    }
}

package managers;

import java.time.Duration;
import java.util.List;
import repositories.ServiceRepository;
import model.Service;
import model.RepairService;
import model.DiagnosticService;

public class ServiceManager {
    private ServiceRepository serviceRepository;

    /**
     * Konstruktor klasy ServiceManager, tworzący obiekt menedżera z podanym repozytorium.
     *
     * @param serviceRepository Repozytorium serwisów
     */
    public ServiceManager(ServiceRepository serviceRepository) {
        if (serviceRepository == null) {
            throw new RuntimeException("Nieprawidłowe repozytorium serwisów.");
        }
        this.serviceRepository = serviceRepository;
    }

    /**
     * Rejestruje nową usługę naprawczą.
     *
     * @param id Identyfikator usługi
     * @param description Opis usługi
     * @param basePrice Cena bazowa
     * @param estimatedDurationTime Szacowany czas trwania
     * @param changeTires Czy wymienia opony
     * @param fillFluids Czy uzupełnia płyny
     * @param fixBrakeSystem Czy naprawia układ hamulcowy
     */
    public void registerRepairService(int id, String description, double basePrice,
                                      Duration estimatedDurationTime, boolean changeTires, boolean fillFluids,
                                      boolean fixBrakeSystem) {
        List<Service> existingServices = findById(id);
        if (!existingServices.isEmpty()) {
            throw new RuntimeException("Usługa o podanym identyfikatorze już istnieje.");
        }

        int newId = serviceRepository.getSize() + 1;

        try {
            Service service = new RepairService(newId, description, basePrice,
                    estimatedDurationTime, changeTires, fillFluids, fixBrakeSystem);
            serviceRepository.addElement(service);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Rejestruje nową usługę diagnostyczną.
     *
     * @param id Identyfikator usługi
     * @param description Opis usługi
     * @param basePrice Cena bazowa
     * @param estimatedDurationTime Szacowany czas trwania
     * @param checkTires Czy sprawdza opony
     * @param checkFluids Czy sprawdza płyny
     * @param checkBrake Czy sprawdza hamulce
     */
    public void registerDiagnosticService(int id, String description, double basePrice,
                                          Duration estimatedDurationTime, boolean checkTires, boolean checkFluids,
                                          boolean checkBrake) {
        List<Service> existingServices = findById(id);
        if (!existingServices.isEmpty()) {
            throw new RuntimeException("Usługa o podanym identyfikatorze już istnieje.");
        }

        int newId = serviceRepository.getSize() + 1;

        try {
            Service service = new DiagnosticService(newId, description, basePrice, estimatedDurationTime,
                    checkTires, checkFluids, checkBrake);
            serviceRepository.addElement(service);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Wypisuje (usuwa) usługę z systemu.
     *
     * @param service Usługa do usunięcia
     */
    public void unregisterService(Service service) {
        if (service == null) {
            throw new RuntimeException("Usługa nie może być null.");
        }

        List<Service> existingServices = findById(service.getId());
        if (existingServices.isEmpty()) {
            throw new RuntimeException("Nie znaleziono usługi w repozytorium.");
        }

        serviceRepository.removeElement(service);
    }

    /**
     * Wyszukuje usługi na podstawie identyfikatora.
     *
     * @param id Identyfikator usługi
     * @return Lista znalezionych usług
     */
    public List<Service> findById(int id) {
        return serviceRepository.findById(id);
    }

    /**
     * Zwraca wszystkie zarejestrowane usługi.
     *
     * @return Lista wszystkich usług
     */
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    /**
     * Generuje raport dotyczący usług.
     *
     * @return Tekstowa reprezentacja raportu
     */
    public String report() {
        return serviceRepository.report();
    }
}

package managers;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import repositories.ServiceRepository;
import model.Service;
import model.RepairService;
import model.DiagnosticService;

public class ServiceManager {
    private final ServiceRepository serviceRepository;

    public ServiceManager(ServiceRepository serviceRepository) {
        if (serviceRepository == null) {
            throw new IllegalArgumentException("Nieprawidłowe repozytorium usług.");
        }
        this.serviceRepository = serviceRepository;
    }

    public RepairService registerRepairService(String description, double basePrice,
                                               Duration estimatedDurationTime, boolean changeTires, boolean fillFluids,
                                               boolean fixBrakeSystem) {
        RepairService service = new RepairService(description, basePrice,
                estimatedDurationTime, changeTires, fillFluids, fixBrakeSystem);
        return (RepairService) serviceRepository.save(service);
    }

    public DiagnosticService registerDiagnosticService(String description, double basePrice,
                                                       Duration estimatedDurationTime, boolean checkTires, boolean checkFluids,
                                                       boolean checkBrake) {
        DiagnosticService service = new DiagnosticService(description, basePrice,
                estimatedDurationTime, checkTires, checkFluids, checkBrake);
        return (DiagnosticService) serviceRepository.save(service);
    }

    public void unregisterService(Service service) {
        if (service == null || service.getId() == null) {
            throw new IllegalArgumentException("Usługa musi posiadać identyfikator.");
        }
        if (serviceRepository.findById(service.getId()).isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono usługi w repozytorium.");
        }
        serviceRepository.deleteById(service.getId());
    }

    public Optional<Service> findById(int id) {
        return serviceRepository.findById(id);
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }
}

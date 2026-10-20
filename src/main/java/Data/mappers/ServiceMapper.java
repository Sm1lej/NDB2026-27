package Data.mappers;

import Data.entity.DiagnosticServiceEnt;
import Data.entity.RepairServiceEnt;
import Data.entity.ServiceEnt;
import model.DiagnosticService;
import model.RepairService;
import model.Service;

public class ServiceMapper {

    public ServiceEnt toEntity(Service service) {
        if (service == null) {
            return null;
        }

        if (service instanceof RepairService repair) {
            return new RepairServiceEnt(
                    repair.getDescription(),
                    repair.getBasePrice(),
                    repair.getEstimatedTime().toMinutes(),
                    repair.isChangeTires(),
                    repair.isFillFluids(),
                    repair.isFixBrakeSystem()
            );
        }

        if (service instanceof DiagnosticService diagnostic) {
            return new DiagnosticServiceEnt(
                    diagnostic.getDescription(),
                    diagnostic.getBasePrice(),
                    diagnostic.getEstimatedTime().toMinutes(),
                    diagnostic.isCheckTires(),
                    diagnostic.isCheckFluids(),
                    diagnostic.isCheckBrake()
            );
        }

        throw new IllegalArgumentException(
                "Nieznany typ usługi: " + service.getClass().getName()
        );
    }

    public Service toDomain(ServiceEnt serviceEnt) {
        if (serviceEnt == null) {
            return null;
        }

        if (serviceEnt instanceof RepairServiceEnt repairEnt) {
            return new RepairService(
                    repairEnt.getId(),
                    repairEnt.getDescription(),
                    repairEnt.getBasePrice(),
                    java.time.Duration.ofMinutes(
                            repairEnt.getEstimatedDurationMinutes()
                    ),
                    repairEnt.isChangeTires(),
                    repairEnt.isFillFluids(),
                    repairEnt.isFixBrakeSystem()
            );
        }

        if (serviceEnt instanceof DiagnosticServiceEnt diagnosticEnt) {
            return new DiagnosticService(
                    diagnosticEnt.getId(),
                    diagnosticEnt.getDescription(),
                    diagnosticEnt.getBasePrice(),
                    java.time.Duration.ofMinutes(
                            diagnosticEnt.getEstimatedDurationMinutes()
                    ),
                    diagnosticEnt.isCheckFluids(),
                    diagnosticEnt.isCheckTires(),
                    diagnosticEnt.isCheckBrake()
            );
        }

        throw new IllegalArgumentException(
                "Nieznany typ encji usługi: " + serviceEnt.getClass().getName()
        );
    }
}

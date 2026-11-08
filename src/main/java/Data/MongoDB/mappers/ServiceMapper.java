package Data.MongoDB.mappers;

import Data.MongoDB.documents.DiagnosticServiceDocument;
import Data.MongoDB.documents.RepairServiceDocument;
import Data.MongoDB.documents.ServiceDocument;
import model.DiagnosticService;
import model.RepairService;
import model.Service;

import java.time.Duration;

public class ServiceMapper {
    public Service toDomain(ServiceDocument serviceDoc) {
        if (serviceDoc == null) {
            return null;
        }

        Duration estimatedDurationMinutes = Duration.ofMinutes(
                serviceDoc.getEstimatedDurationMinutes()
        );

        if (serviceDoc instanceof RepairServiceDocument) {
            return new RepairService(
                    serviceDoc.getId(),
                    serviceDoc.getDescription(),
                    serviceDoc.getBasePrice(),
                    estimatedDurationMinutes,
                    ((RepairServiceDocument) serviceDoc).isChangeTires(),
                    ((RepairServiceDocument) serviceDoc).isFillFluids(),
                    ((RepairServiceDocument) serviceDoc).isFixBrakeSystem()
            );
        }

        if (serviceDoc instanceof DiagnosticServiceDocument) {
            return new DiagnosticService(
                    serviceDoc.getId(),
                    serviceDoc.getDescription(),
                    serviceDoc.getBasePrice(),
                    estimatedDurationMinutes,
                    ((DiagnosticServiceDocument) serviceDoc).isCheckTires(),
                    ((DiagnosticServiceDocument) serviceDoc).isCheckFluids(),
                    ((DiagnosticServiceDocument) serviceDoc).isCheckBrake()
            );
        }

        return null;
    }

    public ServiceDocument toDocument(Service service) {
        if (service == null) {
            return null;
        }

        long estimatedDurationMinutes = service.getEstimatedTime().toMinutes();

        if (service instanceof RepairService) {
            RepairServiceDocument repairServiceDoc = new RepairServiceDocument();

            repairServiceDoc.setId(service.getId());
            repairServiceDoc.setDescription(service.getDescription());
            repairServiceDoc.setBasePrice(service.getBasePrice());
            repairServiceDoc.setEstimatedDurationMinutes(estimatedDurationMinutes);
            repairServiceDoc.setFillFluids(((RepairService) service).isFillFluids());
            repairServiceDoc.setChangeTires(((RepairService) service).isChangeTires());
            repairServiceDoc.setFixBrakeSystem(((RepairService) service).isFixBrakeSystem());

            return repairServiceDoc;
        }

        if (service instanceof DiagnosticService) {
            DiagnosticServiceDocument diagnosticServiceDoc = new DiagnosticServiceDocument();

            diagnosticServiceDoc.setId(service.getId());
            diagnosticServiceDoc.setDescription(service.getDescription());
            diagnosticServiceDoc.setBasePrice(service.getBasePrice());
            diagnosticServiceDoc.setEstimatedDurationMinutes(estimatedDurationMinutes);
            diagnosticServiceDoc.setCheckFluids(((DiagnosticService) service).isCheckFluids());
            diagnosticServiceDoc.setCheckTires(((DiagnosticService) service).isCheckTires());
            diagnosticServiceDoc.setCheckBrake(((DiagnosticService) service).isCheckBrake());

            return diagnosticServiceDoc;
        }

        return null;
    }
}

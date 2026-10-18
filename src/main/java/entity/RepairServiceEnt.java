package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "repair_services")
public class RepairServiceEnt extends ServiceEnt{

    private boolean changeTires;
    private boolean fillFluids;
    private boolean fixBrakeSystem;

    public RepairServiceEnt() {
        super();
    }

    public RepairServiceEnt(String description, double basePrice, boolean changeTires, boolean fillFluids, boolean fixBrakeSystem) {
        super(description, basePrice);
        this.changeTires = changeTires;
        this.fillFluids = fillFluids;
        this.fixBrakeSystem = fixBrakeSystem;
    }

    public boolean isChangeTires() {
        return changeTires;
    }

    public void setChangeTires(boolean changeTires) {
        this.changeTires = changeTires;
    }

    public boolean isFillFluids() {
        return fillFluids;
    }

    public void setFillFluids(boolean fillFluids) {
        this.fillFluids = fillFluids;
    }

    public boolean isFixBrakeSystem() {
        return fixBrakeSystem;
    }

    public void setFixBrakeSystem(boolean fixBrakeSystem) {
        this.fixBrakeSystem = fixBrakeSystem;
    }
}

package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostic_services")
public class DiagnosticServiceEnt extends ServiceEnt {

    private boolean checkTires;
    private boolean checkFluids;
    private boolean checkBrake;

    public DiagnosticServiceEnt() {
        super();
    }

    public DiagnosticServiceEnt(String description, double basePrice, boolean checkTires, boolean checkFluids, boolean checkBrake) {
        super(description, basePrice);
        this.checkTires = checkTires;
        this.checkFluids = checkFluids;
        this.checkBrake = checkBrake;
    }

    public boolean isCheckTires() {
        return checkTires;
    }

    public void setCheckTires(boolean checkTires) {
        this.checkTires = checkTires;
    }

    public boolean isCheckFluids() {
        return checkFluids;
    }

    public void setCheckFluids(boolean checkFluids) {
        this.checkFluids = checkFluids;
    }

    public boolean isCheckBrake() {
        return checkBrake;
    }

    public void setCheckBrake(boolean checkBrake) {
        this.checkBrake = checkBrake;
    }
}
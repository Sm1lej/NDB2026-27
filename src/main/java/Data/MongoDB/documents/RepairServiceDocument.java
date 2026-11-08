package Data.MongoDB.documents;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "REPAIR")
public class RepairServiceDocument extends ServiceDocument {

    private boolean changeTires;
    private boolean fillFluids;
    private boolean fixBrakeSystem;

    public RepairServiceDocument() {
        super();
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

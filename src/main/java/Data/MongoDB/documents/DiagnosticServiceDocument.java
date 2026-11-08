package Data.MongoDB.documents;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "DIAGNOSTIC")
public class DiagnosticServiceDocument extends ServiceDocument {

    private boolean checkTires;
    private boolean checkFluids;
    private boolean checkBrake;

    public DiagnosticServiceDocument() {
        super();
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
package Data.document;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key = "_clazz", value = "MOTORBIKE")
public class MotorbikeDocument extends VehicleDocument {
    private String brakeSystem;

    public MotorbikeDocument() {
        super();
    }

    public String getBrakeSystem() {
        return brakeSystem;
    }
    public void setBrakeSystem(String brakeSystem) {
        this.brakeSystem = brakeSystem;
    }
}

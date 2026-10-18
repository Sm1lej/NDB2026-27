package model;

public class Invoice {

    private Integer id;
    private final Appointment appointment;
    private boolean isPaid = false;

    public Invoice(Appointment appointment) {
        if (appointment == null) {
            throw new RuntimeException("Wizyta nie może być nullem");
        }

        this.appointment = appointment;
    }

    public Invoice(Integer id, Appointment appointment) {
        this(appointment);
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Identyfikator faktury musi być większy od zera"
            );
        }
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;

        if (appointment != null && appointment.getServices() != null) {
            for (Service service : appointment.getServices()) {
                double basePrice = service.getPriceForClient();
                totalCost += appointment.getClient().getClientType().applyDiscount(basePrice);
            }
        }

        return totalCost;
    }

    public void markAsPaid() {
        if (isPaid) {
            throw new RuntimeException("Faktura jest już opłacona");
        }
        this.isPaid = true;
    }

    public String getInfo() {
        StringBuilder ss = new StringBuilder();
        ss.append("Faktura\n")
                .append("Id: ").append(getId()).append("\n")
                .append("Czy faktura jest opłacona: ").append(isPaid).append("\n");

        return ss.toString();
    }
}

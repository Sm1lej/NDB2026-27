package model;

public class Invoice {

    private final int id;
    private final Appointment appointment;
    private boolean isPaid = false;

    public Invoice(int id, Appointment appointment) {
        if (id <= 0) {
            throw new RuntimeException("Identyfikator faktury musi być większy od zera");
        }
        if (appointment == null) {
            throw new RuntimeException("Wizyta nie może być nullem");
        }

        this.id = id;
        this.appointment = appointment;
    }

    public int getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public boolean isPaid() {
        return isPaid;
    }

//    public double calculateTotalCost() {
//        double totalCost = 0.0;
//
//        if (appointment != null && appointment.getServices() != null) {
//            for (Service service : appointment.getServices()) {
//                double basePrice = service.getPriceForClient();
//                totalCost += appointment.getClient().getClientType().applyDiscount(basePrice);
//            }
//        }
//
//        return totalCost;
//    }

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

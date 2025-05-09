package Service;

import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class PagoService {
    private List<Pago> pagos;

    public PagoService() {
        this.pagos = new ArrayList<>();
    }

    public void registrarPago(Pago pago) {
        pagos.add(pago);
    }

    public List<Pago> getPagosPorVehiculo(String placa) {
        return pagos.stream()
                .filter(p -> p.getVehiculo().getPlaca().equalsIgnoreCase(placa))
                .toList();
    }

    public List<Pago> getPagosPorCliente(String cedula) {
        return pagos.stream()
                .filter(p -> p.getVehiculo().getCliente().getCedula().equals(cedula))
                .toList();
    }

    public double getTotalIngresosPorDia(LocalDate fecha) {
        return pagos.stream()
                .filter(p -> p.getFecha().toLocalDate().isEqual(fecha))
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public double getTotalIngresosPorMes(int año, int mes) {
        return pagos.stream()
                .filter(p -> p.getFecha().getYear() == año && p.getFecha().getMonthValue() == mes)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public double getTotalIngresosPorAño(int año) {
        return pagos.stream()
                .filter(p -> p.getFecha().getYear() == año)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public List<Pago> getAllPagos() {
        return new ArrayList<>(pagos);
    }
}
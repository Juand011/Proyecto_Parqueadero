package Service;

import model.*;
import java.util.List;
import java.util.ArrayList;

public class VehiculoService {
    private List<Vehiculo> vehiculos;

    public VehiculoService() {
        this.vehiculos = new ArrayList<>();
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
        vehiculo.getCliente().agregarVehiculo(vehiculo);
    }

    public boolean eliminarVehiculo(String placa) {
        return vehiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
    }

    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        return vehiculos.stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    public List<Vehiculo> buscarVehiculosPorTipo(String tipo) {
        return vehiculos.stream()
                .filter(v -> v.getTipo().equalsIgnoreCase(tipo))
                .toList();
    }

    public List<Vehiculo> buscarVehiculosPorCliente(String cedulaCliente) {
        return vehiculos.stream()
                .filter(v -> v.getCliente().getCedula().equals(cedulaCliente))
                .toList();
    }

    public boolean actualizarVehiculo(String placa, String nuevoColor, String nuevoModelo) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo != null) {
            vehiculo.setColor(nuevoColor);
            vehiculo.setModelo(nuevoModelo);
            return true;
        }
        return false;
    }

    public List<Vehiculo> getAllVehiculos() {
        return new ArrayList<>(vehiculos);
    }
}
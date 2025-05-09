package Service;

import model.*;
import java.time.*;          // Import para LocalDate, LocalDateTime, Duration
import java.time.format.*;   // Import específico para DateTimeFormatter
import java.util.*;
import java.util.stream.*;

public class ParqueaderoService {
    private Parqueadero parqueadero;
    private List<Cliente> clientes;
    private List<Vehiculo> vehiculos;
    private List<Pago> pagos;
    private List<Membresia> membresias;

    public ParqueaderoService(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
        this.clientes = new ArrayList<>();
        this.vehiculos = new ArrayList<>();
        this.pagos = new ArrayList<>();
        this.membresias = new ArrayList<>();
    }

    // ==================== MÉTODOS PARA CLIENTES ====================
    public void agregarCliente(Cliente cliente) {
        if (buscarClientePorCedula(cliente.getCedula()) == null) {
            clientes.add(cliente);
        } else {
            throw new IllegalArgumentException("Ya existe un cliente con esta cédula");
        }
    }

    public boolean eliminarCliente(String cedula) {
        Cliente cliente = buscarClientePorCedula(cedula);
        if (cliente != null) {
            // Eliminar primero los vehículos asociados
            List<Vehiculo> vehiculosCliente = buscarVehiculosPorCliente(cedula);
            vehiculos.removeAll(vehiculosCliente);
            
            return clientes.remove(cliente);
        }
        return false;
    }

    public Cliente buscarClientePorCedula(String cedula) {
        return clientes.stream()
                .filter(c -> c.getCedula().equals(cedula))
                .findFirst()
                .orElse(null);
    }

    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Cliente> buscarClientesPorTelefono(String telefono) {
        return clientes.stream()
                .filter(c -> c.getTelefono().equals(telefono))
                .collect(Collectors.toList());
    }

    public boolean actualizarCliente(String cedula, String nuevoTelefono, String nuevoCorreo) {
        Cliente cliente = buscarClientePorCedula(cedula);
        if (cliente != null) {
            cliente.setTelefono(nuevoTelefono);
            cliente.setCorreo(nuevoCorreo);
            return true;
        }
        return false;
    }

    // ==================== MÉTODOS PARA VEHÍCULOS ====================
    public void registrarVehiculo(Vehiculo vehiculo) {
        if (buscarVehiculoPorPlaca(vehiculo.getPlaca()) == null) {
            vehiculos.add(vehiculo);
            if (vehiculo.getCliente() != null) {
                vehiculo.getCliente().agregarVehiculo(vehiculo);
            }
        } else {
            throw new IllegalArgumentException("Ya existe un vehículo con esta placa");
        }
    }

    public boolean eliminarVehiculo(String placa) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo != null) {
            // Verificar si el vehículo está actualmente en el parqueadero
            if (vehiculo.isEnParqueadero()) {
                // Registrar salida automática si está en el parqueadero
                registrarSalida(vehiculo);
            }
            
            // Eliminar de la lista de vehículos
            vehiculos.remove(vehiculo);
            
            // Si tiene cliente asociado, removerlo de su lista
            if (vehiculo.getCliente() != null) {
                vehiculo.getCliente().removerVehiculo(vehiculo);
            }
            
            return true;
        }
        return false;
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
                .collect(Collectors.toList());
    }

    public List<Vehiculo> buscarVehiculosPorCliente(String cedulaCliente) {
        return vehiculos.stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getCedula().equals(cedulaCliente))
                .collect(Collectors.toList());
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

    // ==================== MÉTODOS PARA MEMBRESÍAS ====================
    public boolean asignarMembresia(String placa, String tipoMembresia) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo != null) {
            double costo = calcularCostoMembresia(vehiculo.getTipo(), tipoMembresia);
            Membresia membresia = new Membresia(tipoMembresia, costo);
            vehiculo.setMembresia(membresia);
            membresias.add(membresia);
            return true;
        }
        return false;
    }

    public boolean renovarMembresia(String placa, String tipoMembresia) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo != null && vehiculo.getMembresia() != null) {
            return asignarMembresia(placa, tipoMembresia);
        }
        return false;
    }

    private double calcularCostoMembresia(String tipoVehiculo, String tipoMembresia) {
        double tarifaBase = parqueadero.getTarifa(tipoVehiculo);
        int dias = 0;
        
        switch(tipoMembresia) {
            case "1 mes":
                dias = 30;
                tarifaBase *= 0.9; // 10% descuento
                break;
            case "3 meses":
                dias = 90;
                tarifaBase *= 0.85; // 15% descuento
                break;
            case "1 año":
                dias = 365;
                tarifaBase *= 0.8; // 20% descuento
                break;
        }
        
        return tarifaBase * dias;
    }

    // ==================== MÉTODOS PARA OPERACIONES ====================
    public boolean registrarIngreso(Vehiculo vehiculo) {
        if (vehiculo == null) return false;
        
        String tipoVehiculo = vehiculo.getTipo();
        if (parqueadero.getEspaciosDisponibles(tipoVehiculo) > 0) {
            if (vehiculo.getMembresia() != null && vehiculo.getMembresia().estaActiva()) {
                // Vehículo con membresía ocupa espacio permanente
                parqueadero.ocuparEspacio(tipoVehiculo);
            }
            
            vehiculo.setEnParqueadero(true);
            vehiculo.setHoraEntrada(LocalDateTime.now());
            return true;
        }
        return false;
    }

    public Pago registrarSalida(Vehiculo vehiculo) {
        if (vehiculo == null || !vehiculo.isEnParqueadero()) {
            return null;
        }
        
        vehiculo.setEnParqueadero(false);
        LocalDateTime horaSalida = LocalDateTime.now();
        double monto = calcularMonto(vehiculo, horaSalida);
        
        // Liberar espacio si no tiene membresía activa
        if (vehiculo.getMembresia() == null || !vehiculo.getMembresia().estaActiva()) {
            parqueadero.liberarEspacio(vehiculo.getTipo());
        }
        
        Pago pago = new Pago(vehiculo, monto, vehiculo.getHoraEntrada(), horaSalida);
        pagos.add(pago);
        return pago;
    }

    private double calcularMonto(Vehiculo vehiculo, LocalDateTime horaSalida) {
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia().estaActiva()) {
            return 0; // No paga por estacionamiento si tiene membresía activa
        }
        
        long horas = Duration.between(vehiculo.getHoraEntrada(), horaSalida).toHours();
        double tarifa = parqueadero.getTarifa(vehiculo.getTipo());
        return tarifa * (horas > 0 ? horas : 1); // Mínimo 1 hora
    }

    // ==================== MÉTODOS PARA REPORTES ====================
    public List<Vehiculo> getVehiculosEnParqueadero() {
        return vehiculos.stream()
                .filter(Vehiculo::isEnParqueadero)
                .collect(Collectors.toList());
    }

    public List<Cliente> getClientesConMembresiasActivas() {
        return clientes.stream()
                .filter(c -> c.getVehiculos().stream()
                        .anyMatch(v -> v.getMembresia() != null && v.getMembresia().estaActiva()))
                .collect(Collectors.toList());
    }

    public List<Cliente> getClientesConMembresiasPorVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        return clientes.stream()
                .filter(c -> c.getVehiculos().stream()
                        .anyMatch(v -> v.getMembresia() != null && 
                                v.getMembresia().getFechaFin().isBefore(hoy.plusDays(dias)) &&
                                v.getMembresia().getFechaFin().isAfter(hoy)))
                .collect(Collectors.toList());
    }

    public double getTotalIngresosPorDia(LocalDate fecha) {
        return pagos.stream()
                .filter(p -> p.getFecha().toLocalDate().equals(fecha))
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

    // ==================== MÉTODOS PARA ADMINISTRACIÓN ====================
    public Parqueadero getInfoParqueadero() {
        return this.parqueadero;
    }

    public boolean actualizarInfoParqueadero(String nombre, String direccion, 
                                           String representante, String telefono, 
                                           String correo) {
        parqueadero.setNombre(nombre);
        parqueadero.setDireccion(direccion);
        parqueadero.setRepresentante(representante);
        parqueadero.setTelefono(telefono);
        parqueadero.setCorreo(correo);
        return true;
    }

    public boolean actualizarTarifas(double tarifaAuto, double tarifaMoto, double tarifaCamion) {
        if (tarifaAuto < 0 || tarifaMoto < 0 || tarifaCamion < 0) {
            return false;
        }
        
        parqueadero.setTarifa("Automóvil", tarifaAuto);
        parqueadero.setTarifa("Moto", tarifaMoto);
        parqueadero.setTarifa("Camión", tarifaCamion);
        return true;
    }

    public boolean actualizarEspacios(int espaciosAuto, int espaciosMoto, int espaciosCamion) {
        if (espaciosAuto < 0 || espaciosMoto < 0 || espaciosCamion < 0) {
            return false;
        }
        
        parqueadero.setEspaciosDisponibles("Automóvil", espaciosAuto);
        parqueadero.setEspaciosDisponibles("Moto", espaciosMoto);
        parqueadero.setEspaciosDisponibles("Camión", espaciosCamion);
        return true;
    }

    public Map<String, Integer> getDisponibilidadEspacios() {
        Map<String, Integer> disponibilidad = new HashMap<>();
        disponibilidad.put("Automóvil", parqueadero.getEspaciosDisponibles("Automóvil"));
        disponibilidad.put("Moto", parqueadero.getEspaciosDisponibles("Moto"));
        disponibilidad.put("Camión", parqueadero.getEspaciosDisponibles("Camión"));
        return disponibilidad;
    }

    // ==================== MÉTODOS DE CONSULTA GENERAL ====================
    public List<Cliente> getAllClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Vehiculo> getAllVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public List<Pago> getAllPagos() {
        return new ArrayList<>(pagos);
    }

    public List<Membresia> getAllMembresias() {
        return new ArrayList<>(membresias);
    }
 // Dentro de la clase ParqueaderoService

    /**
     * Genera una factura por la compra/renewación de una membresía
     * @param placa Placa del vehículo
     * @return Factura en formato String o null si no se encuentra el vehículo o no tiene membresía
     */
    public String generarFacturaMembresia(String placa) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo == null || vehiculo.getMembresia() == null) {
            return null;
        }

        Membresia membresia = vehiculo.getMembresia();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        StringBuilder factura = new StringBuilder();
        factura.append("=== FACTURA DE MEMBRESÍA ===\n\n");
        factura.append("Parqueadero: ").append(parqueadero.getNombre()).append("\n");
        factura.append("Dirección: ").append(parqueadero.getDireccion()).append("\n");
        factura.append("Teléfono: ").append(parqueadero.getTelefono()).append("\n");
        factura.append("Fecha: ").append(LocalDateTime.now().format(formatter)).append("\n\n");
        
        factura.append("=== DATOS DEL CLIENTE ===\n");
        if (vehiculo.getCliente() != null) {
            factura.append("Nombre: ").append(vehiculo.getCliente().getNombre()).append("\n");
            factura.append("Cédula: ").append(vehiculo.getCliente().getCedula()).append("\n");
            factura.append("Teléfono: ").append(vehiculo.getCliente().getTelefono()).append("\n");
        } else {
            factura.append("Cliente: Temporal\n");
        }
        
        factura.append("\n=== DATOS DEL VEHÍCULO ===\n");
        factura.append("Placa: ").append(vehiculo.getPlaca()).append("\n");
        factura.append("Tipo: ").append(vehiculo.getTipo()).append("\n");
        factura.append("Color: ").append(vehiculo.getColor()).append("\n");
        factura.append("Modelo: ").append(vehiculo.getModelo()).append("\n");
        
        factura.append("\n=== DETALLE DE MEMBRESÍA ===\n");
        factura.append("Tipo: ").append(membresia.getTipo()).append("\n");
        factura.append("Fecha Inicio: ").append(membresia.getFechaInicio()).append("\n");
        factura.append("Fecha Fin: ").append(membresia.getFechaFin()).append("\n");
        factura.append("Costo Total: $").append(String.format("%,.2f", membresia.getCosto())).append("\n");
        
        factura.append("\nGracias por su preferencia!\n");
        factura.append("=== FIN DE FACTURA ===");
        
        return factura.toString();
    }

    /**
     * Genera una factura por estacionamiento por horas
     * @param placa Placa del vehículo
     * @return Factura en formato String o null si no se encuentra el vehículo o no tiene registro de pago
     */
    public String generarFacturaEstacionamiento(String placa) {
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            return null;
        }

        // Buscar el último pago asociado a este vehículo
        Optional<Pago> ultimoPago = pagos.stream()
                .filter(p -> p.getVehiculo().getPlaca().equalsIgnoreCase(placa))
                .max(Comparator.comparing(Pago::getFecha));

        if (!ultimoPago.isPresent()) {
            return null;
        }

        Pago pago = ultimoPago.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        StringBuilder factura = new StringBuilder();
        factura.append("=== FACTURA DE ESTACIONAMIENTO ===\n\n");
        factura.append("Parqueadero: ").append(parqueadero.getNombre()).append("\n");
        factura.append("Dirección: ").append(parqueadero.getDireccion()).append("\n");
        factura.append("Teléfono: ").append(parqueadero.getTelefono()).append("\n");
        factura.append("Fecha: ").append(pago.getFecha().format(formatter)).append("\n\n");
        
        factura.append("=== DATOS DEL CLIENTE ===\n");
        if (vehiculo.getCliente() != null) {
            factura.append("Nombre: ").append(vehiculo.getCliente().getNombre()).append("\n");
            factura.append("Cédula: ").append(vehiculo.getCliente().getCedula()).append("\n");
            factura.append("Teléfono: ").append(vehiculo.getCliente().getTelefono()).append("\n");
        } else {
            factura.append("Cliente: Temporal\n");
        }
        
        factura.append("\n=== DATOS DEL VEHÍCULO ===\n");
        factura.append("Placa: ").append(vehiculo.getPlaca()).append("\n");
        factura.append("Tipo: ").append(vehiculo.getTipo()).append("\n");
        factura.append("Color: ").append(vehiculo.getColor()).append("\n");
        factura.append("Modelo: ").append(vehiculo.getModelo()).append("\n");
        
        factura.append("\n=== DETALLE DE ESTACIONAMIENTO ===\n");
        factura.append("Hora de Ingreso: ").append(pago.getHoraEntrada().format(formatter)).append("\n");
        factura.append("Hora de Salida: ").append(pago.getHoraSalida().format(formatter)).append("\n");
        
        long horas = Duration.between(pago.getHoraEntrada(), pago.getHoraSalida()).toHours();
        long minutos = Duration.between(pago.getHoraEntrada(), pago.getHoraSalida()).toMinutesPart();
        
        factura.append("Tiempo estacionado: ").append(horas).append(" horas y ").append(minutos).append(" minutos\n");
        factura.append("Tarifa por hora: $").append(String.format("%,.2f", parqueadero.getTarifa(vehiculo.getTipo()))).append("\n");
        factura.append("Total a Pagar: $").append(String.format("%,.2f", pago.getMonto())).append("\n");
        
        factura.append("\nGracias por su preferencia!\n");
        factura.append("=== FIN DE FACTURA ===");
        
        return factura.toString();
    }

    /**
     * Método combinado que genera la factura según el tipo de servicio (membresía o estacionamiento)
     * @param placa Placa del vehículo
     * @param tipoFactura "membresia" o "estacionamiento"
     * @return Factura en formato String o mensaje de error si no se encuentra
     */
    public String generarFactura(String placa, String tipoFactura) {
        switch(tipoFactura.toLowerCase()) {
            case "membresia":
                return generarFacturaMembresia(placa);
            case "estacionamiento":
                return generarFacturaEstacionamiento(placa);
            default:
                return "Tipo de factura no válido. Use 'membresia' o 'estacionamiento'";
        }
    }
}
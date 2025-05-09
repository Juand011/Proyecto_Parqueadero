package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pago {
    private Vehiculo vehiculo;
    private double monto;
    private LocalDateTime fecha;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public Pago(Vehiculo vehiculo, double monto, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        this.vehiculo = vehiculo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    // Getters
    public Vehiculo getVehiculo() { return vehiculo; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public LocalDateTime getHoraSalida() { return horaSalida; }

    public String generarFactura() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder factura = new StringBuilder();
        
        factura.append("FACTURA DE PARQUEADERO\n");
        factura.append("----------------------\n");
        
        factura.append("Datos del Cliente:\n");
        factura.append("Nombre: ").append(vehiculo.getCliente().getNombre()).append("\n");
        factura.append("Cédula: ").append(vehiculo.getCliente().getCedula()).append("\n\n");
        
        factura.append("Datos del Vehículo:\n");
        factura.append("Placa: ").append(vehiculo.getPlaca()).append("\n");
        factura.append("Tipo: ").append(vehiculo.getTipo()).append("\n");
        factura.append("Color: ").append(vehiculo.getColor()).append("\n");
        factura.append("Modelo: ").append(vehiculo.getModelo()).append("\n\n");
        
        if (vehiculo.getMembresia() != null && vehiculo.getMembresia().estaActiva()) {
            factura.append("Membresía Activa\n");
            factura.append("Tipo: ").append(vehiculo.getMembresia().getTipo()).append("\n");
            factura.append("Fecha Inicio: ").append(vehiculo.getMembresia().getFechaInicio()).append("\n");
            factura.append("Fecha Fin: ").append(vehiculo.getMembresia().getFechaFin()).append("\n");
            factura.append("Costo: $").append(String.format("%,.2f", vehiculo.getMembresia().getCosto())).append("\n\n");
        } else {
            factura.append("Ingreso Temporal\n");
            factura.append("Hora Entrada: ").append(horaEntrada.format(formatter)).append("\n");
            factura.append("Hora Salida: ").append(horaSalida.format(formatter)).append("\n");
            factura.append("Total a Pagar: $").append(String.format("%,.2f", monto)).append("\n\n");
        }
        
        factura.append("Fecha: ").append(fecha.format(formatter)).append("\n");
        factura.append("Gracias por su preferencia!");
        
        return factura.toString();
    }
}
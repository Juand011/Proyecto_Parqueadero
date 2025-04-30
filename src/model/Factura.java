package model;

import java.util.Date;

public class Factura {
    private String numero;
    private Date fecha;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private double subtotal;
    private double iva;
    private double total;

    public Factura(Cliente cliente, Vehiculo vehiculo, double monto) {
        this.numero = "FAC-" + System.currentTimeMillis();
        this.fecha = new Date();
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.subtotal = monto / 1.19; // Asumiendo 19% IVA
        this.iva = monto - subtotal;
        this.total = monto;
    }

    public String generarFactura() {
        StringBuilder sb = new StringBuilder();
        sb.append("FACTURA N°: ").append(numero).append("\n");
        sb.append("Fecha: ").append(fecha.toString()).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Cédula: ").append(cliente.getCedula()).append("\n");
        sb.append("Vehículo: ").append(vehiculo.getPlaca()).append("\n");
        sb.append("--------------------------------\n");
        sb.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        sb.append("IVA (19%): $").append(String.format("%.2f", iva)).append("\n");
        sb.append("TOTAL: $").append(String.format("%.2f", total)).append("\n");
        sb.append("--------------------------------\n");
        sb.append("Gracias por su preferencia!");
        
        return sb.toString();
    }
}
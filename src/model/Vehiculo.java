package model;

import java.time.LocalDateTime;

public abstract class Vehiculo {
    protected String placa;
    protected String color;
    protected String modelo;
    protected Cliente cliente;
    protected Membresia membresia;
    protected boolean enParqueadero;
    protected LocalDateTime horaEntrada;

    public Vehiculo(String placa, String color, String modelo, Cliente cliente) {
        this.placa = placa;
        this.color = color;
        this.modelo = modelo;
        this.cliente = cliente;
        this.enParqueadero = false;
    }

    // Getters y Setters
    public String getPlaca() { return placa; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public Cliente getCliente() { return cliente; }
    
    public Membresia getMembresia() { return membresia; }
    public void setMembresia(Membresia membresia) { this.membresia = membresia; }
    
    public boolean isEnParqueadero() { return enParqueadero; }
    public void setEnParqueadero(boolean enParqueadero) { this.enParqueadero = enParqueadero; }
    
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalDateTime horaEntrada) { this.horaEntrada = horaEntrada; }
    
    public abstract String getTipo();
}
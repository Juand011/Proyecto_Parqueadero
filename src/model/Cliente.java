package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public String nombre;
    public String cedula;
    public String telefono;
    private String correo;
    private List<Vehiculo> vehiculos;

    public Cliente(String nombre, String cedula, String telefono, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.vehiculos = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCedula() { return cedula; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public List<Vehiculo> getVehiculos() { return vehiculos; }
    
    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }
    
    public void removerVehiculo(Vehiculo vehiculo) {
        vehiculos.remove(vehiculo);
    }
}
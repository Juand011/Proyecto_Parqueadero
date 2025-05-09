package model;

import java.util.HashMap;
import java.util.Map;

public class Parqueadero {
    private String nombre;
    private String direccion;
    private String representante;
    private String telefono;
    private String correo;
    
    private Map<String, Double> tarifas;
    private Map<String, Integer> espacios;
    private Map<String, Integer> espaciosOcupados;

    public Parqueadero(String nombre, String direccion, String representante, String telefono, String correo) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.representante = representante;
        this.telefono = telefono;
        this.correo = correo;
        
        this.tarifas = new HashMap<>();
        this.espacios = new HashMap<>();
        this.espaciosOcupados = new HashMap<>();
        
        // Valores por defecto
        tarifas.put("Automóvil", 2000.0);
        tarifas.put("Moto", 1000.0);
        tarifas.put("Camión", 3000.0);
        
        espacios.put("Automóvil", 20);
        espacios.put("Moto", 30);
        espacios.put("Camión", 10);
        
        espaciosOcupados.put("Automóvil", 0);
        espaciosOcupados.put("Moto", 0);
        espaciosOcupados.put("Camión", 0);
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getRepresentante() { return representante; }
    public void setRepresentante(String representante) { this.representante = representante; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    // Métodos para tarifas
    public void setTarifa(String tipoVehiculo, double tarifa) {
        tarifas.put(tipoVehiculo, tarifa);
    }
    
    public double getTarifa(String tipoVehiculo) {
        return tarifas.getOrDefault(tipoVehiculo, 0.0);
    }
    
    // Métodos para espacios
    public void setEspaciosDisponibles(String tipoVehiculo, int cantidad) {
        espacios.put(tipoVehiculo, cantidad);
    }
    
    public int getEspaciosDisponibles(String tipoVehiculo) {
        return espacios.getOrDefault(tipoVehiculo, 0) - espaciosOcupados.getOrDefault(tipoVehiculo, 0);
    }
    
    public boolean ocuparEspacio(String tipoVehiculo) {
        if (getEspaciosDisponibles(tipoVehiculo) > 0) {
            espaciosOcupados.put(tipoVehiculo, espaciosOcupados.get(tipoVehiculo) + 1);
            return true;
        }
        return false;
    }
    
    public void liberarEspacio(String tipoVehiculo) {
        if (espaciosOcupados.get(tipoVehiculo) > 0) {
            espaciosOcupados.put(tipoVehiculo, espaciosOcupados.get(tipoVehiculo) - 1);
        }
    }
}
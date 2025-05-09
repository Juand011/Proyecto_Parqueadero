package model;

import java.time.LocalDate;

public class Membresia {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double costo;
    private String tipo; // "1 mes", "3 meses", "1 año"

    public Membresia(String tipo, double costo) {
        this.tipo = tipo;
        this.costo = costo;
        this.fechaInicio = LocalDate.now();
        
        switch(tipo) {
            case "1 mes":
                this.fechaFin = fechaInicio.plusMonths(1);
                break;
            case "3 meses":
                this.fechaFin = fechaInicio.plusMonths(3);
                break;
            case "1 año":
                this.fechaFin = fechaInicio.plusYears(1);
                break;
        }
    }

    // Getters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getCosto() { return costo; }
    public String getTipo() { return tipo; }
    
    public boolean estaActiva() {
        return LocalDate.now().isBefore(fechaFin) || LocalDate.now().isEqual(fechaFin);
    }
}
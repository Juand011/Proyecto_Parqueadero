package model;

public class Camion extends Vehiculo {
    public Camion(String placa, String color, String modelo, Cliente cliente) {
        super(placa, color, modelo, cliente);
    }

    @Override
    public String getTipo() {
        return "Camión";
    }
}
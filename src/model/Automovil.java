package model;

public class Automovil extends Vehiculo {
    public Automovil(String placa, String color, String modelo, Cliente cliente) {
        super(placa, color, modelo, cliente);
    }

    @Override
    public String getTipo() {
        return "Automóvil";
    }
}
package model;

public class Moto extends Vehiculo {
    public Moto(String placa, String color, String modelo, Cliente cliente) {
        super(placa, color, modelo, cliente);
    }

    @Override
    public String getTipo() {
        return "Moto";
    }
}
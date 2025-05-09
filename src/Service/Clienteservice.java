package Service;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class Clienteservice {
    private List<Cliente> clientes;

    public Clienteservice() {
        this.clientes = new ArrayList<>();
    }

    public void agregarCliente(Cliente cliente) {
        if (buscarClientePorCedula(cliente.getCedula()) == null) {
            clientes.add(cliente);
        } else {
            throw new IllegalArgumentException("Ya existe un cliente con esta cédula");
        }
    }

    public boolean eliminarCliente(String cedula) {
        return clientes.removeIf(c -> c.getCedula().equals(cedula));
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
                .toList();
    }

    public List<Cliente> buscarClientesPorTelefono(String telefono) {
        return clientes.stream()
                .filter(c -> c.getTelefono().equals(telefono))
                .toList();
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

    public List<Cliente> getAllClientes() {
        return new ArrayList<>(clientes);
    }
}
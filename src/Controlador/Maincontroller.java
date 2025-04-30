package Controlador;

import model.*;
import service.*;
import javax.swing.*;
import java.util.List;

public class Maincontroller {
    private final ParqueaderoService parqueaderoService;
    private final PagoService pagoService;
    private final ReporteService reporteService;

    public Maincontroller() {
        this.parqueaderoService = new ParqueaderoService();
        this.pagoService = new PagoService();
        this.reporteService = new ReporteService();
    }

    // --- Método principal que inicia la aplicación ---
    public void iniciar() {
        configurarParqueadero(); // Configuración inicial
        mostrarMenuPrincipal();
    }

    // --- Menú Principal ---
    private void mostrarMenuPrincipal() {
        String[] opciones = {
            "Gestión de Clientes",
            "Gestión de Vehículos",
            "Registrar Ingreso/Salida",
            "Pagos y Membresías",
            "Reportes y Consultas",
            "Salir"
        };

        while (true) {
            String opcion = (String) JOptionPane.showInputDialog(
                null,
                "SISTEMA DE PARQUEADERO\n\nSeleccione una opción:",
                "Menú Principal",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (opcion == null || opcion.equals("Salir")) break;

            switch (opcion) {
                case "Gestión de Clientes" -> gestionarClientes();
                case "Gestión de Vehículos" -> gestionarVehiculos();
                case "Registrar Ingreso/Salida" -> gestionarIngresosSalidas();
                case "Pagos y Membresías" -> gestionarPagos();
                case "Reportes y Consultas" -> generarReportes();
            }
        }
    }

    // --- 1. Gestión de Clientes ---
    private void gestionarClientes() {
        String[] opciones = {
            "Registrar Cliente",
            "Buscar Cliente",
            "Actualizar Cliente",
            "Eliminar Cliente",
            "Volver"
        };

        String opcion = (String) JOptionPane.showInputDialog(
            null,
            "GESTIÓN DE CLIENTES\n\nSeleccione una acción:",
            "Clientes",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (opcion == null || opcion.equals("Volver")) return;

        switch (opcion) {
            case "Registrar Cliente" -> registrarCliente();
            case "Buscar Cliente" -> buscarCliente();
            case "Actualizar Cliente" -> actualizarCliente();
            case "Eliminar Cliente" -> eliminarCliente();
        }
    }

    private void registrarCliente() {
        String nombre = JOptionPane.showInputDialog("Nombre completo:");
        String cedula = JOptionPane.showInputDialog("Cédula:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String correo = JOptionPane.showInputDialog("Correo electrónico:");

        if (nombre != null && cedula != null) {
            parqueaderoService.registrarCliente(nombre, cedula, telefono, correo);
            mostrarMensaje("Cliente registrado exitosamente", "Éxito");
        }
    }

    private void buscarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
        Cliente cliente = parqueaderoService.buscarClientePorCedula(cedula);

        if (cliente != null) {
            mostrarMensaje(cliente.toString(), "Datos del Cliente");
        } else {
            mostrarError("Cliente no encontrado");
        }
    }

    private void actualizarCliente() {
        String cedula = JOptionPane.showInputDialog("Cédula del cliente a actualizar:");
        Cliente cliente = parqueaderoService.buscarClientePorCedula(cedula);

        if (cliente != null) {
            String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", cliente.getTelefono());
            String nuevoCorreo = JOptionPane.showInputDialog("Nuevo correo:", cliente.getCorreo());

            cliente.setTelefono(nuevoTelefono);
            cliente
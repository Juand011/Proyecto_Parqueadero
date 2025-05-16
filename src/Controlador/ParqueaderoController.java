package Controlador;

import javax.swing.JFrame;
import model.*;
import Service.ParqueaderoService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParqueaderoController {
    private ParqueaderoService parqueaderoService;

    public ParqueaderoController() {
        // Inicializar con datos de ejemplo
        Parqueadero parqueadero = new Parqueadero(
            "Parqueadero Central", 
            "Calle 15 #10-20, Armenia", 
            "Juan Pérez", 
            "3001234567", 
            "contacto@parqueaderocentral.com"
        );
        
        // Configurar espacios y tarifas por defecto
        parqueadero.setEspaciosDisponibles("Automóvil", 50);
        parqueadero.setEspaciosDisponibles("Moto", 30);
        parqueadero.setEspaciosDisponibles("Camión", 10);
        
        parqueadero.setTarifa("Automóvil", 2000);
        parqueadero.setTarifa("Moto", 1000);
        parqueadero.setTarifa("Camión", 3000);
        
        this.parqueaderoService = new ParqueaderoService(parqueadero);
        
        // Datos de prueba
        inicializarDatosPrueba();
    }

    private void inicializarDatosPrueba() {
        // Clientes de prueba
        Cliente cliente1 = new Cliente("Carlos Gómez", "123456789", "3101234567", "carlos@email.com");
        Cliente cliente2 = new Cliente("María Rodríguez", "987654321", "3207654321", "maria@email.com");
        
        parqueaderoService.agregarCliente(cliente1);
        parqueaderoService.agregarCliente(cliente2);
        
        // Vehículos de prueba
        Vehiculo auto1 = new Automovil("ABC123", "Rojo", "2020", cliente1);
        Vehiculo moto1 = new Moto("XYZ987", "Negro", "2021", cliente1);
        Vehiculo camion1 = new Camion("DEF456", "Blanco", "2019", cliente2);
        
        parqueaderoService.registrarVehiculo(auto1);
        parqueaderoService.registrarVehiculo(moto1);
        parqueaderoService.registrarVehiculo(camion1);
        
        // Membresías de prueba
        parqueaderoService.asignarMembresia("ABC123", "1 mes");
    }

    public void iniciar() {
        boolean salir = false;
        
        while (!salir) {
            String opcion = mostrarMenuPrincipal();
            
            switch (opcion) {
                case "1":
                    gestionClientes();
                    break;
                case "2":
                    gestionVehiculos();
                    break;
                case "3":
                    administrarParqueadero();
                    break;
                case "4":
                    operacionesParqueadero();
                    break;
                case "5":
                    generarReportes();
                    break;
                case "6":
                    salir = true;
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        }
        
        mostrarMensaje("¡Gracias por usar el sistema de parqueadero!");
    }
    
    // ==================== MÉTODOS PARA CLIENTES ====================
    private void gestionClientes() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                "Gestión de Clientes\n\n" +
                "1. Registrar nuevo cliente\n" +
                "2. Buscar cliente\n" +
                "3. Actualizar cliente\n" +
                "4. Eliminar cliente\n" +
                "5. Ver vehículos de cliente\n" +
                "6. Volver al menú principal\n\n" +
                "Seleccione una opción:"
            );
            
            switch (opcion) {
                case "1":
                    registrarCliente();
                    break;
                case "2":
                    buscarCliente();
                    break;
                case "3":
                    actualizarCliente();
                    break;
                case "4":
                    eliminarCliente();
                    break;
                case "5":
                    verVehiculosCliente();
                    break;
                case "6":
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        } while (!opcion.equals("6"));
    }
    
    private void registrarCliente() {
        try {
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
            String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
            String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del cliente:");
            String correo = JOptionPane.showInputDialog("Ingrese el correo del cliente:");
            
            Cliente cliente = new Cliente(nombre, cedula, telefono, correo);
            parqueaderoService.agregarCliente(cliente);
            mostrarMensaje("Cliente registrado exitosamente");
        } catch (Exception e) {
            mostrarMensaje("Error: " + e.getMessage());
        }
    }
    
    private void buscarCliente() {
        String criterio = JOptionPane.showInputDialog(
            "Buscar cliente por:\n" +
            "1. Cédula\n" +
            "2. Nombre\n" +
            "3. Teléfono\n" +
            "Seleccione una opción:"
        );
        
        String busqueda = JOptionPane.showInputDialog("Ingrese el término de búsqueda:");
        List<Cliente> resultados = new ArrayList<>();
        
        switch (criterio) {
            case "1":
                Cliente cliente = parqueaderoService.buscarClientePorCedula(busqueda);
                if (cliente != null) resultados.add(cliente);
                break;
            case "2":
                resultados = parqueaderoService.buscarClientesPorNombre(busqueda);
                break;
            case "3":
                resultados = parqueaderoService.buscarClientesPorTelefono(busqueda);
                break;
            default:
                mostrarMensaje("Opción no válida");
                return;
        }
        
        if (resultados.isEmpty()) {
            mostrarMensaje("No se encontraron clientes");
        } else {
            StringBuilder mensaje = new StringBuilder("Clientes encontrados:\n\n");
            for (Cliente c : resultados) {
                mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    private void actualizarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a actualizar:");
        Cliente cliente = parqueaderoService.buscarClientePorCedula(cedula);
        
        if (cliente == null) {
            mostrarMensaje("Cliente no encontrado");
            return;
        }
        
        String nuevoTelefono = JOptionPane.showInputDialog("Nuevo teléfono:", cliente.getTelefono());
        String nuevoCorreo = JOptionPane.showInputDialog("Nuevo correo:", cliente.getCorreo());
        
        if (parqueaderoService.actualizarCliente(cedula, nuevoTelefono, nuevoCorreo)) {
            mostrarMensaje("Cliente actualizado exitosamente");
        } else {
            mostrarMensaje("Error al actualizar cliente");
        }
    }
    
    private void eliminarCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente a eliminar:");
        
        if (parqueaderoService.eliminarCliente(cedula)) {
            mostrarMensaje("Cliente eliminado exitosamente");
        } else {
            mostrarMensaje("Cliente no encontrado");
        }
    }
    
    private void verVehiculosCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
        List<Vehiculo> vehiculos = parqueaderoService.buscarVehiculosPorCliente(cedula);
        
        if (vehiculos.isEmpty()) {
            mostrarMensaje("El cliente no tiene vehículos registrados");
        } else {
            StringBuilder mensaje = new StringBuilder("Vehículos del cliente:\n\n");
            for (Vehiculo v : vehiculos) {
                mensaje.append("Placa: ").append(v.getPlaca()).append("\n");
                mensaje.append("Tipo: ").append(v.getTipo()).append("\n");
                mensaje.append("Color: ").append(v.getColor()).append("\n");
                mensaje.append("Modelo: ").append(v.getModelo()).append("\n");
                
                if (v.getMembresia() != null) {
                    mensaje.append("Membresía: ").append(v.getMembresia().getTipo()).append("\n");
                    mensaje.append("Estado: ").append(v.getMembresia().estaActiva() ? "Activa" : "Vencida").append("\n");
                    mensaje.append("Vence: ").append(v.getMembresia().getFechaFin()).append("\n");
                }
                mensaje.append("\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    // ==================== MÉTODOS PARA VEHÍCULOS ====================
    private void gestionVehiculos() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                "Gestión de Vehículos\n\n" +
                "1. Registrar vehículo\n" +
                "2. Buscar vehículo\n" +
                "3. Actualizar vehículo\n" +
                "4. Eliminar vehículo\n" +
                "5. Asignar/renovar membresía\n" +
                "6. Generar factura de membresía\n" +
                "7. Volver al menú principal\n\n" +
                "Seleccione una opción:"
            );
            
            switch (opcion) {
                case "1":
                    registrarVehiculo();
                    break;
                case "2":
                    buscarVehiculo();
                    break;
                case "3":
                    actualizarVehiculo();
                    break;
                case "4":
                    eliminarVehiculo();
                    break;
                case "5":
                    gestionMembresias();
                    break;
                case "6":
                    generarFacturaMembresia();
                    break;
                case "7":
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        } while (!opcion.equals("7"));
    }
    
    private void registrarVehiculo() {
        try {
            String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente (deje vacío para vehículo temporal):");
            Cliente cliente = null;
            
            if (!cedula.isEmpty()) {
                cliente = parqueaderoService.buscarClientePorCedula(cedula);
                if (cliente == null) {
                    mostrarMensaje("Cliente no encontrado");
                    return;
                }
            }
            
            String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
            String tipo = JOptionPane.showInputDialog("Tipo de vehículo (Automóvil/Moto/Camión):");
            String color = JOptionPane.showInputDialog("Color del vehículo:");
            String modelo = JOptionPane.showInputDialog("Modelo del vehículo:");
            
            Vehiculo vehiculo;
            switch(tipo.toLowerCase()) {
                case "automóvil":
                case "auto":
                    vehiculo = new Automovil(placa, color, modelo, cliente);
                    break;
                case "moto":
                    vehiculo = new Moto(placa, color, modelo, cliente);
                    break;
                case "camión":
                case "camion":
                    vehiculo = new Camion(placa, color, modelo, cliente);
                    break;
                default:
                    mostrarMensaje("Tipo de vehículo no válido");
                    return;
            }
            
            parqueaderoService.registrarVehiculo(vehiculo);
            mostrarMensaje("Vehículo registrado exitosamente");
        } catch (Exception e) {
            mostrarMensaje("Error: " + e.getMessage());
        }
    }
    
    private void buscarVehiculo() {
        String criterio = JOptionPane.showInputDialog(
            "Buscar vehículo por:\n" +
            "1. Placa\n" +
            "2. Tipo\n" +
            "3. Cliente\n" +
            "Seleccione una opción:"
        );
        
        String busqueda = JOptionPane.showInputDialog("Ingrese el término de búsqueda:");
        List<Vehiculo> resultados = new ArrayList<>();
        
        switch (criterio) {
            case "1":
                Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(busqueda);
                if (vehiculo != null) resultados.add(vehiculo);
                break;
            case "2":
                resultados = parqueaderoService.buscarVehiculosPorTipo(busqueda);
                break;
            case "3":
                resultados = parqueaderoService.buscarVehiculosPorCliente(busqueda);
                break;
            default:
                mostrarMensaje("Opción no válida");
                return;
        }
        
        if (resultados.isEmpty()) {
            mostrarMensaje("No se encontraron vehículos");
        } else {
            StringBuilder mensaje = new StringBuilder("Vehículos encontrados:\n\n");
            for (Vehiculo v : resultados) {
                mensaje.append("Placa: ").append(v.getPlaca()).append("\n");
                mensaje.append("Tipo: ").append(v.getTipo()).append("\n");
                mensaje.append("Color: ").append(v.getColor()).append("\n");
                mensaje.append("Modelo: ").append(v.getModelo()).append("\n");
                
                if (v.getCliente() != null) {
                    mensaje.append("Cliente: ").append(v.getCliente().getNombre()).append("\n");
                } else {
                    mensaje.append("Cliente: Temporal\n");
                }
                mensaje.append("\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    private void actualizarVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a actualizar:");
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
        
        if (vehiculo == null) {
            mostrarMensaje("Vehículo no encontrado");
            return;
        }
        
        String nuevoColor = JOptionPane.showInputDialog("Nuevo color:", vehiculo.getColor());
        String nuevoModelo = JOptionPane.showInputDialog("Nuevo modelo:", vehiculo.getModelo());
        
        if (parqueaderoService.actualizarVehiculo(placa, nuevoColor, nuevoModelo)) {
            mostrarMensaje("Vehículo actualizado exitosamente");
        } else {
            mostrarMensaje("Error al actualizar vehículo");
        }
    }
    
    private void eliminarVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a eliminar:");
        
        if (parqueaderoService.eliminarVehiculo(placa)) {
            mostrarMensaje("Vehículo eliminado exitosamente");
        } else {
            mostrarMensaje("Vehículo no encontrado");
        }
    }
    
    private void gestionMembresias() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
        
        if (vehiculo == null) {
            mostrarMensaje("Vehículo no encontrado");
            return;
        }
        
        String opcion = JOptionPane.showInputDialog(
            "Vehículo: " + vehiculo.getPlaca() + "\n" +
            "Tipo: " + vehiculo.getTipo() + "\n\n" +
            "1. Asignar nueva membresía\n" +
            "2. Renovar membresía existente\n" +
            "3. Generar factura de membresía\n" +
            "4. Cancelar\n" +
            "Seleccione una opción:"
        );
        
        if (opcion.equals("1") || opcion.equals("2")) {
            String tipoMembresia = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de membresía:",
                "Membresía",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"1 mes", "3 meses", "1 año"},
                "1 mes"
            );
            
            if (tipoMembresia != null) {
                boolean resultado = opcion.equals("1") ? 
                    parqueaderoService.asignarMembresia(placa, tipoMembresia) :
                    parqueaderoService.renovarMembresia(placa, tipoMembresia);
                
                if (resultado) {
                    String factura = parqueaderoService.generarFacturaMembresia(placa);
                    mostrarFactura(factura);
                } else {
                    mostrarMensaje("Error al procesar la membresía");
                }
            }
        } else if (opcion.equals("3")) {
            generarFacturaMembresia(placa);
        }
    }
    
    // ==================== MÉTODOS PARA ADMINISTRACIÓN ====================
    private void administrarParqueadero() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                "Administración del Parqueadero\n\n" +
                "1. Ver información del parqueadero\n" +
                "2. Actualizar información\n" +
                "3. Configurar tarifas\n" +
                "4. Configurar espacios\n" +
                "5. Volver al menú principal\n\n" +
                "Seleccione una opción:"
            );
            
            switch (opcion) {
                case "1":
                    mostrarInfoParqueadero();
                    break;
                case "2":
                    actualizarInfoParqueadero();
                    break;
                case "3":
                    configurarTarifas();
                    break;
                case "4":
                    configurarEspacios();
                    break;
                case "5":
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        } while (!opcion.equals("5"));
    }
    
    private void mostrarInfoParqueadero() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        StringBuilder mensaje = new StringBuilder("Información del Parqueadero\n\n");
        mensaje.append("Nombre: ").append(parqueadero.getNombre()).append("\n");
        mensaje.append("Dirección: ").append(parqueadero.getDireccion()).append("\n");
        mensaje.append("Representante: ").append(parqueadero.getRepresentante()).append("\n");
        mensaje.append("Teléfono: ").append(parqueadero.getTelefono()).append("\n");
        mensaje.append("Correo: ").append(parqueadero.getCorreo()).append("\n\n");
        
        mensaje.append("Tarifas actuales:\n");
        mensaje.append("- Automóvil: $").append(parqueadero.getTarifa("Automóvil")).append(" por hora\n");
        mensaje.append("- Moto: $").append(parqueadero.getTarifa("Moto")).append(" por hora\n");
        mensaje.append("- Camión: $").append(parqueadero.getTarifa("Camión")).append(" por hora\n\n");
        
        mensaje.append("Espacios disponibles:\n");
        mensaje.append("- Automóviles: ").append(espacios.get("Automóvil")).append("\n");
        mensaje.append("- Motos: ").append(espacios.get("Moto")).append("\n");
        mensaje.append("- Camiones: ").append(espacios.get("Camión")).append("\n");
        
        mostrarMensaje(mensaje.toString());
    }
    
    private void actualizarInfoParqueadero() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        
        String nombre = JOptionPane.showInputDialog("Nombre:", parqueadero.getNombre());
        String direccion = JOptionPane.showInputDialog("Dirección:", parqueadero.getDireccion());
        String representante = JOptionPane.showInputDialog("Representante:", parqueadero.getRepresentante());
        String telefono = JOptionPane.showInputDialog("Teléfono:", parqueadero.getTelefono());
        String correo = JOptionPane.showInputDialog("Correo:", parqueadero.getCorreo());
        
        if (parqueaderoService.actualizarInfoParqueadero(nombre, direccion, representante, telefono, correo)) {
            mostrarMensaje("Información actualizada exitosamente");
        } else {
            mostrarMensaje("Error al actualizar información");
        }
    }
    
    private void configurarTarifas() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        
        try {
            double tarifaAuto = Double.parseDouble(JOptionPane.showInputDialog(
                "Tarifa para automóviles por hora:", parqueadero.getTarifa("Automóvil")));
            double tarifaMoto = Double.parseDouble(JOptionPane.showInputDialog(
                "Tarifa para motos por hora:", parqueadero.getTarifa("Moto")));
            double tarifaCamion = Double.parseDouble(JOptionPane.showInputDialog(
                "Tarifa para camiones por hora:", parqueadero.getTarifa("Camión")));
            
            if (parqueaderoService.actualizarTarifas(tarifaAuto, tarifaMoto, tarifaCamion)) {
                mostrarMensaje("Tarifas actualizadas exitosamente");
            } else {
                mostrarMensaje("Error al actualizar tarifas");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numéricos válidos");
        }
    }
    
    private void configurarEspacios() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        try {
            int espaciosAuto = Integer.parseInt(JOptionPane.showInputDialog(
                "Espacios para automóviles:", espacios.get("Automóvil")));
            int espaciosMoto = Integer.parseInt(JOptionPane.showInputDialog(
                "Espacios para motos:", espacios.get("Moto")));
            int espaciosCamion = Integer.parseInt(JOptionPane.showInputDialog(
                "Espacios para camiones:", espacios.get("Camión")));
            
            if (parqueaderoService.actualizarEspacios(espaciosAuto, espaciosMoto, espaciosCamion)) {
                mostrarMensaje("Espacios actualizados exitosamente");
            } else {
                mostrarMensaje("Error al actualizar espacios");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numéricos válidos");
        }
    }
    
    // ==================== MÉTODOS PARA OPERACIONES ====================
    private void operacionesParqueadero() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                "Operaciones de Parqueadero\n\n" +
                "1. Registrar ingreso de vehículo\n" +
                "2. Registrar salida de vehículo\n" +
                "3. Ver vehículos actuales\n" +
                "4. Generar factura de estacionamiento\n" +
                "5. Ver disponibilidad de espacios\n" +
                "6. Volver al menú principal\n\n" +
                "Seleccione una opción:"
            );
            
            switch (opcion) {
                case "1":
                    registrarIngresoVehiculo();
                    break;
                case "2":
                    registrarSalidaVehiculo();
                    break;
                case "3":
                    mostrarVehiculosActuales();
                    break;
                case "4":
                    generarFacturaEstacionamiento();
                    break;
                case "5":
                    mostrarDisponibilidadEspacios();
                    break;
                case "6":
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        } while (!opcion.equals("6"));
    }
    
    private void registrarIngresoVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
        
        if (vehiculo == null) {
            int respuesta = JOptionPane.showConfirmDialog(
                null,
                "Vehículo no registrado. ¿Desea registrarlo como temporal?",
                "Vehículo no encontrado",
                JOptionPane.YES_NO_OPTION
            );
            
            if (respuesta == JOptionPane.YES_OPTION) {
                String tipo = JOptionPane.showInputDialog("Tipo de vehículo (Automóvil/Moto/Camión):");
                
                switch(tipo.toLowerCase()) {
                    case "automóvil":
                    case "auto":
                        vehiculo = new Automovil(placa, "Temporal", "Temporal", null);
                        break;
                    case "moto":
                        vehiculo = new Moto(placa, "Temporal", "Temporal", null);
                        break;
                    case "camión":
                    case "camion":
                        vehiculo = new Camion(placa, "Temporal", "Temporal", null);
                        break;
                    default:
                        mostrarMensaje("Tipo de vehículo no válido");
                        return;
                }
                
                parqueaderoService.registrarVehiculo(vehiculo);
            } else {
                return;
            }
        }
        
        if (parqueaderoService.registrarIngreso(vehiculo)) {
            mostrarMensaje("Ingreso registrado exitosamente\nPlaca: " + vehiculo.getPlaca() + "\nHora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        } else {
            mostrarMensaje("No hay espacios disponibles para este tipo de vehículo");
        }
    }
    
    private void registrarSalidaVehiculo() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
        
        if (vehiculo == null || !vehiculo.isEnParqueadero()) {
            mostrarMensaje("Vehículo no encontrado o no está en el parqueadero");
            return;
        }
        
        Pago pago = parqueaderoService.registrarSalida(vehiculo);
        if (pago != null) {
            String factura = parqueaderoService.generarFacturaEstacionamiento(placa);
            mostrarFactura(factura);
        } else {
            mostrarMensaje("Error al registrar salida");
        }
    }
    
    private void mostrarVehiculosActuales() {
        List<Vehiculo> vehiculos = parqueaderoService.getVehiculosEnParqueadero();
        
        if (vehiculos.isEmpty()) {
            mostrarMensaje("No hay vehículos en el parqueadero actualmente");
        } else {
            StringBuilder mensaje = new StringBuilder("Vehículos en el parqueadero:\n\n");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (Vehiculo v : vehiculos) {
                mensaje.append("Placa: ").append(v.getPlaca()).append("\n");
                mensaje.append("Tipo: ").append(v.getTipo()).append("\n");
                mensaje.append("Hora ingreso: ").append(v.getHoraEntrada().format(formatter)).append("\n");
                
                if (v.getCliente() != null) {
                    mensaje.append("Cliente: ").append(v.getCliente().getNombre()).append("\n");
                }
                mensaje.append("\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    private void mostrarDisponibilidadEspacios() {
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        StringBuilder mensaje = new StringBuilder("Disponibilidad de espacios:\n\n");
        mensaje.append("Automóviles: ").append(espacios.get("Automóvil")).append(" disponibles\n");
        mensaje.append("Motos: ").append(espacios.get("Moto")).append(" disponibles\n");
        mensaje.append("Camiones: ").append(espacios.get("Camión")).append(" disponibles\n");
        
        mostrarMensaje(mensaje.toString());
    }
    
    // ==================== MÉTODOS PARA FACTURACIÓN ====================
    private void generarFacturaMembresia() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        generarFacturaMembresia(placa);
    }

    private void generarFacturaMembresia(String placa) {
        String factura = parqueaderoService.generarFacturaMembresia(placa);
        mostrarFactura(factura);
    }

    private void generarFacturaEstacionamiento() {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        String factura = parqueaderoService.generarFacturaEstacionamiento(placa);
        mostrarFactura(factura);
    }

    private void mostrarFactura(String factura) {
        if (factura != null && !factura.isEmpty()) {
            JTextArea textArea = new JTextArea(factura);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(
                null, 
                scrollPane, 
                "Factura del Parqueadero", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null, 
                "No se encontró información para generar la factura.\n" +
                "Verifique que la placa sea correcta y que exista el registro correspondiente.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // ==================== MÉTODOS PARA REPORTES ====================
    private void generarReportes() {
        String opcion;
        do {
            opcion = JOptionPane.showInputDialog(
                "Reportes y Consultas\n\n" +
                "1. Historial de vehículos por cliente\n" +
                "2. Total de ingresos\n" +
                "3. Clientes con membresías activas\n" +
                "4. Clientes con membresías por vencer\n" +
                "5. Volver al menú principal\n\n" +
                "Seleccione una opción:"
            );
            
            switch (opcion) {
                case "1":
                    generarHistorialVehiculosCliente();
                    break;
                case "2":
                    generarReporteIngresos();
                    break;
                case "3":
                    generarClientesMembresiasActivas();
                    break;
                case "4":
                    generarClientesMembresiasPorVencer();
                    break;
                case "5":
                    break;
                default:
                    mostrarMensaje("Opción no válida");
            }
        } while (!opcion.equals("5"));
    }
    
    private void generarHistorialVehiculosCliente() {
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del cliente:");
        List<Vehiculo> vehiculos = parqueaderoService.buscarVehiculosPorCliente(cedula);
        
        if (vehiculos.isEmpty()) {
            mostrarMensaje("El cliente no tiene vehículos registrados");
        } else {
            StringBuilder mensaje = new StringBuilder("Vehículos del cliente:\n\n");
            for (Vehiculo v : vehiculos) {
                mensaje.append("Placa: ").append(v.getPlaca()).append("\n");
                mensaje.append("Tipo: ").append(v.getTipo()).append("\n");
                mensaje.append("Color: ").append(v.getColor()).append("\n");
                mensaje.append("Modelo: ").append(v.getModelo()).append("\n");
                
                if (v.getMembresia() != null) {
                    mensaje.append("Membresía: ").append(v.getMembresia().getTipo()).append("\n");
                    mensaje.append("Estado: ").append(v.getMembresia().estaActiva() ? "Activa" : "Vencida").append("\n");
                    mensaje.append("Vence: ").append(v.getMembresia().getFechaFin()).append("\n");
                }
                mensaje.append("\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    private void generarReporteIngresos() {
        String periodo = JOptionPane.showInputDialog(
            "Seleccione el período para el reporte:\n" +
            "1. Día actual\n" +
            "2. Mes actual\n" +
            "3. Año actual\n" +
            "4. Personalizado\n" +
            "Seleccione una opción:"
        );
        
        double total = 0;
        LocalDate hoy = LocalDate.now();
        
        switch (periodo) {
            case "1":
                total = parqueaderoService.getTotalIngresosPorDia(hoy);
                mostrarMensaje("Total ingresos hoy: $" + String.format("%,.2f", total));
                break;
            case "2":
                total = parqueaderoService.getTotalIngresosPorMes(hoy.getYear(), hoy.getMonthValue());
                mostrarMensaje("Total ingresos este mes: $" + String.format("%,.2f", total));
                break;
            case "3":
                total = parqueaderoService.getTotalIngresosPorAño(hoy.getYear());
                mostrarMensaje("Total ingresos este año: $" + String.format("%,.2f", total));
                break;
            case "4":
                try {
                    String inicioStr = JOptionPane.showInputDialog("Fecha inicio (dd/mm/aaaa):");
                    String finStr = JOptionPane.showInputDialog("Fecha fin (dd/mm/aaaa):");
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate inicio = LocalDate.parse(inicioStr, formatter);
                    LocalDate fin = LocalDate.parse(finStr, formatter);
                    
                    total = 0;
                    for (LocalDate date = inicio; !date.isAfter(fin); date = date.plusDays(1)) {
                        total += parqueaderoService.getTotalIngresosPorDia(date);
                    }
                    
                    mostrarMensaje("Total ingresos entre " + inicioStr + " y " + finStr + ": $" + String.format("%,.2f", total));
                } catch (Exception e) {
                    mostrarMensaje("Formato de fecha inválido. Use dd/mm/aaaa");
                }
                break;
            default:
                mostrarMensaje("Opción no válida");
        }
    }
    
    private void generarClientesMembresiasActivas() {
        List<Cliente> clientes = parqueaderoService.getClientesConMembresiasActivas();
        
        if (clientes.isEmpty()) {
            mostrarMensaje("No hay clientes con membresías activas");
        } else {
            StringBuilder mensaje = new StringBuilder("Clientes con membresías activas:\n\n");
            for (Cliente c : clientes) {
                mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
            }
            mostrarMensaje(mensaje.toString());
        }
    }
    
    private void generarClientesMembresiasPorVencer() {
        String diasStr = JOptionPane.showInputDialog("Ingrese el número de días para considerar como próximo a vencer:");
        try {
            int dias = Integer.parseInt(diasStr);
            List<Cliente> clientes = parqueaderoService.getClientesConMembresiasPorVencer(dias);
            
            if (clientes.isEmpty()) {
                mostrarMensaje("No hay clientes con membresías por vencer en los próximos " + dias + " días");
            } else {
                StringBuilder mensaje = new StringBuilder("Clientes con membresías por vencer (próximos " + dias + " días):\n\n");
                for (Cliente c : clientes) {
                    mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                    mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                    mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                    mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
                }
                mostrarMensaje(mensaje.toString());
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese un número válido");
        }
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    private String mostrarMenuPrincipal() {
        return JOptionPane.showInputDialog(
            "Sistema de Administración de Parqueadero\n\n" +
            "1. Gestión de Clientes\n" +
            "2. Gestión de Vehículos\n" +
            "3. Administración del Parqueadero\n" +
            "4. Operaciones de Parqueadero\n" +
            "5. Reportes y Consultas\n" +
            "6. Salir\n\n" +
            "Seleccione una opción:"
        );
    }
    
    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    public static void main(String[] args) {
        ParqueaderoController controller = new ParqueaderoController();
        controller.iniciar();
    }
}
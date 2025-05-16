package Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.*;
import Service.ParqueaderoService;

public class Interface {
    private ParqueaderoService parqueaderoService;
    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public Interface() {
        // Inicialización del servicio (igual que antes)
        Parqueadero parqueadero = new Parqueadero(
            "Parqueadero Central", 
            "Calle 15 #10-20, Armenia", 
            "Juan Pérez", 
            "3001234567", 
            "contacto@parqueaderocentral.com"
        );
        
        parqueadero.setEspaciosDisponibles("Automóvil", 50);
        parqueadero.setEspaciosDisponibles("Moto", 30);
        parqueadero.setEspaciosDisponibles("Camión", 10);
        
        parqueadero.setTarifa("Automóvil", 2000);
        parqueadero.setTarifa("Moto", 1000);
        parqueadero.setTarifa("Camión", 3000);
        
        this.parqueaderoService = new ParqueaderoService(parqueadero);
        
        // Configurar interfaz gráfica
        initGUI();
    }

    private void initGUI() {
        mainFrame = new JFrame("Sistema de Administración de Parqueadero");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Crear todos los paneles
        crearMenuPrincipal();
        crearPanelClientes();
        crearPanelVehiculos();
        crearPanelAdministracion();
        crearPanelOperaciones();
        crearPanelReportes();
        crearPanelRegistroCliente();
        crearPanelBuscarCliente();
        crearPanelRegistroVehiculo();
        crearPanelBuscarVehiculo();
        crearPanelMembresias();
        
        mainFrame.add(cardPanel);
        mainFrame.setVisible(true);
    }

    // ==================== PANELES PRINCIPALES ====================

    private void crearMenuPrincipal() {
        // Panel con imagen de fondo
        JPanelWithBackground panel = new JPanelWithBackground(
            "C:\\Users\\darda\\OneDrive\\Escritorio\\JAVA-Cristian\\TrabajosJava2\\Proyecto_Final\\src\\recurso\\Fondo_Parqueadero.jpeg"
        );
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Configurar transparencia del fondo (0.7f = 70% opaco)
        panel.setTransparency(0.7f);

        // Panel de contenido semi-transparente
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Fondo semi-transparente oscuro para mejor contraste
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 120)); // Negro con ~50% transparencia
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setLayout(new BorderLayout());
        glassPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Título con efecto de sombra
        JLabel titulo = new JLabel("Sistema de Administración de Parqueadero") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                
                // Sombra del texto
                g2.setColor(new Color(0, 0, 0, 150));
                g2.setFont(getFont().deriveFont(Font.BOLD, 22f));
                g2.drawString(getText(), 5, 27);
                
                // Texto principal
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), 4, 26);
            }
        };
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        
        // Panel para los botones con transparencia
        JPanel botonesPanel = new JPanel();
        botonesPanel.setOpaque(false);
        botonesPanel.setLayout(new GridLayout(5, 1, 15, 15));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        String[] opciones = {
            "Gestión de Clientes",
            "Gestión de Vehículos",
            "Administración del Parqueadero",
            "Operaciones de Parqueadero",
            "Reportes y Consultas"
        };
        
        // Crear botones con estilo moderno y transparente
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion) {
                @Override
                protected void paintComponent(Graphics g) {
                    if (!isOpaque()) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(new Color(255, 255, 255, 30)); // Fondo blanco muy transparente
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        
                        // Borde sutil
                        g2.setColor(new Color(255, 255, 255, 100));
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                        g2.dispose();
                    }
                    super.paintComponent(g);
                }
            };
            
            boton.setOpaque(false);
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setForeground(Color.WHITE);
            boton.setFont(new Font("Arial", Font.BOLD, 14));
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Efecto hover
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    boton.setForeground(new Color(255, 255, 180)); // Amarillo claro
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    boton.setForeground(Color.WHITE);
                }
            });
            
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Gestión de Clientes")) {
                    cardLayout.show(cardPanel, "clientes");
                } else if (textoBoton.startsWith("Gestión de Vehículos")) {
                    cardLayout.show(cardPanel, "vehiculos");
                } else if (textoBoton.startsWith("Administración")) {
                    cardLayout.show(cardPanel, "administracion");
                } else if (textoBoton.startsWith("Operaciones")) {
                    cardLayout.show(cardPanel, "operaciones");
                } else if (textoBoton.startsWith("Reportes")) {
                    cardLayout.show(cardPanel, "reportes");
                }
            });
            botonesPanel.add(boton);
        }
        
        // Botón de salida con estilo
        JButton salirButton = new JButton("Salir") {
         
        	@Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(200, 50, 50, 150)); // Rojo semi-transparente
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        salirButton.setOpaque(false);
        salirButton.setContentAreaFilled(false);
        salirButton.setBorderPainted(false);
        salirButton.setForeground(Color.WHITE);
        salirButton.setFont(new Font("Arial", Font.BOLD, 12));
        salirButton.addActionListener(e -> {
            mostrarMensaje("¡Gracias por usar el sistema de parqueadero!");
            System.exit(0);
        });
        
        // Organizar componentes
        glassPanel.add(titulo, BorderLayout.NORTH);
        glassPanel.add(botonesPanel, BorderLayout.CENTER);
        glassPanel.add(salirButton, BorderLayout.SOUTH);
        
        panel.add(glassPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "principal");
    }

    private void crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gestión de Clientes", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel botonesPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        String[] opciones = {
            "Registrar nuevo cliente",
            "Buscar cliente",
            "Actualizar cliente",
            "Eliminar cliente",
            "Ver vehículos de cliente",
            "Volver al menú principal"
        };
        
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Registrar")) {
                    cardLayout.show(cardPanel, "registroCliente");
                } else if (textoBoton.startsWith("Buscar")) {
                    cardLayout.show(cardPanel, "buscarCliente");
                } else if (textoBoton.startsWith("Actualizar")) {
                    actualizarCliente();
                } else if (textoBoton.startsWith("Eliminar")) {
                    eliminarCliente();
                } else if (textoBoton.startsWith("Ver vehículos")) {
                    verVehiculosCliente();
                } else if (textoBoton.startsWith("Volver")) {
                    cardLayout.show(cardPanel, "principal");
                }
            });
            botonesPanel.add(boton);
        }
        
        panel.add(botonesPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "clientes");
    }

    private void actualizarCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Actualizar Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel cedulaLabel = new JLabel("Cédula del cliente a actualizar:");
        JTextField cedulaField = new JTextField();
        
        JLabel telefonoLabel = new JLabel("Nuevo teléfono:");
        JTextField telefonoField = new JTextField();
        
        JLabel correoLabel = new JLabel("Nuevo correo:");
        JTextField correoField = new JTextField();
        
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(telefonoLabel);
        formPanel.add(telefonoField);
        formPanel.add(correoLabel);
        formPanel.add(correoField);
        
        JButton buscarButton = new JButton("Buscar Cliente");
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        
        buscarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            if (cedula.isEmpty()) {
                infoArea.setText("Ingrese una cédula para buscar");
                return;
            }
            
            Cliente cliente = parqueaderoService.buscarClientePorCedula(cedula);
            if (cliente == null) {
                infoArea.setText("Cliente no encontrado");
            } else {
                infoArea.setText("Cliente encontrado:\n\n" +
                               "Nombre: " + cliente.getNombre() + "\n" +
                               "Teléfono actual: " + cliente.getTelefono() + "\n" +
                               "Correo actual: " + cliente.getCorreo());
                telefonoField.setText(cliente.getTelefono());
                correoField.setText(cliente.getCorreo());
            }
        });
        
        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            String nuevoTelefono = telefonoField.getText();
            String nuevoCorreo = correoField.getText();
            
            if (cedula.isEmpty()) {
                infoArea.setText("Ingrese una cédula válida");
                return;
            }
            
            if (parqueaderoService.actualizarCliente(cedula, nuevoTelefono, nuevoCorreo)) {
                infoArea.setText("Cliente actualizado exitosamente");
            } else {
                infoArea.setText("Error al actualizar cliente");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "clientes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(buscarButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "actualizarCliente");
        cardLayout.show(cardPanel, "actualizarCliente");
    }

    private void eliminarCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Eliminar Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel cedulaLabel = new JLabel("Cédula del cliente a eliminar:");
        JTextField cedulaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar Cliente");
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        
        buscarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            if (cedula.isEmpty()) {
                infoArea.setText("Ingrese una cédula para buscar");
                return;
            }
            
            Cliente cliente = parqueaderoService.buscarClientePorCedula(cedula);
            if (cliente == null) {
                infoArea.setText("Cliente no encontrado");
            } else {
                infoArea.setText("Cliente encontrado:\n\n" +
                               "Nombre: " + cliente.getNombre() + "\n" +
                               "Teléfono: " + cliente.getTelefono() + "\n" +
                               "Correo: " + cliente.getCorreo() + "\n\n" +
                               "¿Está seguro que desea eliminar este cliente?");
            }
        });
        
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(new JLabel());
        formPanel.add(buscarButton);
        
        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            
            if (cedula.isEmpty()) {
                infoArea.setText("Ingrese una cédula válida");
                return;
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(
                mainFrame,
                "¿Está seguro que desea eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (parqueaderoService.eliminarCliente(cedula)) {
                    infoArea.setText("Cliente eliminado exitosamente");
                    cedulaField.setText("");
                } else {
                    infoArea.setText("Error al eliminar cliente");
                }
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "clientes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(eliminarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "eliminarCliente");
        cardLayout.show(cardPanel, "eliminarCliente");
    }

    private void verVehiculosCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Vehículos del Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel cedulaLabel = new JLabel("Cédula del cliente:");
        JTextField cedulaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar Vehículos");
        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        
        buscarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            if (cedula.isEmpty()) {
                resultadosArea.setText("Ingrese una cédula para buscar");
                return;
            }
            
            List<Vehiculo> vehiculos = parqueaderoService.buscarVehiculosPorCliente(cedula);
            
            if (vehiculos.isEmpty()) {
                resultadosArea.setText("El cliente no tiene vehículos registrados");
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
                resultadosArea.setText(mensaje.toString());
            }
        });
        
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(new JLabel());
        formPanel.add(buscarButton);
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "clientes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "verVehiculosCliente");
        cardLayout.show(cardPanel, "verVehiculosCliente");
    }
	private void crearPanelVehiculos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gestión de Vehículos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel botonesPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        String[] opciones = {
            "Registrar vehículo",
            "Buscar vehículo",
            "Actualizar vehículo",
            "Eliminar vehículo",
            "Gestionar membresías",
            "Generar factura de membresía",
            "Volver al menú principal"
        };
        
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Registrar")) {
                    cardLayout.show(cardPanel, "registroVehiculo");
                } else if (textoBoton.startsWith("Buscar")) {
                    cardLayout.show(cardPanel, "buscarVehiculo");
                } else if (textoBoton.startsWith("Actualizar")) {
                    actualizarVehiculo();
                } else if (textoBoton.startsWith("Eliminar")) {
                    eliminarVehiculo();
                } else if (textoBoton.startsWith("Gestionar")) {
                    cardLayout.show(cardPanel, "membresias");
                } else if (textoBoton.startsWith("Generar factura")) {
                    generarFacturaMembresia();
                } else if (textoBoton.startsWith("Volver")) {
                    cardLayout.show(cardPanel, "principal");
                }
            });
            botonesPanel.add(boton);
        }
        
        panel.add(botonesPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "vehiculos");
    }
	private void actualizarVehiculo() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    JLabel titulo = new JLabel("Actualizar Vehículo", JLabel.CENTER);
	    titulo.setFont(new Font("Arial", Font.BOLD, 18));
	    panel.add(titulo, BorderLayout.NORTH);
	    
	    JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    
	    JLabel placaLabel = new JLabel("Placa del vehículo:");
	    JTextField placaField = new JTextField();
	    
	    JLabel colorLabel = new JLabel("Nuevo color:");
	    JTextField colorField = new JTextField();
	    
	    JLabel modeloLabel = new JLabel("Nuevo modelo:");
	    JTextField modeloField = new JTextField();
	    
	    formPanel.add(placaLabel);
	    formPanel.add(placaField);
	    formPanel.add(colorLabel);
	    formPanel.add(colorField);
	    formPanel.add(modeloLabel);
	    formPanel.add(modeloField);
	    
	    JButton buscarButton = new JButton("Buscar Vehículo");
	    JTextArea infoArea = new JTextArea();
	    infoArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(infoArea);
	    
	    buscarButton.addActionListener(e -> {
	        String placa = placaField.getText();
	        if (placa.isEmpty()) {
	            infoArea.setText("Ingrese una placa para buscar");
	            return;
	        }
	        
	        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
	        if (vehiculo == null) {
	            infoArea.setText("Vehículo no encontrado");
	        } else {
	            infoArea.setText("Vehículo encontrado:\n\n" +
	                           "Placa: " + vehiculo.getPlaca() + "\n" +
	                           "Tipo: " + vehiculo.getTipo() + "\n" +
	                           "Color actual: " + vehiculo.getColor() + "\n" +
	                           "Modelo actual: " + vehiculo.getModelo());
	            colorField.setText(vehiculo.getColor());
	            modeloField.setText(vehiculo.getModelo());
	        }
	    });
	    
	    JButton actualizarButton = new JButton("Actualizar");
	    actualizarButton.addActionListener(e -> {
	        String placa = placaField.getText();
	        String nuevoColor = colorField.getText();
	        String nuevoModelo = modeloField.getText();
	        
	        if (placa.isEmpty()) {
	            infoArea.setText("Ingrese una placa válida");
	            return;
	        }
	        
	        if (parqueaderoService.actualizarVehiculo(placa, nuevoColor, nuevoModelo)) {
	            infoArea.setText("Vehículo actualizado exitosamente");
	        } else {
	            infoArea.setText("Error al actualizar vehículo");
	        }
	    });
	    
	    JButton volverButton = new JButton("Volver");
	    volverButton.addActionListener(e -> cardLayout.show(cardPanel, "vehiculos"));
	    
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.add(buscarButton);
	    buttonPanel.add(actualizarButton);
	    buttonPanel.add(volverButton);
	    
	    panel.add(formPanel, BorderLayout.NORTH);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    panel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    // Crear un panel temporal para mostrar este formulario
	    JPanel tempPanel = new JPanel(new BorderLayout());
	    tempPanel.add(panel, BorderLayout.CENTER);
	    
	    cardPanel.add(tempPanel, "actualizarVehiculo");
	    cardLayout.show(cardPanel, "actualizarVehiculo");
	}

	private void eliminarVehiculo() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    JLabel titulo = new JLabel("Eliminar Vehículo", JLabel.CENTER);
	    titulo.setFont(new Font("Arial", Font.BOLD, 18));
	    panel.add(titulo, BorderLayout.NORTH);
	    
	    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    
	    JLabel placaLabel = new JLabel("Placa del vehículo:");
	    JTextField placaField = new JTextField();
	    
	    JButton buscarButton = new JButton("Buscar Vehículo");
	    JTextArea infoArea = new JTextArea();
	    infoArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(infoArea);
	    
	    buscarButton.addActionListener(e -> {
	        String placa = placaField.getText();
	        if (placa.isEmpty()) {
	            infoArea.setText("Ingrese una placa para buscar");
	            return;
	        }
	        
	        Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
	        if (vehiculo == null) {
	            infoArea.setText("Vehículo no encontrado");
	        } else {
	            infoArea.setText("Vehículo encontrado:\n\n" +
	                           "Placa: " + vehiculo.getPlaca() + "\n" +
	                           "Tipo: " + vehiculo.getTipo() + "\n" +
	                           "Color: " + vehiculo.getColor() + "\n" +
	                           "Modelo: " + vehiculo.getModelo() + "\n\n" +
	                           "¿Está seguro que desea eliminar este vehículo?");
	        }
	    });
	    
	    formPanel.add(placaLabel);
	    formPanel.add(placaField);
	    formPanel.add(new JLabel());
	    formPanel.add(buscarButton);
	    
	    JButton eliminarButton = new JButton("Eliminar");
	    eliminarButton.addActionListener(e -> {
	        String placa = placaField.getText();
	        
	        if (placa.isEmpty()) {
	            infoArea.setText("Ingrese una placa válida");
	            return;
	        }
	        
	        int confirmacion = JOptionPane.showConfirmDialog(
	            mainFrame,
	            "¿Está seguro que desea eliminar este vehículo?",
	            "Confirmar eliminación",
	            JOptionPane.YES_NO_OPTION
	        );
	        
	        if (confirmacion == JOptionPane.YES_OPTION) {
	            if (parqueaderoService.eliminarVehiculo(placa)) {
	                infoArea.setText("Vehículo eliminado exitosamente");
	                placaField.setText("");
	            } else {
	                infoArea.setText("Error al eliminar vehículo");
	            }
	        }
	    });
	    
	    JButton volverButton = new JButton("Volver");
	    volverButton.addActionListener(e -> cardLayout.show(cardPanel, "vehiculos"));
	    
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.add(eliminarButton);
	    buttonPanel.add(volverButton);
	    
	    panel.add(formPanel, BorderLayout.NORTH);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    panel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    // Crear un panel temporal para mostrar este formulario
	    JPanel tempPanel = new JPanel(new BorderLayout());
	    tempPanel.add(panel, BorderLayout.CENTER);
	    
	    cardPanel.add(tempPanel, "eliminarVehiculo");
	    cardLayout.show(cardPanel, "eliminarVehiculo");
	}

	private void generarFacturaMembresia() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    JLabel titulo = new JLabel("Generar Factura de Membresía", JLabel.CENTER);
	    titulo.setFont(new Font("Arial", Font.BOLD, 18));
	    panel.add(titulo, BorderLayout.NORTH);
	    
	    JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
	    
	    JLabel placaLabel = new JLabel("Placa del vehículo:");
	    JTextField placaField = new JTextField();
	    
	    JButton generarButton = new JButton("Generar Factura");
	    JTextArea facturaArea = new JTextArea();
	    facturaArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(facturaArea);
	    
	    generarButton.addActionListener(e -> {
	        String placa = placaField.getText();
	        if (placa.isEmpty()) {
	            facturaArea.setText("Ingrese una placa para generar la factura");
	            return;
	        }
	        
	        String factura = parqueaderoService.generarFacturaMembresia(placa);
	        if (factura != null && !factura.isEmpty()) {
	            facturaArea.setText(factura);
	        } else {
	            facturaArea.setText("No se encontró información para generar la factura.\n" +
	                               "Verifique que la placa sea correcta y que exista el registro correspondiente.");
	        }
	    });
	    
	    formPanel.add(placaLabel);
	    formPanel.add(placaField);
	    formPanel.add(new JLabel());
	    formPanel.add(generarButton);
	    
	    JButton volverButton = new JButton("Volver");
	    volverButton.addActionListener(e -> {
	        placaField.setText("");
	        facturaArea.setText("");
	        cardLayout.show(cardPanel, "vehiculos");
	    });
	    
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.add(volverButton);
	    
	    panel.add(formPanel, BorderLayout.NORTH);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    panel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    // Crear un panel temporal para mostrar este formulario
	    JPanel tempPanel = new JPanel(new BorderLayout());
	    tempPanel.add(panel, BorderLayout.CENTER);
	    
	    cardPanel.add(tempPanel, "generarFacturaMembresia");
	    cardLayout.show(cardPanel, "generarFacturaMembresia");
	}

    private void crearPanelAdministracion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Administración del Parqueadero", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        String[] opciones = {
            "Ver información del parqueadero",
            "Actualizar información",
            "Configurar tarifas",
            "Configurar espacios",
            "Volver al menú principal"
        };
        
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Ver información")) {
                    mostrarInfoParqueadero();
                } else if (textoBoton.startsWith("Actualizar información")) {
                    actualizarInfoParqueadero();
                } else if (textoBoton.startsWith("Configurar tarifas")) {
                    configurarTarifas();
                } else if (textoBoton.startsWith("Configurar espacios")) {
                    configurarEspacios();
                } else if (textoBoton.startsWith("Volver")) {
                    cardLayout.show(cardPanel, "principal");
                }
            });
            botonesPanel.add(boton);
        }
        
        panel.add(botonesPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "administracion");
    }
    private void mostrarInfoParqueadero() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Información del Parqueadero", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        StringBuilder mensaje = new StringBuilder();
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
        
        infoArea.setText(mensaje.toString());
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "administracion"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(volverButton, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar esta información
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "infoParqueadero");
        cardLayout.show(cardPanel, "infoParqueadero");
    }

    private void actualizarInfoParqueadero() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Actualizar Información del Parqueadero", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField(parqueadero.getNombre());
        
        JLabel direccionLabel = new JLabel("Dirección:");
        JTextField direccionField = new JTextField(parqueadero.getDireccion());
        
        JLabel representanteLabel = new JLabel("Representante:");
        JTextField representanteField = new JTextField(parqueadero.getRepresentante());
        
        JLabel telefonoLabel = new JLabel("Teléfono:");
        JTextField telefonoField = new JTextField(parqueadero.getTelefono());
        
        JLabel correoLabel = new JLabel("Correo:");
        JTextField correoField = new JTextField(parqueadero.getCorreo());
        
        formPanel.add(nombreLabel);
        formPanel.add(nombreField);
        formPanel.add(direccionLabel);
        formPanel.add(direccionField);
        formPanel.add(representanteLabel);
        formPanel.add(representanteField);
        formPanel.add(telefonoLabel);
        formPanel.add(telefonoField);
        formPanel.add(correoLabel);
        formPanel.add(correoField);
        
        JButton actualizarButton = new JButton("Actualizar");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        
        actualizarButton.addActionListener(e -> {
            String nombre = nombreField.getText();
            String direccion = direccionField.getText();
            String representante = representanteField.getText();
            String telefono = telefonoField.getText();
            String correo = correoField.getText();
            
            if (nombre.isEmpty() || direccion.isEmpty() || representante.isEmpty()) {
                resultadoArea.setText("Nombre, dirección y representante son campos obligatorios");
                return;
            }
            
            if (parqueaderoService.actualizarInfoParqueadero(nombre, direccion, representante, telefono, correo)) {
                resultadoArea.setText("Información actualizada exitosamente");
            } else {
                resultadoArea.setText("Error al actualizar información");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "administracion"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(actualizarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(resultadoArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "actualizarInfoParqueadero");
        cardLayout.show(cardPanel, "actualizarInfoParqueadero");
    }

    private void configurarTarifas() {
        Parqueadero parqueadero = parqueaderoService.getInfoParqueadero();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Configurar Tarifas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel autoLabel = new JLabel("Tarifa para automóviles:");
        JTextField autoField = new JTextField(String.valueOf(parqueadero.getTarifa("Automóvil")));
        
        JLabel motoLabel = new JLabel("Tarifa para motos:");
        JTextField motoField = new JTextField(String.valueOf(parqueadero.getTarifa("Moto")));
        
        JLabel camionLabel = new JLabel("Tarifa para camiones:");
        JTextField camionField = new JTextField(String.valueOf(parqueadero.getTarifa("Camión")));
        
        formPanel.add(autoLabel);
        formPanel.add(autoField);
        formPanel.add(motoLabel);
        formPanel.add(motoField);
        formPanel.add(camionLabel);
        formPanel.add(camionField);
        
        JButton guardarButton = new JButton("Guardar Tarifas");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        
        guardarButton.addActionListener(e -> {
            try {
                double tarifaAuto = Double.parseDouble(autoField.getText());
                double tarifaMoto = Double.parseDouble(motoField.getText());
                double tarifaCamion = Double.parseDouble(camionField.getText());
                
                if (tarifaAuto <= 0 || tarifaMoto <= 0 || tarifaCamion <= 0) {
                    resultadoArea.setText("Las tarifas deben ser valores positivos");
                    return;
                }
                
                if (parqueaderoService.actualizarTarifas(tarifaAuto, tarifaMoto, tarifaCamion)) {
                    resultadoArea.setText("Tarifas actualizadas exitosamente");
                } else {
                    resultadoArea.setText("Error al actualizar tarifas");
                }
            } catch (NumberFormatException ex) {
                resultadoArea.setText("Ingrese valores numéricos válidos");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "administracion"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(guardarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(resultadoArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "configurarTarifas");
        cardLayout.show(cardPanel, "configurarTarifas");
    }

    private void configurarEspacios() {
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Configurar Espacios", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel autoLabel = new JLabel("Espacios para automóviles:");
        JTextField autoField = new JTextField(String.valueOf(espacios.get("Automóvil")));
        
        JLabel motoLabel = new JLabel("Espacios para motos:");
        JTextField motoField = new JTextField(String.valueOf(espacios.get("Moto")));
        
        JLabel camionLabel = new JLabel("Espacios para camiones:");
        JTextField camionField = new JTextField(String.valueOf(espacios.get("Camión")));
        
        formPanel.add(autoLabel);
        formPanel.add(autoField);
        formPanel.add(motoLabel);
        formPanel.add(motoField);
        formPanel.add(camionLabel);
        formPanel.add(camionField);
        
        JButton guardarButton = new JButton("Guardar Espacios");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        
        guardarButton.addActionListener(e -> {
            try {
                int espaciosAuto = Integer.parseInt(autoField.getText());
                int espaciosMoto = Integer.parseInt(motoField.getText());
                int espaciosCamion = Integer.parseInt(camionField.getText());
                
                if (espaciosAuto <= 0 || espaciosMoto <= 0 || espaciosCamion <= 0) {
                    resultadoArea.setText("Los espacios deben ser valores positivos");
                    return;
                }
                
                if (parqueaderoService.actualizarEspacios(espaciosAuto, espaciosMoto, espaciosCamion)) {
                    resultadoArea.setText("Espacios actualizados exitosamente");
                } else {
                    resultadoArea.setText("Error al actualizar espacios");
                }
            } catch (NumberFormatException ex) {
                resultadoArea.setText("Ingrese valores numéricos válidos");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "administracion"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(guardarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(resultadoArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crear un panel temporal para mostrar este formulario
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(panel, BorderLayout.CENTER);
        
        cardPanel.add(tempPanel, "configurarEspacios");
        cardLayout.show(cardPanel, "configurarEspacios");
    }

    private void crearPanelOperaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Operaciones de Parqueadero", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel botonesPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        String[] opciones = {
            "Registrar ingreso de vehículo",
            "Registrar salida de vehículo",
            "Ver vehículos actuales",
            "Generar factura de estacionamiento",
            "Ver disponibilidad de espacios",
            "Volver al menú principal"
        };
        
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Registrar ingreso")) {
                    registrarIngresoVehiculo();
                } else if (textoBoton.startsWith("Registrar salida")) {
                    registrarSalidaVehiculo();
                } else if (textoBoton.startsWith("Ver vehículos actuales")) {
                    mostrarVehiculosActuales();
                } else if (textoBoton.startsWith("Generar factura")) {
                    generarFacturaEstacionamiento();
                } else if (textoBoton.startsWith("Ver disponibilidad")) {
                    mostrarDisponibilidadEspacios();
                } else if (textoBoton.startsWith("Volver")) {
                    cardLayout.show(cardPanel, "principal");
                }
            });
            botonesPanel.add(boton);
        }
        
        panel.add(botonesPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "operaciones");
    }
    
    private void registrarIngresoVehiculo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Registrar Ingreso de Vehículo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel placaLabel = new JLabel("Placa del vehículo:");
        JTextField placaField = new JTextField();
        
        JLabel tipoLabel = new JLabel("Tipo de vehículo:");
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Automóvil", "Moto", "Camión"});
        
        formPanel.add(placaLabel);
        formPanel.add(placaField);
        formPanel.add(tipoLabel);
        formPanel.add(tipoCombo);
        
        JButton registrarButton = new JButton("Registrar Ingreso");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        
        registrarButton.addActionListener(e -> {
            String placa = placaField.getText();
            String tipo = (String) tipoCombo.getSelectedItem();
            
            if (placa.isEmpty()) {
                resultadoArea.setText("Ingrese la placa del vehículo");
                return;
            }
            
            Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
            
            if (vehiculo == null) {
                int respuesta = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Vehículo no registrado. ¿Desea registrarlo como temporal?",
                    "Vehículo no encontrado",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (respuesta == JOptionPane.YES_OPTION) {
                    switch(tipo) {
                        case "Automóvil":
                            vehiculo = new Automovil(placa, "Temporal", "Temporal", null);
                            break;
                        case "Moto":
                            vehiculo = new Moto(placa, "Temporal", "Temporal", null);
                            break;
                        case "Camión":
                            vehiculo = new Camion(placa, "Temporal", "Temporal", null);
                            break;
                    }
                    
                    parqueaderoService.registrarVehiculo(vehiculo);
                } else {
                    return;
                }
            }
            
            if (parqueaderoService.registrarIngreso(vehiculo)) {
                resultadoArea.setText("Ingreso registrado exitosamente\n\n" +
                                   "Placa: " + vehiculo.getPlaca() + "\n" +
                                   "Tipo: " + vehiculo.getTipo() + "\n" +
                                   "Hora ingreso: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                placaField.setText("");
            } else {
                resultadoArea.setText("No hay espacios disponibles para este tipo de vehículo");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "operaciones"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registrarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "registrarIngreso");
        cardLayout.show(cardPanel, "registrarIngreso");
    }

    private void registrarSalidaVehiculo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Registrar Salida de Vehículo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel placaLabel = new JLabel("Placa del vehículo:");
        JTextField placaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar Vehículo");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        
        buscarButton.addActionListener(e -> {
            String placa = placaField.getText();
            if (placa.isEmpty()) {
                resultadoArea.setText("Ingrese la placa del vehículo");
                return;
            }
            
            Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
            if (vehiculo == null || !vehiculo.isEnParqueadero()) {
                resultadoArea.setText("Vehículo no encontrado o no está en el parqueadero");
            } else {
                resultadoArea.setText("Vehículo encontrado:\n\n" +
                                   "Placa: " + vehiculo.getPlaca() + "\n" +
                                   "Tipo: " + vehiculo.getTipo() + "\n" +
                                   "Hora ingreso: " + vehiculo.getHoraEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
        });
        
        formPanel.add(placaLabel);
        formPanel.add(placaField);
        formPanel.add(new JLabel());
        formPanel.add(buscarButton);
        
        JButton registrarButton = new JButton("Registrar Salida");
        registrarButton.addActionListener(e -> {
            String placa = placaField.getText();
            Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
            
            if (vehiculo == null || !vehiculo.isEnParqueadero()) {
                resultadoArea.setText("Vehículo no encontrado o no está en el parqueadero");
                return;
            }
            
            Pago pago = parqueaderoService.registrarSalida(vehiculo);
            if (pago != null) {
                String factura = parqueaderoService.generarFacturaEstacionamiento(placa);
                resultadoArea.setText(factura);
            } else {
                resultadoArea.setText("Error al registrar salida");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "operaciones"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registrarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "registrarSalida");
        cardLayout.show(cardPanel, "registrarSalida");
    }

    private void mostrarVehiculosActuales() {
        List<Vehiculo> vehiculos = parqueaderoService.getVehiculosEnParqueadero();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Vehículos Actuales en el Parqueadero", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JTextArea vehiculosArea = new JTextArea();
        vehiculosArea.setEditable(false);
        vehiculosArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        if (vehiculos.isEmpty()) {
            vehiculosArea.setText("No hay vehículos en el parqueadero actualmente");
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
            vehiculosArea.setText(mensaje.toString());
        }
        
        JScrollPane scrollPane = new JScrollPane(vehiculosArea);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "operaciones"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(volverButton, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "vehiculosActuales");
        cardLayout.show(cardPanel, "vehiculosActuales");
    }

    private void generarFacturaEstacionamiento() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Generar Factura de Estacionamiento", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel placaLabel = new JLabel("Placa del vehículo:");
        JTextField placaField = new JTextField();
        
        JButton generarButton = new JButton("Generar Factura");
        JTextArea facturaArea = new JTextArea();
        facturaArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(facturaArea);
        
        generarButton.addActionListener(e -> {
            String placa = placaField.getText();
            if (placa.isEmpty()) {
                facturaArea.setText("Ingrese la placa del vehículo");
                return;
            }
            
            String factura = parqueaderoService.generarFacturaEstacionamiento(placa);
            if (factura != null && !factura.isEmpty()) {
                facturaArea.setText(factura);
            } else {
                facturaArea.setText("No se encontró información para generar la factura.\n" +
                                  "Verifique que la placa sea correcta y que exista el registro correspondiente.");
            }
        });
        
        formPanel.add(placaLabel);
        formPanel.add(placaField);
        formPanel.add(new JLabel());
        formPanel.add(generarButton);
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            placaField.setText("");
            facturaArea.setText("");
            cardLayout.show(cardPanel, "operaciones");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "generarFacturaEstacionamiento");
        cardLayout.show(cardPanel, "generarFacturaEstacionamiento");
    }

    private void mostrarDisponibilidadEspacios() {
        Map<String, Integer> espacios = parqueaderoService.getDisponibilidadEspacios();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Disponibilidad de Espacios", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JTextArea disponibilidadArea = new JTextArea();
        disponibilidadArea.setEditable(false);
        disponibilidadArea.setFont(new Font("Arial", Font.PLAIN, 16));
        
        StringBuilder mensaje = new StringBuilder("Espacios disponibles:\n\n");
        mensaje.append("Automóviles: ").append(espacios.get("Automóvil")).append(" disponibles\n");
        mensaje.append("Motos: ").append(espacios.get("Moto")).append(" disponibles\n");
        mensaje.append("Camiones: ").append(espacios.get("Camión")).append(" disponibles\n");
        
        disponibilidadArea.setText(mensaje.toString());
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "operaciones"));
        
        panel.add(disponibilidadArea, BorderLayout.CENTER);
        panel.add(volverButton, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "disponibilidadEspacios");
        cardLayout.show(cardPanel, "disponibilidadEspacios");
    }

    private void crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Reportes y Consultas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        String[] opciones = {
            "Historial de vehículos por cliente",
            "Total de ingresos",
            "Clientes con membresías activas",
            "Clientes con membresías por vencer",
            "Volver al menú principal"
        };
        
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(e -> {
                String textoBoton = ((JButton)e.getSource()).getText();
                if (textoBoton.startsWith("Historial")) {
                    generarHistorialVehiculosCliente();
                } else if (textoBoton.startsWith("Total de ingresos")) {
                    generarReporteIngresos();
                } else if (textoBoton.startsWith("Clientes con membresías activas")) {
                    generarClientesMembresiasActivas();
                } else if (textoBoton.startsWith("Clientes con membresías por vencer")) {
                    generarClientesMembresiasPorVencer();
                } else if (textoBoton.startsWith("Volver")) {
                    cardLayout.show(cardPanel, "principal");
                }
            });
            botonesPanel.add(boton);
        }
        
        panel.add(botonesPanel, BorderLayout.CENTER);
        cardPanel.add(panel, "reportes");
    }
    private void generarHistorialVehiculosCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Historial de Vehículos por Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel cedulaLabel = new JLabel("Cédula del cliente:");
        JTextField cedulaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar Vehículos");
        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        
        buscarButton.addActionListener(e -> {
            String cedula = cedulaField.getText();
            if (cedula.isEmpty()) {
                resultadosArea.setText("Ingrese una cédula para buscar");
                return;
            }
            
            List<Vehiculo> vehiculos = parqueaderoService.buscarVehiculosPorCliente(cedula);
            
            if (vehiculos.isEmpty()) {
                resultadosArea.setText("El cliente no tiene vehículos registrados");
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
                resultadosArea.setText(mensaje.toString());
            }
        });
        
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(new JLabel());
        formPanel.add(buscarButton);
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "reportes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "historialVehiculos");
        cardLayout.show(cardPanel, "historialVehiculos");
    }

    private void generarReporteIngresos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Reporte de Ingresos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel periodoPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        periodoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        ButtonGroup periodoGroup = new ButtonGroup();
        JRadioButton hoyRadio = new JRadioButton("Día actual");
        JRadioButton mesRadio = new JRadioButton("Mes actual");
        JRadioButton añoRadio = new JRadioButton("Año actual");
        JRadioButton customRadio = new JRadioButton("Personalizado");
        
        periodoGroup.add(hoyRadio);
        periodoGroup.add(mesRadio);
        periodoGroup.add(añoRadio);
        periodoGroup.add(customRadio);
        hoyRadio.setSelected(true);
        
        JPanel radioPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        radioPanel.add(hoyRadio);
        radioPanel.add(mesRadio);
        radioPanel.add(añoRadio);
        radioPanel.add(customRadio);
        
        JLabel inicioLabel = new JLabel("Fecha inicio (dd/mm/aaaa):");
        JTextField inicioField = new JTextField();
        inicioField.setEnabled(false);
        
        JLabel finLabel = new JLabel("Fecha fin (dd/mm/aaaa):");
        JTextField finField = new JTextField();
        finField.setEnabled(false);
        
        customRadio.addActionListener(e -> {
            inicioField.setEnabled(customRadio.isSelected());
            finField.setEnabled(customRadio.isSelected());
        });
        
        periodoPanel.add(radioPanel);
        periodoPanel.add(inicioLabel);
        periodoPanel.add(inicioField);
        periodoPanel.add(finLabel);
        periodoPanel.add(finField);
        
        JButton generarButton = new JButton("Generar Reporte");
        JTextArea reporteArea = new JTextArea();
        reporteArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reporteArea);
        
        generarButton.addActionListener(e -> {
            double total = 0;
            LocalDate hoy = LocalDate.now();
            
            try {
                if (hoyRadio.isSelected()) {
                    total = parqueaderoService.getTotalIngresosPorDia(hoy);
                    reporteArea.setText("Total ingresos hoy: $" + String.format("%,.2f", total));
                } else if (mesRadio.isSelected()) {
                    total = parqueaderoService.getTotalIngresosPorMes(hoy.getYear(), hoy.getMonthValue());
                    reporteArea.setText("Total ingresos este mes: $" + String.format("%,.2f", total));
                } else if (añoRadio.isSelected()) {
                    total = parqueaderoService.getTotalIngresosPorAño(hoy.getYear());
                    reporteArea.setText("Total ingresos este año: $" + String.format("%,.2f", total));
                } else if (customRadio.isSelected()) {
                    String inicioStr = inicioField.getText();
                    String finStr = finField.getText();
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate inicio = LocalDate.parse(inicioStr, formatter);
                    LocalDate fin = LocalDate.parse(finStr, formatter);
                    
                    total = 0;
                    for (LocalDate date = inicio; !date.isAfter(fin); date = date.plusDays(1)) {
                        total += parqueaderoService.getTotalIngresosPorDia(date);
                    }
                    
                    reporteArea.setText("Total ingresos entre " + inicioStr + " y " + finStr + ": $" + String.format("%,.2f", total));
                }
            } catch (Exception ex) {
                reporteArea.setText("Error: " + ex.getMessage());
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "reportes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(generarButton);
        buttonPanel.add(volverButton);
        
        panel.add(periodoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "reporteIngresos");
        cardLayout.show(cardPanel, "reporteIngresos");
    }

    private void generarClientesMembresiasActivas() {
        List<Cliente> clientes = parqueaderoService.getClientesConMembresiasActivas();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Clientes con Membresías Activas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JTextArea clientesArea = new JTextArea();
        clientesArea.setEditable(false);
        clientesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        if (clientes.isEmpty()) {
            clientesArea.setText("No hay clientes con membresías activas");
        } else {
            StringBuilder mensaje = new StringBuilder("Clientes con membresías activas:\n\n");
            for (Cliente c : clientes) {
                mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
            }
            clientesArea.setText(mensaje.toString());
        }
        
        JScrollPane scrollPane = new JScrollPane(clientesArea);
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "reportes"));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(volverButton, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "membresiasActivas");
        cardLayout.show(cardPanel, "membresiasActivas");
    }

    private void generarClientesMembresiasPorVencer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Clientes con Membresías por Vencer", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel diasLabel = new JLabel("Número de días para considerar:");
        JTextField diasField = new JTextField("30");
        
        JButton buscarButton = new JButton("Buscar Clientes");
        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        
        buscarButton.addActionListener(e -> {
            try {
                int dias = Integer.parseInt(diasField.getText());
                List<Cliente> clientes = parqueaderoService.getClientesConMembresiasPorVencer(dias);
                
                if (clientes.isEmpty()) {
                    resultadosArea.setText("No hay clientes con membresías por vencer en los próximos " + dias + " días");
                } else {
                    StringBuilder mensaje = new StringBuilder("Clientes con membresías por vencer (próximos " + dias + " días):\n\n");
                    for (Cliente c : clientes) {
                        mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                        mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                        mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                        mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
                    }
                    resultadosArea.setText(mensaje.toString());
                }
            } catch (NumberFormatException ex) {
                resultadosArea.setText("Ingrese un número válido de días");
            }
        });
        
        formPanel.add(diasLabel);
        formPanel.add(diasField);
        formPanel.add(new JLabel());
        formPanel.add(buscarButton);
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "reportes"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "membresiasPorVencer");
        cardLayout.show(cardPanel, "membresiasPorVencer");
    }

    // ==================== PANELES SECUNDARIOS ====================

    private void crearPanelRegistroCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Registro de Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreField = new JTextField();
        
        JLabel cedulaLabel = new JLabel("Cédula:");
        JTextField cedulaField = new JTextField();
        
        JLabel telefonoLabel = new JLabel("Teléfono:");
        JTextField telefonoField = new JTextField();
        
        JLabel correoLabel = new JLabel("Correo:");
        JTextField correoField = new JTextField();
        
        formPanel.add(nombreLabel);
        formPanel.add(nombreField);
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(telefonoLabel);
        formPanel.add(telefonoField);
        formPanel.add(correoLabel);
        formPanel.add(correoField);
        
        JButton registrarButton = new JButton("Registrar");
        registrarButton.addActionListener(e -> {
            try {
                String nombre = nombreField.getText();
                String cedula = cedulaField.getText();
                String telefono = telefonoField.getText();
                String correo = correoField.getText();
                
                if (nombre.isEmpty() || cedula.isEmpty()) {
                    mostrarMensaje("Nombre y cédula son campos obligatorios");
                    return;
                }
                
                Cliente cliente = new Cliente(nombre, cedula, telefono, correo);
                parqueaderoService.agregarCliente(cliente);
                mostrarMensaje("Cliente registrado exitosamente");
                limpiarCampos(nombreField, cedulaField, telefonoField, correoField);
                cardLayout.show(cardPanel, "clientes");
            } catch (Exception ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            }
        });
        
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> {
            limpiarCampos(nombreField, cedulaField, telefonoField, correoField);
            cardLayout.show(cardPanel, "clientes");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registrarButton);
        buttonPanel.add(cancelarButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "registroCliente");
    }

    private void crearPanelBuscarCliente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Buscar Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        ButtonGroup criterioGroup = new ButtonGroup();
        JRadioButton cedulaRadio = new JRadioButton("Por cédula", true);
        JRadioButton nombreRadio = new JRadioButton("Por nombre");
        JRadioButton telefonoRadio = new JRadioButton("Por teléfono");
        
        criterioGroup.add(cedulaRadio);
        criterioGroup.add(nombreRadio);
        criterioGroup.add(telefonoRadio);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(cedulaRadio);
        radioPanel.add(nombreRadio);
        radioPanel.add(telefonoRadio);
        
        JLabel busquedaLabel = new JLabel("Término de búsqueda:");
        JTextField busquedaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar");
        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        
        buscarButton.addActionListener(e -> {
            String busqueda = busquedaField.getText();
            if (busqueda.isEmpty()) {
                mostrarMensaje("Ingrese un término de búsqueda");
                return;
            }
            
            List<Cliente> resultados = new ArrayList<>();
            if (cedulaRadio.isSelected()) {
                Cliente cliente = parqueaderoService.buscarClientePorCedula(busqueda);
                if (cliente != null) resultados.add(cliente);
            } else if (nombreRadio.isSelected()) {
                resultados = parqueaderoService.buscarClientesPorNombre(busqueda);
            } else if (telefonoRadio.isSelected()) {
                resultados = parqueaderoService.buscarClientesPorTelefono(busqueda);
            }
            
            if (resultados.isEmpty()) {
                resultadosArea.setText("No se encontraron clientes");
            } else {
                StringBuilder mensaje = new StringBuilder("Clientes encontrados:\n\n");
                for (Cliente c : resultados) {
                    mensaje.append("Nombre: ").append(c.getNombre()).append("\n");
                    mensaje.append("Cédula: ").append(c.getCedula()).append("\n");
                    mensaje.append("Teléfono: ").append(c.getTelefono()).append("\n");
                    mensaje.append("Correo: ").append(c.getCorreo()).append("\n\n");
                }
                resultadosArea.setText(mensaje.toString());
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "clientes"));
        
        formPanel.add(radioPanel);
        formPanel.add(busquedaLabel);
        formPanel.add(busquedaField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "buscarCliente");
    }

    private void crearPanelRegistroVehiculo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Registro de Vehículo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel cedulaLabel = new JLabel("Cédula del cliente (opcional):");
        JTextField cedulaField = new JTextField();
        
        JLabel placaLabel = new JLabel("Placa:");
        JTextField placaField = new JTextField();
        
        JLabel tipoLabel = new JLabel("Tipo:");
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Automóvil", "Moto", "Camión"});
        
        JLabel colorLabel = new JLabel("Color:");
        JTextField colorField = new JTextField();
        
        JLabel modeloLabel = new JLabel("Modelo:");
        JTextField modeloField = new JTextField();
        
        formPanel.add(cedulaLabel);
        formPanel.add(cedulaField);
        formPanel.add(placaLabel);
        formPanel.add(placaField);
        formPanel.add(tipoLabel);
        formPanel.add(tipoCombo);
        formPanel.add(colorLabel);
        formPanel.add(colorField);
        formPanel.add(modeloLabel);
        formPanel.add(modeloField);
        
        JButton registrarButton = new JButton("Registrar");
        registrarButton.addActionListener(e -> {
            try {
                String cedula = cedulaField.getText();
                String placa = placaField.getText();
                String tipo = (String) tipoCombo.getSelectedItem();
                String color = colorField.getText();
                String modelo = modeloField.getText();
                
                if (placa.isEmpty()) {
                    mostrarMensaje("La placa es obligatoria");
                    return;
                }
                
                Cliente cliente = null;
                if (!cedula.isEmpty()) {
                    cliente = parqueaderoService.buscarClientePorCedula(cedula);
                    if (cliente == null) {
                        mostrarMensaje("Cliente no encontrado");
                        return;
                    }
                }
                
                Vehiculo vehiculo;
                switch(tipo) {
                    case "Automóvil":
                        vehiculo = new Automovil(placa, color, modelo, cliente);
                        break;
                    case "Moto":
                        vehiculo = new Moto(placa, color, modelo, cliente);
                        break;
                    case "Camión":
                        vehiculo = new Camion(placa, color, modelo, cliente);
                        break;
                    default:
                        mostrarMensaje("Tipo de vehículo no válido");
                        return;
                }
                
                parqueaderoService.registrarVehiculo(vehiculo);
                mostrarMensaje("Vehículo registrado exitosamente");
                limpiarCampos(cedulaField, placaField, colorField, modeloField);
                cardLayout.show(cardPanel, "vehiculos");
            } catch (Exception ex) {
                mostrarMensaje("Error: " + ex.getMessage());
            }
        });
        
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> {
            limpiarCampos(cedulaField, placaField, colorField, modeloField);
            cardLayout.show(cardPanel, "vehiculos");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(registrarButton);
        buttonPanel.add(cancelarButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "registroVehiculo");
    }

    private void crearPanelBuscarVehiculo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Buscar Vehículo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        ButtonGroup criterioGroup = new ButtonGroup();
        JRadioButton placaRadio = new JRadioButton("Por placa", true);
        JRadioButton tipoRadio = new JRadioButton("Por tipo");
        JRadioButton clienteRadio = new JRadioButton("Por cliente (cédula)");
        
        criterioGroup.add(placaRadio);
        criterioGroup.add(tipoRadio);
        criterioGroup.add(clienteRadio);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(placaRadio);
        radioPanel.add(tipoRadio);
        radioPanel.add(clienteRadio);
        
        JLabel busquedaLabel = new JLabel("Término de búsqueda:");
        JTextField busquedaField = new JTextField();
        
        JButton buscarButton = new JButton("Buscar");
        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);
        
        buscarButton.addActionListener(e -> {
            String busqueda = busquedaField.getText();
            if (busqueda.isEmpty()) {
                mostrarMensaje("Ingrese un término de búsqueda");
                return;
            }
            
            List<Vehiculo> resultados = new ArrayList<>();
            if (placaRadio.isSelected()) {
                Vehiculo vehiculo = parqueaderoService.buscarVehiculoPorPlaca(busqueda);
                if (vehiculo != null) resultados.add(vehiculo);
            } else if (tipoRadio.isSelected()) {
                resultados = parqueaderoService.buscarVehiculosPorTipo(busqueda);
            } else if (clienteRadio.isSelected()) {
                resultados = parqueaderoService.buscarVehiculosPorCliente(busqueda);
            }
            
            if (resultados.isEmpty()) {
                resultadosArea.setText("No se encontraron vehículos");
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
                resultadosArea.setText(mensaje.toString());
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> cardLayout.show(cardPanel, "vehiculos"));
        
        formPanel.add(radioPanel);
        formPanel.add(busquedaLabel);
        formPanel.add(busquedaField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "buscarVehiculo");
    }

    private void crearPanelMembresias() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titulo = new JLabel("Gestión de Membresías", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel placaLabel = new JLabel("Placa del vehículo:");
        JTextField placaField = new JTextField();
        
        JLabel tipoLabel = new JLabel("Tipo de membresía:");
        JComboBox<String> tipoMembresiaCombo = new JComboBox<>(new String[]{"1 mes", "3 meses", "1 año"});
        
        ButtonGroup operacionGroup = new ButtonGroup();
        JRadioButton asignarRadio = new JRadioButton("Asignar nueva membresía", true);
        JRadioButton renovarRadio = new JRadioButton("Renovar membresía existente");
        
        operacionGroup.add(asignarRadio);
        operacionGroup.add(renovarRadio);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(asignarRadio);
        radioPanel.add(renovarRadio);
        
        formPanel.add(placaLabel);
        formPanel.add(placaField);
        formPanel.add(tipoLabel);
        formPanel.add(tipoMembresiaCombo);
        formPanel.add(new JLabel("Operación:"));
        formPanel.add(radioPanel);
        
        JButton procesarButton = new JButton("Procesar");
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        
        procesarButton.addActionListener(e -> {
            String placa = placaField.getText();
            if (placa.isEmpty()) {
                mostrarMensaje("Ingrese la placa del vehículo");
                return;
            }
            
            String tipoMembresia = (String) tipoMembresiaCombo.getSelectedItem();
            boolean resultado;
            
            if (asignarRadio.isSelected()) {
                resultado = parqueaderoService.asignarMembresia(placa, tipoMembresia);
            } else {
                resultado = parqueaderoService.renovarMembresia(placa, tipoMembresia);
            }
            
            if (resultado) {
                String factura = parqueaderoService.generarFacturaMembresia(placa);
                resultadoArea.setText(factura);
            } else {
                resultadoArea.setText("Error al procesar la membresía");
            }
        });
        
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            placaField.setText("");
            resultadoArea.setText("");
            cardLayout.show(cardPanel, "vehiculos");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(procesarButton);
        buttonPanel.add(volverButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.add(panel, "membresias");
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(mainFrame, mensaje);
    }

    // Los métodos de la lógica de negocio (registrarCliente, buscarCliente, etc.) 
    // se mantienen igual que en la versión original, pero adaptados para trabajar 
    // con los componentes de JFrame en lugar de JOptionPane

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interface());
    }
}
package Controlador;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPanelWithBackground extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage backgroundImage;
    private float transparency = 1.0f; // 1.0 = completamente opaco

    public JPanelWithBackground(String imagePath) {
        setOpaque(false); // Hace que el panel sea transparente
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            backgroundImage = null;
        }
    }

    // Método para establecer la transparencia (nuevo)
    public void setTransparency(float transparency) {
        this.transparency = Math.max(0, Math.min(1, transparency)); // Asegura valor entre 0 y 1
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Aplica la transparencia
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
            
            // Dibuja la imagen escalada al tamaño del panel
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
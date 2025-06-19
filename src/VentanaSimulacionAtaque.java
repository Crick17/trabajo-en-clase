import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class VentanaSimulacionAtaque extends JFrame {
    private final String objetivo;
    private final GestorAtaques gestor;
    private int nivel = 1;
    private final List<String> camino = new ArrayList<>();
    private final Random random = new Random();

    public VentanaSimulacionAtaque(String objetivo) {
        this.objetivo = objetivo;
        this.gestor = new GestorAtaques();
        setTitle("Simulación de Ataque - Ciberseguridad");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        siguienteNivel(null);
    }

    private void siguienteNivel(String opcionAnterior) {
        getContentPane().removeAll();
        if (nivel > 5) {
            mostrarResultadoFinal();
            return;
        }

        List<Ataque> opciones = gestor.generarOpcionesNivel(nivel, opcionAnterior);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Nivel " + nivel + ": Selecciona un método de ataque"));

        for (Ataque atk : opciones) {
            JButton boton = new JButton("<html><center>" + atk.nombre + "<br><small>" + atk.descripcion + "</small></center></html>");
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(600, 60));
            boton.addActionListener(e -> {
                // Validar si opción es correcta (random)
                if (esOpcionCorrecta()) {
                    nivel++;
                    camino.add(atk.nombre + " - " + atk.descripcion);
                    siguienteNivel(atk.nombre);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Sistema Bloqueado. Escoga otro camino.",
                            "Bloqueo de Seguridad",
                            JOptionPane.WARNING_MESSAGE);
                    // No avanzar, dejar nivel actual para elegir otra opción
                }
            });
            panel.add(Box.createVerticalStrut(10));
            panel.add(boton);
        }

        panel.add(Box.createVerticalGlue());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        revalidate();
        repaint();
        setVisible(true);
    }

    private boolean esOpcionCorrecta() {
        // 70% de probabilidades que sea correcta, 30% bloqueada
        return random.nextDouble() < 0.7;
    }

    private void mostrarResultadoFinal() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Árbol de Decisiones del Ataque: " + objetivo, JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelArbol = new JPanel() {
            private final Color[] coloresNiveles = {
                    new Color(70, 130, 180),
                    new Color(46, 139, 87),
                    new Color(218, 112, 214),
                    new Color(255, 140, 0),
                    new Color(220, 20, 60)
            };

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int startX = width / 2;
                int startY = getHeight() - 60;
                int stepY = 100;
                int ovalWidth = 550;
                int ovalHeight = 50;

                g2.setFont(new Font("SansSerif", Font.BOLD, 14));

                for (int i = 0; i < camino.size(); i++) {
                    int x = startX - ovalWidth / 2;
                    int y = startY - (i * stepY);

                    Color nivelColor = coloresNiveles[i % coloresNiveles.length];
                    g2.setColor(nivelColor);
                    g2.fillRoundRect(x, y, ovalWidth, ovalHeight, 30, 30);

                    g2.setColor(Color.WHITE);
                    drawStringMultiLine(g2, camino.get(i), x + ovalWidth / 2, y + ovalHeight / 2);

                    if (i < camino.size() - 1) {
                        g2.setColor(Color.DARK_GRAY);
                        g2.setStroke(new BasicStroke(3));
                        int lineX = startX;
                        int lineY1 = y;
                        int lineY2 = y - stepY + ovalHeight;
                        g2.drawLine(lineX, lineY1, lineX, lineY2);
                    }
                }

                int finalY = startY - (camino.size() * stepY);
                int objWidth = 400;
                int objHeight = 60;
                int objX = startX - objWidth / 2;

                g2.setColor(new Color(139, 0, 0));
                g2.fillRoundRect(objX, finalY, objWidth, objHeight, 40, 40);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                drawStringMultiLine(g2, "🎯 Objetivo:\n" + objetivo, objX + objWidth / 2, finalY + objHeight / 2);
            }

            private void drawStringMultiLine(Graphics2D g2, String text, int xCenter, int yCenter) {
                String[] lines = text.split(" - ");
                FontMetrics fm = g2.getFontMetrics();
                int totalHeight = lines.length * fm.getHeight();
                int y = yCenter - totalHeight / 2 + fm.getAscent();

                for (String line : lines) {
                    int lineWidth = fm.stringWidth(line);
                    g2.drawString(line, xCenter - lineWidth / 2, y);
                    y += fm.getHeight();
                }
            }
        };

        panelArbol.setPreferredSize(new Dimension(700, 700));
        add(new JScrollPane(panelArbol), BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class VentanaSimulacionAtaque extends JFrame {
    private final String objetivo;
    private final GestorAtaques gestor;
    private int nivel = 1;
    private final List<String> camino = new ArrayList<>();
    private final List<List<String>> opcionesPorNivel = new ArrayList<>();
    private final Random random = new Random();

    public VentanaSimulacionAtaque(String objetivo) {
        this.objetivo = objetivo;
        this.gestor = new GestorAtaques();
        setTitle("Simulación de Ataque - Ciberseguridad");
        setSize(900, 800);
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
        List<String> nombres = new ArrayList<>();
        for (Ataque a : opciones) {
            nombres.add(a.nombre + " - " + a.descripcion);
        }
        opcionesPorNivel.add(nombres);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Nivel " + nivel + ": Selecciona un método de ataque"));

        for (Ataque atk : opciones) {
            String text = atk.nombre + " - " + atk.descripcion;
            JButton boton = new JButton("<html><center>" + atk.nombre + "<br><small>" + atk.descripcion + "</small></center></html>");
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(700, 60));
            boton.addActionListener(e -> {
                if (esOpcionCorrecta()) {
                    camino.add(text);
                    nivel++;
                    SwingUtilities.invokeLater(() -> siguienteNivel(atk.nombre));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ Sistema Bloqueado. Escoge otro camino.",
                            "Bloqueo de Seguridad",
                            JOptionPane.WARNING_MESSAGE);
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
        return random.nextDouble() < 0.7;
    }

    private void mostrarResultadoFinal() {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("🔐 Árbol de Decisiones del Ataque: " + objetivo, JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelArbol = new JPanel() {
            private final Color OPCION = new Color(200, 200, 200);
            private final Color ELEGIDA = new Color(0, 160, 0);
            private final Color OBJETIVO = new Color(180, 0, 0);

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int W = getWidth(), H = getHeight();
                int levels = opcionesPorNivel.size();
                int spacingY = (H - 100) / (levels + 1);
                int ovalW = 280, ovalH = 50;

                for (int lvl = 0; lvl < levels; lvl++) {
                    List<String> lista = opcionesPorNivel.get(lvl);
                    int count = lista.size();
                    for (int j = 0; j < count; j++) {
                        String s = lista.get(j);
                        int x = (j + 1) * W / (count + 1) - ovalW / 2;
                        int y = 80 + (lvl + 1) * spacingY - ovalH / 2;

                        boolean elegido = (lvl < camino.size() && camino.get(lvl).equals(s));
                        g2.setColor(elegido ? ELEGIDA : OPCION);
                        g2.fillRoundRect(x, y, ovalW, ovalH, 30, 30);

                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        drawStringMultiLine(g2, s, x + ovalW / 2, y + ovalH / 2);

                        // Conexiones desde nivel anterior
                        if (lvl > 0) {
                            List<String> prev = opcionesPorNivel.get(lvl - 1);
                            int px = (j % prev.size() + 1) * W / (prev.size() + 1);
                            int py = 80 + lvl * spacingY;
                            int cx = x + ovalW / 2;
                            int cy = y;
                            g2.setColor(Color.GRAY);
                            g2.setStroke(new BasicStroke(2));
                            g2.drawLine(px, py, cx, cy);
                        }
                    }
                }

                // Dibuja objetivo
                int ox = W / 2 - ovalW / 2, oy = 40;
                g2.setColor(OBJETIVO);
                g2.fillRoundRect(ox, oy, ovalW, ovalH, 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                drawStringMultiLine(g2, "🎯 Objetivo: " + objetivo, W / 2, oy + ovalH / 2);
            }

            private void drawStringMultiLine(Graphics2D g2, String text, int cx, int cy) {
                String[] lines = text.split(" - ");
                FontMetrics fm = g2.getFontMetrics();
                int totalHeight = lines.length * fm.getHeight();
                int y = cy - totalHeight / 2 + fm.getAscent();
                for (String line : lines) {
                    int w = fm.stringWidth(line);
                    g2.drawString(line, cx - w / 2, y);
                    y += fm.getHeight();
                }
            }
        };

        // ✅ FIX: Calcular la altura del panel en base al número de niveles
        panelArbol.setPreferredSize(new Dimension(900, opcionesPorNivel.size() * 150 + 200));
        add(new JScrollPane(panelArbol), BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}

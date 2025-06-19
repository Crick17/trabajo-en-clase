import javax.swing.*;

public class VentanaSeleccionObjetivo extends JFrame {
    public VentanaSeleccionObjetivo() {
        setTitle("Árbol de Ataques - Selección de Objetivo");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] objetivos = {
                "Acceso a Base de Datos",
                "Control Total del Sistema",
                "Robo de Credenciales"
        };

        JComboBox<String> comboObjetivos = new JComboBox<>(objetivos);
        JButton btnContinuar = new JButton("Iniciar Simulación");

        btnContinuar.addActionListener(e -> {
            String objetivo = (String) comboObjetivos.getSelectedItem();
            dispose();
            new VentanaSimulacionAtaque(objetivo);
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Selecciona el objetivo del ataque:"));
        panel.add(comboObjetivos);
        panel.add(btnContinuar);

        add(panel);
        setVisible(true);
    }
}

import java.util.*;

public class GestorAtaques {
    private final Random random = new Random();

    private final List<Ataque> ataquesBase = List.of(
            new Ataque("Phishing", "Engaño por correo electrónico."),
            new Ataque("Exploit de Software", "Aprovechar vulnerabilidad."),
            new Ataque("Ingeniería Social", "Manipulación humana.")
    );

    public List<Ataque> generarOpcionesNivel(int nivel, String anterior) {
        List<Ataque> opciones = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Ataque base = ataquesBase.get(random.nextInt(ataquesBase.size()));
            opciones.add(new Ataque(base.nombre + " Nivel " + nivel,
                    base.descripcion + " [Nivel " + nivel + "]"));
        }
        return opciones;
    }
}

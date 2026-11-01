package io.github.agraria.control;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.agraria.economia.Billetera;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.tienda.VentanaTienda;

public class ControlTienda {

    private Stage stage;
    private Billetera billetera;
    private Inventario inventario;
    private Skin skin;

    private VentanaTienda ventana;

    public ControlTienda(Stage stage, Billetera billetera,
                         Inventario inventario, Skin skin) {

        this.stage = stage;
        this.billetera = billetera;
        this.inventario = inventario;
        this.skin = skin;
    }

    public void abrir() {

        // Evitar crear varias ventanas
        if (ventana != null && ventana.estaAbierta()) {
            return;
        }

        ventana = new VentanaTienda(stage, billetera, inventario, skin);
    }
    
    public void cerrar() {
        if (ventana != null) {
            ventana.cerrarVentana(); // Llama al método que limpia la ventana del Stage
            ventana = null;          // Limpiamos la referencia para poder abrirla de cero después
        }
        System.out.println("-> Tienda cerrada.");
    }
    
    public boolean estaAbierta() {
        return ventana != null && ventana.estaAbierta();
    }
    
}
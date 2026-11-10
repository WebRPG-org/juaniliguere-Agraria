
package io.github.agraria;

import com.badlogic.gdx.Game;

import io.github.agraria.pantallas.PantallaMenu;
import io.github.agraria.ui.GestorFuentes;

public class Agraria extends Game {

    private GestorFuentes gestorFuentes;

    @Override
    public void create() {

        gestorFuentes = new GestorFuentes();
        gestorFuentes.cargar();

        setScreen(new PantallaMenu(this));
    }

    public GestorFuentes getGestorFuentes() {
        return gestorFuentes;
    }

    @Override
    public void dispose() {

        super.dispose();

        if (gestorFuentes != null) {
            gestorFuentes.dispose();
        }
    }
}

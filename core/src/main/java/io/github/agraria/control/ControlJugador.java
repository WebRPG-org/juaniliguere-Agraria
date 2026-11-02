package io.github.agraria.control;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import io.github.agraria.inventario.Inventario;
import io.github.agraria.pantallas.PantallaGranja;
import io.github.agraria.personajes.Personaje;
import io.github.agraria.tienda.VentanaTienda;

public class ControlJugador extends InputAdapter {
    
    private Personaje jugador;
    private PantallaGranja pantalla;
    private Inventario inventario;

    public ControlJugador(Personaje jugador, PantallaGranja pantalla, Inventario inventario) {
        this.jugador = jugador;
        this.pantalla = pantalla;        
        this.inventario = inventario;
    }

    // --- MANEJO DE MOVIMIENTO FLUIDO EN DIAGONAL ---
    // En lugar de depender de keyUp/keyDown para las direcciones, 
    // consultamos el teclado en cada frame.
    public void actualizarMovimiento() {
        int dirX = 0;
        int dirY = 0;

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))    dirY = 1;
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))  dirY = -1;
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))  dirX = -1;
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) dirX = 1;

        jugador.dirX = dirX;
        jugador.dirY = dirY;
    }

    // LibGDX ejecuta esto SOLO cuando APRETÁS una tecla (ideal para acciones puntuales)
    @Override
    public boolean keyDown(int keycode) {  
        
        if (keycode == Keys.Z) pantalla.procesarDormir();       
        if (keycode == Keys.T) pantalla.procesarAbrirTienda();
        
        // Selección del inventario
        if (keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9) {
            inventario.setCasillaSeleccionada(keycode - Keys.NUM_1);
        }

        if (keycode == Keys.NUM_0) {
            inventario.setCasillaSeleccionada(9);
        }
        
        return true;
    }

    // Ya no necesitamos apagar las direcciones en keyUp porque se actualizan solas por frame.
    @Override
    public boolean keyUp(int keycode) {  
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
            pantalla.procesarCultivar(screenX, screenY);
            return true;
        } else if (button == com.badlogic.gdx.Input.Buttons.RIGHT) {
            pantalla.procesarCosechar(screenX, screenY);
            return true;
        }       
        return false;
    }
}
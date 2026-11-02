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
 // Bandera para congelar al jugador (por ejemplo, cuando abre la tienda)
    private boolean movimientoBloqueado = false;

    public ControlJugador(Personaje jugador, PantallaGranja pantalla, Inventario inventario) {
        this.jugador = jugador;
        this.pantalla = pantalla;        
        this.inventario = inventario;
    }

    public void setMovimientoBloqueado(boolean bloqueado) {
        this.movimientoBloqueado = bloqueado;

        if (bloqueado) {
            jugador.dirX = 0;
            jugador.dirY = 0;
        }
    }
    
    public void actualizarMovimiento() {

        if (movimientoBloqueado) {
            jugador.dirX = 0;
            jugador.dirY = 0;
            return;
        }

        int dirX = 0;
        int dirY = 0;

        boolean arriba = Gdx.input.isKeyPressed(Keys.W)
                || Gdx.input.isKeyPressed(Keys.UP);

        boolean abajo = Gdx.input.isKeyPressed(Keys.S)
                || Gdx.input.isKeyPressed(Keys.DOWN);

        boolean izquierda = Gdx.input.isKeyPressed(Keys.A)
                || Gdx.input.isKeyPressed(Keys.LEFT);

        boolean derecha = Gdx.input.isKeyPressed(Keys.D)
                || Gdx.input.isKeyPressed(Keys.RIGHT);

        // Movimiento exclusivamente horizontal o vertical
        if (arriba && !abajo) {
            dirY = 1;
        } else if (abajo && !arriba) {
            dirY = -1;
        } else if (izquierda && !derecha) {
            dirX = -1;
        } else if (derecha && !izquierda) {
            dirX = 1;
        }

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
    	
        if (movimientoBloqueado) {
            return true;
        }
        
        
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
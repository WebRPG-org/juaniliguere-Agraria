
package io.github.agraria.control;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import io.github.agraria.inventario.Inventario;
import io.github.agraria.personajes.Personaje;

public class ControlJugador extends InputAdapter {

    @FunctionalInterface
    public interface AccionMouse {
        void ejecutar(int screenX, int screenY);
    }
    
    private Personaje jugador;
    private Inventario inventario;

    private boolean movimientoBloqueado = false;

    // Acciones que puede ejecutar el jugador
    private Runnable accionDormir;
    private Runnable accionTienda;
    private AccionMouse accionCultivar;
    private AccionMouse accionCosechar;

    public ControlJugador(Personaje jugador, Inventario inventario) {
        this.jugador = jugador;
        this.inventario = inventario;
    }

    // Configuración de las acciones

    public void setAccionDormir(Runnable accion) {
        this.accionDormir = accion;
    }

    public void setAccionTienda(Runnable accion) {
        this.accionTienda = accion;
    }

    public void setAccionCultivar(AccionMouse accion) {
        this.accionCultivar = accion;
    }

    public void setAccionCosechar(AccionMouse accion) {
        this.accionCosechar = accion;
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

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Keys.T && accionTienda != null) {
            accionTienda.run();
        }
        
        if (movimientoBloqueado) {
            return true;
        }

        if (keycode == Keys.Z && accionDormir != null) {
            accionDormir.run();
        }


        // Selección del inventario
        if (keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9) {
            inventario.setCasillaSeleccionada(keycode - Keys.NUM_1);
        }

        if (keycode == Keys.NUM_0) {
            inventario.setCasillaSeleccionada(9);
        }

        return true;
    }

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

            if (accionCultivar != null) {
                accionCultivar.ejecutar(screenX, screenY);
            }

            return true;

        } else if (button == com.badlogic.gdx.Input.Buttons.RIGHT) {

            if (accionCosechar != null) {
                accionCosechar.ejecutar(screenX, screenY);
            }

            return true;
        }

        return false;
    }
}

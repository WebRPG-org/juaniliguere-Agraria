package io.github.agraria.control;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.agraria.pantallas.PantallaMenu;


public class ControlEntrada extends InputAdapter {
	

    private boolean solicitioSalirMenu = false;

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
        	System.out.println("se apreto esc");
            solicitioSalirMenu = true;
            return true;
        }
        return false;
    }

    public boolean isSolicitioSalirMenu() {
        return solicitioSalirMenu;
    }

  
}



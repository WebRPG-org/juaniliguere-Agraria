package io.github.agraria.pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.agraria.Agraria;

public class PantallaConfiguracion extends ScreenAdapter {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float MAP_WIDTH = 480; 
    private static final float MAP_HEIGHT = 320;

    @Override
    public void show() {
        map = new TmxMapLoader().load("pantallas/configuracion/pantallaConfiguracion.tmx");

        camera = new OrthographicCamera();
        viewport = new StretchViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        viewport.apply();

        // --- ACÁ ESTÁ EL SECRETO: Centrar la cámara en la mitad del mapa ---
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenu((Agraria) Gdx.app.getApplicationListener()));
        }

        // Asegurate de actualizar la cámara antes de pasarla al renderer
        camera.update();
        renderer.setView(camera);
        renderer.render();
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (map != null) map.dispose();
        if (renderer != null) renderer.dispose();
    }
}
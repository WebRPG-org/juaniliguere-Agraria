package io.github.agraria.pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaCarga extends ScreenAdapter {
   
	private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private OrthographicCamera camera;
    private Viewport viewport;

    private float tiempoCargando = 0f;
    private final float TIEMPO_MINIMO_CARGA = 2.0f; // Tiempo en segundos que dura la pantalla
    private boolean cambioRealizado = false;

    @Override
    public void show() {
    	
//        map = new TmxMapLoader().load("pantallas/carga/pantallaCarga.tmx");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }

    @Override
    public void render(float delta) {
        // Limpiamos pantalla con fondo negro
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiempoCargando += delta;
        float progreso = Math.min(1f, tiempoCargando / TIEMPO_MINIMO_CARGA);

        viewport.apply();
        camera.update();

        // -------------------------------------------------------------
        // 1. DIBUJAR BARRA DE PROGRESO (ShapeRenderer)
        // -------------------------------------------------------------
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float anchoBarraMax = 400f;
        float altoBarra = 20f;
        float xBarra = (800f - anchoBarraMax) / 2f;
        float yBarra = 200f;

        // Fondo de la barra (Gris oscuro)
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(xBarra, yBarra, anchoBarraMax, altoBarra);

        shapeRenderer.setColor(0.9f, 0.8f, 0.3f, 1f);
        shapeRenderer.rect(xBarra, yBarra, anchoBarraMax * progreso, altoBarra);

        shapeRenderer.end();

        // -------------------------------------------------------------
        // 2. DIBUJAR TEXTO DE CARGA (SpriteBatch)
        // -------------------------------------------------------------
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        String textoCarga = "Hola...";
        if (progreso > 0.6f) textoCarga = "Klk gente...";
        if (progreso >= 1.0f) textoCarga = "listo";

        font.draw(batch, textoCarga, xBarra, yBarra + 50);
        font.draw(batch, (int) (progreso * 100) + "%", xBarra + anchoBarraMax - 50, yBarra + 50);

        batch.end();

        // -------------------------------------------------------------
        // 3. CAMBIO AUTOMÁTICO A PANTALLA GRANJA AL TERMINAR
        // -------------------------------------------------------------
        
        
        if (tiempoCargando >= TIEMPO_MINIMO_CARGA && !cambioRealizado) {
            cambioRealizado = true;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaGranja());
        }
        
//        // Asegurate de actualizar la cámara antes de pasarla al renderer
//        camera.update();
//        renderer.setView(camera);
//        renderer.render();
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
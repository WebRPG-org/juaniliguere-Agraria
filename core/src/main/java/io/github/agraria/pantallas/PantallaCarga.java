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
import io.github.agraria.Agraria;

public class PantallaCarga extends ScreenAdapter {
   
	private Agraria juego;
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

    public PantallaCarga(Agraria juego) {
        this.juego = juego;
    }
    
    @Override
    public void show() {
    	
//        map = new TmxMapLoader().load("pantallas/carga/pantallaCarga.tmx");
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = juego.getGestorFuentes().getFuentePrincipal();
    }


@Override
public void render(float delta) {

    Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    tiempoCargando += delta;
    float progreso = Math.min(1f, tiempoCargando / TIEMPO_MINIMO_CARGA);

    viewport.apply();
    camera.update();

    // Dimensiones y posición de la barra
    float anchoBarraMax = 400f;
    float altoBarra = 20f;
    float xBarra = (800f - anchoBarraMax) / 2f;
    float yBarra = 200f;

    // Dibujar barra de progreso
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

    shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
    shapeRenderer.rect(xBarra, yBarra, anchoBarraMax, altoBarra);

    shapeRenderer.setColor(0.9f, 0.8f, 0.3f, 1f);
    shapeRenderer.rect(xBarra, yBarra, anchoBarraMax * progreso, altoBarra);

    shapeRenderer.end();

    // Determinar texto
    String textoCarga = "Hola...";

    if (progreso > 0.6f) {
        textoCarga = "Klk gente...";
    }

    if (progreso >= 1f) {
        textoCarga = "Listo";
    }

    // Dibujar texto
    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    font.getData().setScale(1.5f);

    font.draw(batch, textoCarga, xBarra, yBarra + 50);
    font.draw(batch, (int) (progreso * 100) + "%",
            xBarra + anchoBarraMax - 50, yBarra + 50);

    font.getData().setScale(1f);

    batch.end();

    // Cambiar a la pantalla de granja
    if (tiempoCargando >= TIEMPO_MINIMO_CARGA && !cambioRealizado) {
        cambioRealizado = true;
        juego.setScreen(new PantallaGranja(juego));
    }
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
package io.github.agraria.pantallas;

import com.badlogic.gdx.Game;
import io.github.agraria.Agraria;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.agraria.sonidos.GestorAudio;

public class PantallaPausa extends ScreenAdapter {

    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
	private PantallaGranja pantallaGranja;

    public PantallaPausa(PantallaGranja pantallaGranja) {
        this.pantallaGranja = pantallaGranja;
    }
    
    @Override
    public void show() {
    	
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        stage = new Stage(new FitViewport(800, 600, camera));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(stage);

        // Crear una ventana flotante centrada
        Table tabla = new Table();
        tabla.center();
        tabla.setFillParent(true);

        TextButton btnContinuar = new TextButton("Continuar", skin);
        TextButton btnSalir = new TextButton("Salir al Menu", skin);

        // --- ACCIÓN DEL BOTÓN CONTINUAR ---
        btnContinuar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                ((Game) Gdx.app.getApplicationListener()).setScreen(pantallaGranja);
            }
        });

        // --- ACCIÓN DEL BOTÓN SALIR AL MENÚ ---
        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	
                GestorAudio.apagarMusicaGlobal();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenu((Agraria) Gdx.app.getApplicationListener()));
                
            }
        });
        


        tabla.add(btnContinuar).width(200).height(50).padBottom(20).row();
        tabla.add(btnSalir).width(200).height(50);

        stage.addActor(tabla);
    }

    @Override
    public void render(float delta) {
        // Dibujamos una capa semitransparente oscura encima del juego para dar efecto de "pausa"
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f); // Negro con 60% de opacidad
        shapeRenderer.rect(0, 0, 800, 600);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Dibujamos la ventana de la UI encima
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        shapeRenderer.dispose();
    }
}
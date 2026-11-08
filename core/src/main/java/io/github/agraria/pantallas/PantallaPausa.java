package io.github.agraria.pantallas;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
public class PantallaPausa extends ScreenAdapter{

	private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

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

        tabla.add(btnContinuar).width(200).height(50).padBottom(20).row();
        tabla.add(btnSalir).width(200).height(50);

        stage.addActor(tabla);
    }

    @Override
    public void render(float delta) {
        // 1. IMPORTANTE: NO limpiamos la pantalla con ScreenUtils.clear para que lo de atrás no se borre.
        
        // 2. Dibujamos una capa semitransparente oscura encima del juego para dar efecto de "pausa"
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f); // Negro con 60% de opacidad
        shapeRenderer.rect(0, 0, 800, 600);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 3. Dibujamos la ventana de la UI encima
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

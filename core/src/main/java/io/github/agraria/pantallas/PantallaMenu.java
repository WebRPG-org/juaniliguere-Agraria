package io.github.agraria.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Importaciones de Scene2D y UI
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class PantallaMenu implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    // Variables de Scene2D y texturas de los botones
    private Stage stage;
    private Texture tilesetTexture; // Textura de la imagen del tileset

    // Variables para la transición a negro
    private ShapeRenderer shapeRenderer;
    private float fadeAlpha = 0f;          // Controla la transparencia (0 a 1)
    private boolean isTransitioning = false; // Bandera para activar la animación

    private static final float MAP_WIDTH = 480; 
    private static final float MAP_HEIGHT = 320;

    @Override
    public void show() {
        // 1. Cargar el mapa Tiled
        map = new TmxMapLoader().load("pantallas/menu/AgrariaMenu.tmx");

        // 2. Configurar cámara y viewport con las medidas reales del mapa
        camera = new OrthographicCamera();
        viewport = new StretchViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        viewport.apply();

        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map);

        // Inicializar el ShapeRenderer para dibujar el rectángulo negro de la transición
        shapeRenderer = new ShapeRenderer();

        // --- 3. CONFIGURACIÓN DE SCENE2D Y BOTONES DESDE EL TILESET ---
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // CARGAR LA IMAGEN DEL TILESET
        tilesetTexture = new Texture(Gdx.files.internal("pantallas/menu/Wooden Pixel Art GUI 32x32.png"));

        // RECORTRAR LOS BOTONES
        TextureRegion regionBtnJugarNormal = new TextureRegion(tilesetTexture, 130, 320, 126, 32);
        TextureRegion regionBtnJugarPress = new TextureRegion(tilesetTexture, 130, 352, 126, 30); 

        // Crear los estilos de los ImageButton
        ImageButton.ImageButtonStyle estiloJugar = new ImageButton.ImageButtonStyle();
        estiloJugar.imageUp = new TextureRegionDrawable(regionBtnJugarNormal);
        estiloJugar.imageDown = new TextureRegionDrawable(regionBtnJugarPress);

        // Instanciar los botones con sus estilos
        ImageButton btnJugar = new ImageButton(estiloJugar);

        // Acción al hacer clic en "Jugar"
        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Activamos la bandera para que comience el fundido a negro
                isTransitioning = true;
            }
        });
        
        
        
        TextureRegion regionBtnConfiguracionNormal = new TextureRegion(tilesetTexture, 130, 64, 126, 32); 
        TextureRegion regionBtnConfiguracionPress = new TextureRegion(tilesetTexture, 130, 97, 126, 30);
        
        ImageButton.ImageButtonStyle estiloConfiguracion = new ImageButton.ImageButtonStyle();
        estiloConfiguracion.imageUp = new TextureRegionDrawable(regionBtnConfiguracionNormal);
        estiloConfiguracion.imageDown = new TextureRegionDrawable(regionBtnConfiguracionPress);

        ImageButton btnConfiguracion= new ImageButton(estiloConfiguracion);
        
       
        btnConfiguracion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Activamos la bandera para que comience el fundido a negro
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaConfiguracion());
            }
        });
        
        
        
        TextureRegion regionBtnControlesNormal = new TextureRegion(tilesetTexture, 130, 128, 126, 32); 
        TextureRegion regionBtnControlesPress = new TextureRegion(tilesetTexture, 130, 160, 126, 30);
        
        ImageButton.ImageButtonStyle estiloControles = new ImageButton.ImageButtonStyle();
        estiloControles.imageUp = new TextureRegionDrawable(regionBtnControlesNormal);
        estiloControles.imageDown = new TextureRegionDrawable(regionBtnControlesPress);

        ImageButton btnControles = new ImageButton(estiloControles);
        
        
        
        TextureRegion regionBtnSalirNormal = new TextureRegion(tilesetTexture, 130, 512, 126, 32); 
        TextureRegion regionBtnSalirPress = new TextureRegion(tilesetTexture, 130, 545, 126, 30);
        
        ImageButton.ImageButtonStyle estiloSalir = new ImageButton.ImageButtonStyle();
        estiloSalir.imageUp = new TextureRegionDrawable(regionBtnSalirNormal);
        estiloSalir.imageDown = new TextureRegionDrawable(regionBtnSalirPress);

        ImageButton btnSalir = new ImageButton(estiloSalir);
        
     // Acción al hacer clic en "Controls"
        btnControles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Activamos la bandera para que comience el fundido a negro
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaControles());
            }
        });
        

        // Crear una tabla para organizar y posicionar los botones en pantalla
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Agregar los botones a la tabla con tamaño y separación (padding)
        table.add(btnJugar).width(126).height(32).padBottom(10);
        table.row();
        table.add(btnConfiguracion).width(126).height(32).padBottom(15);
        table.row();
        table.add(btnControles).width(126).height(32).padBottom(5);
        table.row();
        table.add(btnSalir).width(126).height(32).padBottom(-65).padLeft(35);
        table.row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renderizar el mapa de fondo
        camera.update();
        renderer.setView(camera);
        renderer.render();

        // Actualizar y renderizar los botones de Scene2D encima
        stage.act(delta);
        stage.draw();

        // --- 4. LÓGICA DE LA TRANSICIÓN A NEGRO ---
        if (isTransitioning) {
            // Velocidad del fundido (1.5f ajusta qué tan rápido se pone en negro)
            fadeAlpha += delta * 1.5f; 
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                
                // ACÁ CAMBIÁS A TU PANTALLA DE JUEGO CUANDO TERMINA EL NEGRO:
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaCarga());
            }
        }

        // Dibujar el rectángulo negro translúcido por encima de todo si la animación está activa
        if (fadeAlpha > 0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, fadeAlpha);
            shapeRenderer.rect(0, 0, MAP_WIDTH, MAP_HEIGHT);
            shapeRenderer.end();
            
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override 
    public void hide() {
        // Quitar el procesador de entrada para evitar que el usuario interactúe durante el cambio
        Gdx.input.setInputProcessor(null);
    }


    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        stage.dispose();
        if (tilesetTexture != null) {
            tilesetTexture.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose(); // Liberar memoria del ShapeRenderer
        }
    }

}




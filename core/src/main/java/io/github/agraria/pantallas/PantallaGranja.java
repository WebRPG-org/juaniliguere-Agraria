package io.github.agraria.pantallas;

import io.github.agraria.cultivos.GestorCultivos;
import io.github.agraria.cultivos.Cultivo;
import io.github.agraria.cultivos.Parcela;
import io.github.agraria.cultivos.TipoCultivo;
import io.github.agraria.elementos.Item;
import io.github.agraria.eventos.*;
import io.github.agraria.inventario.GestorInventario;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.economia.Billetera;
import io.github.agraria.tienda.VentanaTienda;
import io.github.agraria.control.ControlJugador;
import io.github.agraria.control.ControlTienda;
import io.github.agraria.personajes.Personaje;
import io.github.tiempo.Reloj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PantallaGranja extends ScreenAdapter {

    private SpriteBatch batch;
    private Personaje jugador;

    private GestorMapa controlMapa;
    
    private GestorMapa mapaGranja;
    private GestorMapa mapaTienda;

    // Estado de la ubicación del jugador
    private boolean enTienda = false;

    // Posición del jugador antes de entrar
    private float posicionAnteriorX;
    private float posicionAnteriorY;
   
    private OrthographicCamera camara;
    private Viewport vp;

    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 600;

    private GestorCultivos gestorCultivos;

    // Guarda las etapas de cada tipo de cultivo.
    private ObjectMap<TipoCultivo, TextureRegion[]> etapasCultivos;
    // Texturas originales que debemos liberar al cerrar la pantalla
    private ObjectMap<TipoCultivo, Texture> texturasCultivos;
    
    private ObjectMap<TipoCultivo, Texture> texturasPodridas;

    private Reloj reloj;
    private ControlSueno controlSueno;
    private ControlClima controlClima;
    
    // --- HUD Y TEXTO ---
    private BitmapFont font;
    private OrthographicCamera hudCamara;
    private ShapeRenderer shapeRenderer;
    
    private Rectangle zonaDormir;
    private Rectangle zonaTienda;
    private Rectangle zonaComprar;
    
    private Inventario inventario;
    private GestorInventario gestorInventario;

    // --- ECONOMÍA Y TIENDA ---
    private Billetera billetera;
    private Stage stageUi; // Stage independiente para manejar la UI de la tienda
    private Skin skinUi;   // Skin por defecto o la que uses para la UI
    private ControlTienda controlTienda;
    
    
    private Window ventanaMensaje;
    private Label textoMensaje;

    public PantallaGranja() {

        batch = new SpriteBatch();
        jugador = new Personaje(125, 125);
        shapeRenderer = new ShapeRenderer();

        // =========================
        // CÁMARA Y VIEWPORT
        // =========================

        camara = new OrthographicCamera();
        vp = new FitViewport(V_WIDTH, V_HEIGHT, camara);
        vp.apply();
        camara.zoom = 0.5f;
        
        hudCamara = new OrthographicCamera();
        hudCamara.setToOrtho(false, V_WIDTH, V_HEIGHT);
        font = new BitmapFont();

        mapaGranja = new GestorMapa("pantallas/zona1/AgrariaMapa.tmx");
        mapaTienda = new GestorMapa("pantallas/tienda/interiorTienda.tmx");
       
        controlMapa = mapaGranja;
        
        zonaDormir = controlMapa.getZonaDormir();
        zonaTienda = controlMapa.getZonaTienda();
        zonaComprar = controlMapa.getZonaComprar();
        
        this.inventario = new Inventario(10);
        this.gestorInventario = new GestorInventario(inventario);

        // Inicializamos la billetera con 100 monedas de base
        this.billetera = new Billetera(100);
        
     // Stage con la cámara fija del HUD para que no se mueva con el zoom del mapa
        stageUi = new Stage(new FitViewport(V_WIDTH, V_HEIGHT, hudCamara));
        skinUi = new Skin(Gdx.files.internal("uiskin.json"));
        
        this.controlTienda = new ControlTienda(stageUi, billetera, inventario, skinUi);

        // =========================
        // CONTROL DE CULTIVOS
        // =========================

        boolean[][] matrizPlantable = GestorCultivos.generarMatrizPlantableDesdeMapa(
                controlMapa.getMapa(),
                controlMapa.getMapWidthTiles(),
                controlMapa.getMapHeightTiles()
        );

        gestorCultivos = new GestorCultivos(
                controlMapa.getMapWidthTiles(),
                controlMapa.getMapHeightTiles(),
                controlMapa.getTileWidth(),    
                matrizPlantable
        );

        cargarTexturasCultivos();
        
        reloj = new Reloj();
        controlClima = new ControlClima(gestorCultivos);
        controlSueno = new ControlSueno();
    }

    // Carga automáticamente las texturas de todos los tipos de cultivo existentes.
    private void cargarTexturasCultivos() {
        etapasCultivos = new ObjectMap<>();
        texturasCultivos = new ObjectMap<>();
        texturasPodridas = new ObjectMap<>();

        for (TipoCultivo tipo : TipoCultivo.values()) {
            Texture textura = new Texture(tipo.getRutaTextura());
            textura.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            texturasCultivos.put(tipo, textura);

            int cantidadEtapas = tipo.getCantidadEtapas();
            int anchoEtapa = textura.getWidth() / cantidadEtapas;
            int altoEtapa = textura.getHeight();

            TextureRegion[] etapas = new TextureRegion[cantidadEtapas];

            for (int i = 0; i < cantidadEtapas; i++) {
                etapas[i] = new TextureRegion(textura, i * anchoEtapa, 0, anchoEtapa, altoEtapa);
            }

            etapasCultivos.put(tipo, etapas);
            
            Texture texturaPodrida = new Texture(tipo.getRutaTexturaPodrido());

            texturaPodrida.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
            );

            texturasPodridas.put(tipo, texturaPodrida);
        }
    }


    @Override
    public void show() {
        ControlJugador controlador = new ControlJugador(jugador, this, inventario);
        
        // Combinamos la UI y el control del jugador para que ambos reciban eventos
        com.badlogic.gdx.InputMultiplexer multiplexer = new com.badlogic.gdx.InputMultiplexer();
        multiplexer.addProcessor(stageUi);   // Primero la UI (botones de la tienda)
        multiplexer.addProcessor(controlador); // Después el juego
        
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void procesarCultivar(int screenX, int screenY) {
        Vector3 posMundo = camara.unproject(new Vector3(screenX, screenY, 0));
        Parcela parcela = gestorCultivos.getParcelaEnPx(posMundo.x, posMundo.y);

        if (parcela == null) return;

        if (!parcela.tieneCultivo() && parcela.isEsPlantable()) {
            
            // Obtenemos el tipo de cultivo que corresponde a la semilla seleccionada
            TipoCultivo cultivoSeleccionado = inventario.getCultivoSeleccionado();

            if (cultivoSeleccionado != null) {
                // Plantamos en la parcela
                boolean exito = gestorCultivos.plantarEn(posMundo.x, posMundo.y, cultivoSeleccionado);
                
                if (exito) {
                    // Consumimos 1 unidad de la semilla de la mano
                    inventario.consumirSeleccionado(1);
                    System.out.println("-> ¡Semilla plantada con éxito!");
                }
            } else {
                System.out.println("-> No tenés seleccionada ninguna semilla válida en la mano.");
            }
        }
    }
    
    public void procesarCosechar(int screenX, int screenY) {
        Vector3 posMundo = camara.unproject(new Vector3(screenX, screenY, 0));
        Parcela parcela = gestorCultivos.getParcelaEnPx(posMundo.x, posMundo.y);

        if (parcela == null) {
            System.out.println("-> No hay un cultivo cerca del clic.");
            return;
        }

        Cultivo cosechado = parcela.cosechar();

        if (cosechado != null) {
            TipoCultivo tipo = cosechado.getTipo();
            Item itemCosecha = tipo.crearItemCosecha();

            boolean guardado = inventario.agregarItem(itemCosecha, 1);

            if (guardado) {
                System.out.println("-> Cosechado y guardado en inventario: " + tipo.getNombre());
            } else {
                System.out.println("-> Cosechado pero el inventario está lleno.");
            }
        } else {
            System.out.println("-> El cultivo todavía no está listo.");
        }
    }
    
    
    
    public void procesarDormir() {

        if (!estaCercaDeLaCasa()) {
            return;
        }

        if (!controlSueno.puedeDormir(reloj)) {
            System.out.println("Todavía es temprano para dormir.");
            return;
        }

        controlSueno.iniciarDormir(reloj, 6);
    }
    
    public boolean estaCercaDeLaCasa() {
        if (zonaDormir == null) {
            return false;
        }
        return zonaDormir.contains(
                jugador.getCentroX(),
                jugador.getPiesY()
        );
    }
    
    public void intentarEntrarSalirTienda() {
        if (!enTienda) {
            // Si estamos afuera, SOLO entramos si estamos cerca de la zona
            if (estaCercaDeLaTienda()) {
                entrarSalirTienda();
            } else {
                System.out.println("-> Tenés que estar cerca de la tienda para entrar.");
            }
        } else {
            // Si ya estamos adentro, podemos salir directamente
            entrarSalirTienda();
        }
    }
    
    public void entrarSalirTienda() {

    	if (!enTienda) {
    		
            System.out.println("ENTRANDO A LA TIENDA");
            posicionAnteriorX = jugador.getX();
            posicionAnteriorY = jugador.getY();

            controlMapa = mapaTienda;
            jugador.setPosicion(10, 120);
            enTienda = true;
            
         // Cambiá el zoom para que la tienda se vea completa y alejada (ej: 1.0f o el que prefieras)
//            camara.zoom = 0.8f;

        } else {
            System.out.println("SALIENDO A LA GRANJA");
            controlMapa = mapaGranja;
            jugador.setPosicion(posicionAnteriorX, posicionAnteriorY);
            enTienda = false;
        }

        // Actualizar referencias del mapa actual
        zonaDormir = controlMapa.getZonaDormir();
        zonaTienda = controlMapa.getZonaTienda();
        zonaComprar = controlMapa.getZonaComprar();
    }
    
    public void procesarAbrirTienda() {
        if (!estaCercaDeComprar()) {
            System.out.println("-> Tenés que pararte frente al mostrador para comprar.");
            return;
        }
        
        controlTienda.abrir();
    }

    
    public boolean estaCercaDeLaTienda() {
        if (zonaTienda == null) {
            return false;
        }
        return zonaTienda.contains(
                jugador.getCentroX(),
                jugador.getPiesY()
        );
    }
    public boolean estaCercaDeComprar() {
        if (zonaComprar == null) {
            return false;
        }
        return zonaComprar.contains(
                jugador.getCentroX(),
                jugador.getPiesY()
        );
    }
    
    @Override
    public void render(float delta) {
    	ScreenUtils.clear(0, 0, 0, 1);
        controlClima.actualizar(delta);
        controlSueno.actualizar(delta, reloj, gestorCultivos);

        if (!controlSueno.estaDurmiendo()) {
            int minutosPasados = reloj.actualizar(delta);
            if (minutosPasados > 0) {
                gestorCultivos.pasarTiempo(minutosPasados);
            }
        }
        
        jugador.actualizar(delta, controlMapa.getColisionesMapa(), controlMapa.getAnchoMapaPixels(), controlMapa.getAltoMapaPixels());
        actualizarCamara();
     // =========================
     // DIBUJAR MAPA Y PERSONAJE
     // =========================

     controlMapa.setView(camara);
     controlMapa.renderAbajo();

     batch.setProjectionMatrix(camara.combined);
     batch.begin();

     // Los cultivos solamente se dibujan en la granja
     if (!enTienda) {
         renderizarCultivos();
     }

     // Dibujar al jugador en ambos mapas
     jugador.renderizar(batch);

     batch.end();

     controlMapa.renderArriba();

        // Efectos visuales de clima
        controlClima.renderizarOscurecimiento(shapeRenderer, camara, vp.getWorldWidth(), vp.getWorldHeight());
        controlClima.renderizarEfectoVisual(batch, hudCamara, V_WIDTH, V_HEIGHT);

        // =========================
        // DIBUJAR HUD (Reloj, Lluvia, Monedas)
        // =========================
        batch.setProjectionMatrix(hudCamara.combined);
        batch.begin();
        
        font.getData().setScale(1.2f);
        String textoHud = "Dia: " + reloj.getDias() + " | Hora: " + reloj.getHoraFormateada() + " | Monedas: $" + billetera.getMonedas() + " | [L] Lluvia: " + (controlClima.isLluviaActiva() ? "ON" : "OFF");
        font.draw(batch, textoHud, 20, V_HEIGHT - 20);
        font.getData().setScale(1f);
        
        gestorInventario.renderizar(batch, font, V_WIDTH, V_HEIGHT);
        
        batch.end();

        // =========================
        // RENDERIZAR STAGE DE UI (Tienda / Menús)
        // =========================
        stageUi.act(delta);
        stageUi.draw();

        // Transición de sueño
        controlSueno.renderizarEfectoVisual(shapeRenderer, batch, camara, hudCamara, vp, font, reloj, V_WIDTH, V_HEIGHT);
    }

    private void actualizarCamara() {

    	float medioAnchoCamara = (camara.viewportWidth * camara.zoom) / 2f;
        float medioAltoCamara = (camara.viewportHeight * camara.zoom) / 2f;

        float anchoMapa = controlMapa.getAnchoMapaPixels();
        float altoMapa = controlMapa.getAltoMapaPixels();
        
        float viewportAnchoEfectivo = camara.viewportWidth * camara.zoom;
        float viewportAltoEfectivo = camara.viewportHeight * camara.zoom;

        float camX, camY;

        // Si el mapa es más chico que la pantalla (como la tienda), lo centramos de forma segura
        if (anchoMapa < viewportAnchoEfectivo) {
            camX = anchoMapa / 2f;
        } else {
            camX = MathUtils.clamp(
                    jugador.getX(),
                    medioAnchoCamara,
                    anchoMapa - medioAnchoCamara
            );
        }

        if (altoMapa < viewportAltoEfectivo) {
            camY = altoMapa / 2f;
        } else {
            camY = MathUtils.clamp(
                    jugador.getY(),
                    medioAltoCamara,
                    altoMapa - medioAltoCamara
            );
        }

        camara.position.set(camX, camY, 0);
        camara.update();
    }

    private void renderizarCultivos() {

        for (int fila = 0; fila < controlMapa.getMapHeightTiles(); fila++) {

            for (int col = 0; col < controlMapa.getMapWidthTiles(); col++) {

                Parcela parcela = gestorCultivos.getParcela(col, fila);

                if (parcela == null || !parcela.tieneCultivo()) {
                    continue;
                }

                Cultivo cultivo = parcela.getCultivoActual();
                TipoCultivo tipo = cultivo.getTipo();

                float xPx = col * controlMapa.getTileWidth();
                float yPx = fila * controlMapa.getTileHeight();

                float ancho = controlMapa.getTileWidth();
                float alto = controlMapa.getTileHeight();

                // Si el cultivo está podrido
                if (cultivo.isPodrido()) {

                    Texture texturaPodrida = texturasPodridas.get(tipo);

                    batch.draw(texturaPodrida, xPx, yPx, ancho, alto);

                } else {

                    // Si todavía está creciendo o está listo para cosechar
                    int etapa = cultivo.getEtapaActual();

                    TextureRegion[] etapas = etapasCultivos.get(tipo);

                    if (etapas == null || etapa < 0 || etapa >= etapas.length) {
                        continue;
                    }

                    batch.draw(etapas[etapa], xPx, yPx, ancho, alto);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        vp.update(width, height);
        stageUi.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        jugador.liberarRecursos();
        mapaGranja.dispose();
        mapaTienda.dispose();
        controlClima.dispose();
        gestorInventario.dispose();
        stageUi.dispose();
        skinUi.dispose();

        for (Texture textura : texturasCultivos.values()) {
            textura.dispose();
        }
    }
}
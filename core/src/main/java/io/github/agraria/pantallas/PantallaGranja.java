package io.github.agraria.pantallas;

import io.github.agraria.cultivos.GestorCultivos;
import io.github.agraria.sonidos.*;
import io.github.agraria.cultivos.Cultivo;
import io.github.agraria.cultivos.Parcela;
import io.github.agraria.cultivos.TipoCultivo;
import io.github.agraria.elementos.Item;
import io.github.agraria.eventos.*;
import io.github.agraria.inventario.GestorInventario;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.economia.Billetera;
import io.github.agraria.tienda.VentanaTienda;
import io.github.agraria.ui.GestorInteraccion;
import io.github.agraria.ui.InteraccionPrompt;
import io.github.agraria.control.ControlCultivos;
import io.github.agraria.control.ControlEntrada;
import io.github.agraria.control.ControlJugador;
import io.github.agraria.control.ControlTienda;
import io.github.agraria.personajes.Personaje;
import io.github.tiempo.Reloj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.graphics.GL20;

public class PantallaGranja extends ScreenAdapter {

    private SpriteBatch batch;
    private Personaje jugador;

    private GestorMapa controlMapa;
    
    private GestorMapa mapaGranja;
    private PantallaTienda pantallaTienda;
    private GestorMapa mapaCasa;


    private boolean enCasa = false;

   
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
    private ControlCultivos controlCultivos;
    
    // --- HUD Y TEXTO ---
    private BitmapFont font;
    private OrthographicCamera hudCamara;
    private ShapeRenderer shapeRenderer;
    
    private Rectangle zonaDormir;
    private Rectangle zonaTienda;
    private Rectangle zonaComprar;
    private Rectangle zonaSalir;
    
    private Inventario inventario;
    private GestorInventario gestorInventario;

    private Billetera billetera;
    private Stage stageUi; // Stage independiente para manejar la UI de la tienda
    private Skin skinUi;  
    private ControlJugador controlJugador;// Skin por defecto o la que uses para la UI
    private ControlTienda controlTienda;
    
    private GestorAudio gestorAudio;

    private boolean isSaliendoAlMenu = false;


    private GestorInteraccion gestorInteraccion;
    
    private ControlEntrada controlEntrada;

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
            pantallaTienda = new PantallaTienda();
            mapaCasa = new GestorMapa("pantallas/casa/interiorCasa.tmx");
       
        
        controlMapa = mapaGranja;
        
        zonaDormir = controlMapa.getZonaDormir();
        zonaTienda = controlMapa.getZonaTienda();
        zonaComprar = controlMapa.getZonaComprar();
        zonaSalir = controlMapa.getZonaSalir();
        
        this.inventario = new Inventario(10);
        this.gestorInventario = new GestorInventario(inventario);

        // Inicializamos la billetera con 100 monedas de base
        this.billetera = new Billetera(100);
        
     // Stage con la cámara fija del HUD para que no se mueva con el zoom del mapa
        stageUi = new Stage(new FitViewport(V_WIDTH, V_HEIGHT, hudCamara));
        skinUi = new Skin(Gdx.files.internal("uiskin.json"));

        // Inicializar el gestor de mensajes flotantes
        this.gestorInteraccion = new GestorInteraccion(stageUi, skinUi);

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
        
        gestorAudio = new GestorAudio();
        gestorAudio.reproducirMusicaFondo();
        
        
        controlCultivos = new ControlCultivos(
        	    gestorCultivos,
        	    inventario,
        	    gestorAudio
        	);
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

    	controlJugador = new ControlJugador(jugador, inventario);
    	controlEntrada = new ControlEntrada();
    	
    	// Acciones del jugador en la granja
    	controlJugador.setAccionDormir(() -> procesarDormir());

    	controlJugador.setAccionTienda(() -> procesarAbrirTienda());

    	controlJugador.setAccionCultivar(
    	    (screenX, screenY) -> procesarCultivar(screenX, screenY)
    	);

    	controlJugador.setAccionCosechar(
    	    (screenX, screenY) -> procesarCosechar(screenX, screenY)
    	);

        controlTienda = new ControlTienda(
            stageUi,
            billetera,
            inventario,
            skinUi,
            controlJugador
        );
        
        // Combinamos la UI y el control del jugador para que ambos reciban eventos
        com.badlogic.gdx.InputMultiplexer multiplexer = new com.badlogic.gdx.InputMultiplexer();
        multiplexer.addProcessor(stageUi);   
        multiplexer.addProcessor(controlEntrada);
        multiplexer.addProcessor(controlJugador);
        
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void procesarCultivar(int screenX, int screenY) {

        Vector3 posicionMundo = camara.unproject(
            new Vector3(screenX, screenY, 0)
        );

        controlCultivos.procesarCultivar(
            posicionMundo.x,
            posicionMundo.y
        );
    }
    
    public void procesarCosechar(int screenX, int screenY) {

        Vector3 posicionMundo = camara.unproject(
            new Vector3(screenX, screenY, 0)
        );

        controlCultivos.procesarCosechar(
            posicionMundo.x,
            posicionMundo.y
        );
    }
    
    
    
    public void procesarDormir() {
    	

        System.out.println("SE EJECUTÓ procesarDormir()");
    	
        if (!estaCercaDeLaCasa()) {
            return;
        }

        if (!controlSueno.puedeDormir(reloj)) {
            System.out.println("Todavía es temprano para dormir.");
            return;
        }
        System.out.println("iniciando cicloSueño()");
        controlSueno.iniciarDormir(reloj, 6);
        controlClima.sortearClimaDiario();
        
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
        if (!pantallaTienda.estaDentro()) {
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

        if (!pantallaTienda.estaDentro()) {

            System.out.println("ENTRANDO A LA TIENDA");

            pantallaTienda.entrar(jugador);

            controlMapa = pantallaTienda.getMapa();

        } else {

            System.out.println("SALIENDO A LA GRANJA");

            pantallaTienda.salir(jugador);

            controlMapa = mapaGranja;
        }

        zonaDormir = controlMapa.getZonaDormir();
        zonaTienda = controlMapa.getZonaTienda();
        zonaComprar = controlMapa.getZonaComprar();
        zonaSalir = controlMapa.getZonaSalir();
    }
    
    public void procesarAbrirTienda() {

    	
    	if (estaCercaDeComprar()) {
            if (controlTienda.estaAbierta()) {
                controlTienda.cerrar();
            } else {
                controlTienda.abrir();
            }
        } else {
            System.out.println("-> Tenés que estar cerca del mostrador para abrir o cerrar la tienda.");
        }
    	
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
    
    public boolean estaCercaDeSalir() {
    	
        if (zonaSalir == null) {
            return false;
        }
        return zonaSalir.contains(
                jugador.getCentroX(),
                jugador.getPiesY()
        );
    }
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (controlJugador != null) {
            controlJugador.actualizarMovimiento();
        }

     // --- DETECTAR TECLA ESC A TRAVÉS DE ControlEntrada ---
        if (controlEntrada != null && controlEntrada.isSolicitioSalirMenu() && !isSaliendoAlMenu) {

            isSaliendoAlMenu = true;
        	System.out.println("se abrio el menu");
        }
        
        if (isSaliendoAlMenu) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaPausa());
                return;
        }

        controlClima.actualizar(delta);
        controlSueno.actualizar(delta, reloj, gestorCultivos);

     // Forzar sueño automáticamente a las 02:00 AM
        if (reloj.getHoras() == 2 && !controlSueno.estaDurmiendo()) {
            System.out.println("¡Te dieron las 2:00 AM! Te desmayás y te vas a dormir hasta más tarde.");
            controlSueno.iniciarDormir(reloj, 10); 
        }
        
        if (!controlSueno.estaDurmiendo()) {
            int minutosPasados = reloj.actualizar(delta);
            if (minutosPasados > 0) {
                gestorCultivos.pasarTiempo(minutosPasados);
            }
        }
        
        jugador.actualizar(delta, controlMapa.getColisionesMapa(), controlMapa.getAnchoMapaPixels(), controlMapa.getAltoMapaPixels());
        
        if (enCasa) {
            if (estaCercaDeSalir()) {
                entrarSalirTienda();
            }
        }
        
        if (!pantallaTienda.estaDentro()) {
        	
            if (estaCercaDeLaTienda()) {
            	
                entrarSalirTienda();
                
            }
            
        } else {
        	
            if (pantallaTienda.estaCercaDeSalir(jugador)) {
            	
                entrarSalirTienda();
                
            }
        }
        
        actualizarCamara();

        // =========================
        // DIBUJAR MAPA Y PERSONAJE
        // =========================
        controlMapa.setView(camara);
        controlMapa.renderAbajo();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        if (!pantallaTienda.estaDentro() && !enCasa) {
            gestorCultivos.renderizar(
                batch,
                etapasCultivos,
                texturasPodridas
            );
        }

        jugador.renderizar(batch);
        batch.end();

        controlMapa.renderArriba();
     
        controlClima.actualizar(delta);

        // Efectos visuales de clima
        boolean mostrarClima = !pantallaTienda.estaDentro() && !enCasa;

        controlClima.renderizarOscurecimiento(
            shapeRenderer, camara,
            vp.getWorldWidth(), vp.getWorldHeight(),
            mostrarClima
        );

        controlClima.renderizarEfectoVisual(
            batch, hudCamara,
            V_WIDTH, V_HEIGHT,
            mostrarClima
        );
        

        // ==========================================
        // LÓGICA DE MENSAJES FLOTANTES (PROMPTS MUNDIALES)
        // ==========================================
        if (estaCercaDeLaCasa() && !controlSueno.estaDurmiendo()) {
            float casaX = zonaDormir.x + (zonaDormir.width / 2f);
            float casaY = zonaDormir.y + zonaDormir.height;
            gestorInteraccion.mostrarPromptMundial("Z", "Dormir", casaX, casaY, camara);
        } 
        else if (pantallaTienda.estaDentro() && estaCercaDeComprar()) {
            float comprarX = zonaComprar.x + (zonaComprar.width / 2f);
            float comprarY = zonaComprar.y + zonaComprar.height;
            gestorInteraccion.mostrarPromptMundial("T", "Tienda", comprarX, comprarY, camara);
        } 
        else {
            gestorInteraccion.ocultarPrompt();
        }

        // =========================
        // DIBUJAR HUD (Reloj, Lluvia, Monedas)
        // =========================
        batch.setProjectionMatrix(hudCamara.combined);
        batch.begin();
        
        font.getData().setScale(1.2f);
        String textoHud = "Dia: " + reloj.getDias() + " | Hora: " + reloj.getHoraFormateada() + " | Monedas: $" + billetera.getMonedas();
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


        // ==========================================
        // CÁLCULO DINÁMICO DE LUZ / ATARDECER / NOCHE
        // ==========================================
        float horaActual = reloj.getHoras() + (reloj.getMinutos() / 60.0f);
        float alphaOscuridad = 0f;
        
        if (horaActual >= 18.0f && horaActual < 20.0f) {
            alphaOscuridad = (horaActual - 18.0f) / 2.0f * 0.5f;
        } 
        else if (horaActual >= 20.0f || horaActual < 5.0f) {
            alphaOscuridad = 0.6f;
        } 
        else if (horaActual >= 5.0f && horaActual < 7.0f) {
            alphaOscuridad = 0.6f - ((horaActual - 5.0f) / 2.0f * 0.6f);
        }

        if (alphaOscuridad > 0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            
            shapeRenderer.setProjectionMatrix(hudCamara.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.05f, 0.05f, 0.2f, alphaOscuridad);
            shapeRenderer.rect(0, 0, V_WIDTH, V_HEIGHT);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void actualizarCamara() {

    	float medioAnchoCamara = (camara.viewportWidth * camara.zoom) / 2f;
        float medioAltoCamara = (camara.viewportHeight * camara.zoom) / 2f;

        float anchoMapa = controlMapa.getAnchoMapaPixels();
        float altoMapa = controlMapa.getAltoMapaPixels();
        
        float viewportAnchoEfectivo = camara.viewportWidth * camara.zoom;
        float viewportAltoEfectivo = camara.viewportHeight * camara.zoom;

        float camX, camY;

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
        pantallaTienda.dispose();
        controlClima.dispose();
        gestorInventario.dispose();
        stageUi.dispose();
        skinUi.dispose();
        if (gestorAudio != null) {
            gestorAudio.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (gestorInteraccion != null) {
            gestorInteraccion.dispose();
        }

        for (Texture textura : texturasCultivos.values()) {
            textura.dispose();
        }
        for (Texture textura : texturasPodridas.values()) {
            textura.dispose();
        }
    }
}
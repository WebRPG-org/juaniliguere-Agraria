package io.github.agraria.eventos;

import io.github.agraria.cultivos.GestorCultivos;
import io.github.tiempo.Reloj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ControlSueno {

    public enum EstadoTransicion { IDLE, FADE_OUT, FAST_FORWARD, FADE_IN }
    private EstadoTransicion estadoTransicion = EstadoTransicion.IDLE;

    private float fadeAlpha = 0f;
    private int targetDiaDormir = 0;
    
    private static final int HORA_DORMIR = 20;
    private static final int HORA_LIMITE = 2;
    private static final int HORA_DESPERTAR = 6;
    private static final int HORA_DESPERTAR_TARDE = 10;
    private int horaDespertar;

    
    public boolean puedeDormir(Reloj reloj) {
        int hora = reloj.getHoras();

        return hora >= HORA_DORMIR || hora <= HORA_LIMITE;
    }
    
    
    
    public void iniciarDormir(Reloj reloj, int horaDespertar) {

        if (estadoTransicion == EstadoTransicion.IDLE) {
            targetDiaDormir = reloj.getDias() + 1;
            this.horaDespertar = horaDespertar;

            estadoTransicion = EstadoTransicion.FADE_OUT;

            System.out.println("-> Acostándose a dormir...");
        }
    	
    }

    public void actualizar(float delta, Reloj reloj, GestorCultivos gestorCultivos) {
        if (estadoTransicion == EstadoTransicion.IDLE) return;

        if (estadoTransicion == EstadoTransicion.FADE_OUT) {
            fadeAlpha += delta * 2f; 
            if (fadeAlpha >= 1f) {
                fadeAlpha = 1f;
                estadoTransicion = EstadoTransicion.FAST_FORWARD; 
            }
        } 
        
        
        else if (estadoTransicion == EstadoTransicion.FAST_FORWARD) {

            int minutosAvanzadosEsteFrame = 0;

            while (minutosAvanzadosEsteFrame < 25) {

                if (reloj.getDias() == targetDiaDormir &&
                    reloj.getHoras() >= horaDespertar) {
                    break;
                }

                reloj.avanzarUnMinuto();
                minutosAvanzadosEsteFrame++;
            }

            if (minutosAvanzadosEsteFrame > 0) {
                gestorCultivos.pasarTiempo(minutosAvanzadosEsteFrame);
            }

            if (reloj.getDias() == targetDiaDormir &&
                reloj.getHoras() >= horaDespertar) {

                gestorCultivos.procesarPudrir();
                System.out.println("-> ¡Buenos días!");
                estadoTransicion = EstadoTransicion.FADE_IN;
            }
        }
        
        
        else if (estadoTransicion == EstadoTransicion.FADE_IN) {
            fadeAlpha -= delta * 2f; 
            if (fadeAlpha <= 0f) {
                fadeAlpha = 0f;
                estadoTransicion = EstadoTransicion.IDLE; 
            }
        }
    }

    public void renderizarEfectoVisual(ShapeRenderer shapeRenderer, SpriteBatch batch, OrthographicCamera camara, OrthographicCamera hudCamara, Viewport vp, BitmapFont font, Reloj reloj, int vWidth, int vHeight) {
        if (estadoTransicion == EstadoTransicion.IDLE) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, fadeAlpha);
        shapeRenderer.rect(
            camara.position.x - (vp.getWorldWidth() * camara.zoom),
            camara.position.y - (vp.getWorldHeight() * camara.zoom),
            vp.getWorldWidth() * camara.zoom * 2f,
            vp.getWorldHeight() * camara.zoom * 2f
        );
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.setProjectionMatrix(hudCamara.combined);
        batch.begin();
        font.getData().setScale(2f);
        
        GlyphLayout layout = new GlyphLayout();
        float centroX = vWidth / 2f;
        float centroY = vHeight / 2f;

        if (estadoTransicion == EstadoTransicion.FAST_FORWARD || estadoTransicion == EstadoTransicion.FADE_OUT) {
            layout.setText(font, "Durmiendo...");
            font.draw(batch, layout, centroX - layout.width / 2f, centroY + 60);

            layout.setText(font, "Hora: " + reloj.getHoraFormateada());
            font.draw(batch, layout, centroX - layout.width / 2f, centroY + 10);

            layout.setText(font, "Dia: " + reloj.getDias());
            font.draw(batch, layout, centroX - layout.width / 2f, centroY - 40);

        } else if (estadoTransicion == EstadoTransicion.FADE_IN) {
            layout.setText(font, "¡Buenos días!");
            font.draw(batch, layout, centroX - layout.width / 2f, centroY + 30);

            layout.setText(font, "Dia " + reloj.getDias() + " - Mediodia");
            font.draw(batch, layout, centroX - layout.width / 2f, centroY - 20);
        }
        
        font.getData().setScale(1f);
        batch.end();
    }

    public void verificarDesmayoPorHorario(Reloj reloj) {
        if (reloj.getHoras() == 2 && !estaDurmiendo()) {
            System.out.println("¡Te dieron las 2:00 AM! Te desmayás y te vas a dormir hasta más tarde.");
            iniciarDormir(reloj, HORA_DESPERTAR_TARDE); 
        }
    }
    
    public boolean estaDurmiendo() {
        return estadoTransicion != EstadoTransicion.IDLE;
    }
}
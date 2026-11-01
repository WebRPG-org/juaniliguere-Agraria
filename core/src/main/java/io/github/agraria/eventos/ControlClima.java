package io.github.agraria.eventos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import io.github.agraria.cultivos.GestorCultivos;

public class ControlClima {

    private Texture[] texturasLluvia;
    private Animation<TextureRegion> animacionLluvia;
    private float tiempoLluvia = 0f;
    
    private boolean lluviaActiva = false;      
    private float intensidadLluvia = 0f;       // Va de 0.0 a 1.0 (Fade In/Out)
	private GestorCultivos gestorCultivos;

    public ControlClima(GestorCultivos gestorCultivos) {
    	   this.gestorCultivos = gestorCultivos;
        cargarAnimacionLluvia();
    }

    private void cargarAnimacionLluvia() {
        int totalFrames = 12; // De 00 a 11
        texturasLluvia = new Texture[totalFrames];
        TextureRegion[] regionFrames = new TextureRegion[totalFrames];

        for (int i = 0; i < totalFrames; i++) {
            String numeroStr = (i < 10) ? "0" + i : String.valueOf(i);
            Texture frameTex = new Texture("efectos/lluvia/frame_" + numeroStr + "_delay-0.02s.png");
            frameTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            
            texturasLluvia[i] = frameTex;
            regionFrames[i] = new TextureRegion(frameTex);
        }

        animacionLluvia = new Animation<>(0.02f, regionFrames);
        animacionLluvia.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void sortearClimaDiario() {
        // MathUtils.random() da un valor entre 0.0 y 1.0
        float probabilidadLluvia = 0.20f; 
        lluviaActiva = MathUtils.random() < probabilidadLluvia;

        if (lluviaActiva) {
            System.out.println("-> ¡Hoy va a llover!");
            if (gestorCultivos != null) {
                gestorCultivos.hacerLlover();
            }
        } else {
            System.out.println("-> Hoy el día estará despejado.");
        }
    }
    
    public void actualizar(float delta) {
        

        float velocidadTransicion = 0.5f; 
        if (lluviaActiva) {
            intensidadLluvia = Math.min(1f, intensidadLluvia + delta * velocidadTransicion);
        } else {
            intensidadLluvia = Math.max(0f, intensidadLluvia - delta * velocidadTransicion);
        }

        if (intensidadLluvia > 0f) {
            tiempoLluvia += delta;
        }
    }

    public void renderizarOscurecimiento(ShapeRenderer shapeRenderer, OrthographicCamera camara, float viewportWidth, float viewportHeight) {
        if (intensidadLluvia <= 0f) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.08f, 0.12f, 0.25f, intensidadLluvia * 0.4f);
        shapeRenderer.rect(
            camara.position.x - (viewportWidth * camara.zoom),
            camara.position.y - (viewportHeight * camara.zoom),
            viewportWidth * camara.zoom * 2f,
            viewportHeight * camara.zoom * 2f
        );
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderizarEfectoVisual(SpriteBatch batch, OrthographicCamera hudCamara, int vWidth, int vHeight) {
        if (intensidadLluvia <= 0f) return;

        batch.setProjectionMatrix(hudCamara.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, intensidadLluvia * 0.85f); 
        
        TextureRegion frameActual = animacionLluvia.getKeyFrame(tiempoLluvia, true);
        batch.draw(frameActual, 0, 0, vWidth, vHeight);
        
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
    }

    public boolean isLluviaActiva() {
        return lluviaActiva;
    }

    public void dispose() {
        if (texturasLluvia == null) return;
        for (Texture tex : texturasLluvia) {
            if (tex != null) {
                tex.dispose();
            }
        }
    }
}
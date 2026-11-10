package io.github.agraria.ui;

import io.github.agraria.inventario.GestorInventario;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.economia.Billetera;
import io.github.tiempo.Reloj;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GestorHud {

    private BitmapFont font;
    private OrthographicCamera hudCamara;
    private Inventario inventario;
    private GestorInventario gestorInventario;

    public GestorHud(Inventario inventario, int vWidth, int vHeight, BitmapFont font) {
        this.font = font;

        this.hudCamara = new OrthographicCamera();
        this.hudCamara.setToOrtho(false, vWidth, vHeight);

        this.inventario = inventario;
        this.gestorInventario = new GestorInventario(inventario);
    }

    public void renderizar(SpriteBatch batch, Reloj reloj, Billetera billetera, int vWidth, int vHeight) {
        batch.setProjectionMatrix(hudCamara.combined);
        batch.begin();
        
        font.getData().setScale(1.2f);
        String textoHud = "Dia: " + reloj.getDias() + " | Hora: " + reloj.getHoraFormateada() + " | Monedas: $" + billetera.getMonedas();
        font.draw(batch, textoHud, 20, vHeight - 20);
        font.getData().setScale(1f);
        
        gestorInventario.renderizar(batch, font, vWidth, vHeight);
        batch.end();
    }

    public OrthographicCamera getHudCamara() {
        return hudCamara;
    }

    public void dispose() {
 
        if (gestorInventario != null) gestorInventario.dispose();
    }
}
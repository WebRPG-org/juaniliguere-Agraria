package io.github.agraria.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class GestorInteraccion {

    private Stage stage;
    private Table contenedorPrompt;
    private Label lblTecla;
    private Label lblAccion;
    private boolean visible = false;
    
    private Vector3 posicionTemporal = new Vector3();

    public GestorInteraccion(Stage stage, Skin skin) {
        this.stage = stage;

        contenedorPrompt = new Table(skin);
        
        // --- SIN FONDO (CAJITA GRIS ELIMINADA) ---
        contenedorPrompt.setBackground((Drawable) null);

        contenedorPrompt.pad(2, 6, 2, 6);

        lblTecla = new Label("", skin);
        lblAccion = new Label("", skin);

        // Texto grande y destacado
        lblTecla.setFontScale(1.4f);
        lblAccion.setFontScale(1.4f);

        lblTecla.setAlignment(Align.center);
        lblAccion.setAlignment(Align.left);

        contenedorPrompt.add(lblTecla).padRight(6).minWidth(24);
        contenedorPrompt.add(lblAccion);

        contenedorPrompt.setVisible(false);
        stage.addActor(contenedorPrompt);
    }

    /**
     * Muestra el prompt flotando en una posición específica del mundo (al lado de la casa/tienda).
     */
    public void mostrarPromptMundial(String tecla, String accion, float mundoX, float mundoY, OrthographicCamera camara) {
        lblTecla.setText("[" + tecla + "]");
        lblAccion.setText(accion);
        contenedorPrompt.pack();

        posicionTemporal.set(mundoX, mundoY, 0);
        camara.project(posicionTemporal);

        float pantallaX = posicionTemporal.x * (stage.getViewport().getWorldWidth() / (float) stage.getViewport().getScreenWidth());
        float pantallaY = posicionTemporal.y * (stage.getViewport().getWorldHeight() / (float) stage.getViewport().getScreenHeight());

        contenedorPrompt.setPosition(
            pantallaX - (contenedorPrompt.getWidth() / 2f), 
            pantallaY + 20
        );

        if (!visible) {
            contenedorPrompt.setVisible(true);
            visible = true;
        }
    }

    /**
     * Método de respaldo (por si en algún lado se llama sin coordenadas).
     */
    public void mostrarPrompt(String tecla, String accion) {
        lblTecla.setText("[" + tecla + "]");
        lblAccion.setText(accion);
        contenedorPrompt.pack();
        
        contenedorPrompt.setPosition(
            (stage.getViewport().getWorldWidth() - contenedorPrompt.getWidth()) / 2f, 
            100
        );
        
        if (!visible) {
            contenedorPrompt.setVisible(true);
            visible = true;
        }
    }

    public void ocultarPrompt() {
        if (visible) {
            contenedorPrompt.setVisible(false);
            visible = false;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        if (contenedorPrompt != null) {
            contenedorPrompt.remove();
        }
    }
}
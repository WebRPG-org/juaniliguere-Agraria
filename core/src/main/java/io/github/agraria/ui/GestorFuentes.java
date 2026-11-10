
package io.github.agraria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GestorFuentes {

    private BitmapFont fuentePrincipal;

    public void cargar() {

        FreeTypeFontGenerator generador = new FreeTypeFontGenerator(
            Gdx.files.internal("fuentes/VT323-Regular.ttf")
        );

        FreeTypeFontGenerator.FreeTypeFontParameter parametros =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parametros.size = 22;
        parametros.minFilter = Texture.TextureFilter.Nearest;
        parametros.magFilter = Texture.TextureFilter.Nearest;

        fuentePrincipal = generador.generateFont(parametros);

        generador.dispose();
    }

    public BitmapFont getFuentePrincipal() {
        return fuentePrincipal;
    }

    public void dispose() {
        if (fuentePrincipal != null) {
            fuentePrincipal.dispose();
            fuentePrincipal = null;
        }
    }
}

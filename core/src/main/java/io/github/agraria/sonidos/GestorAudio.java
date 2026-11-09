package io.github.agraria.sonidos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestorAudio {
    private static Music musicaFondo;
    private Sound sonidoPlantar;
    private Sound apretarBoton;

    public GestorAudio() {
        // Si ya hay una música sonando, nos aseguramos de frenarla antes de reasignarla
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.dispose();
            musicaFondo = null;
        }

        try {
            // Cargar música de fondo (loop)
            musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("sonidos/temaFondo.wav"));
            musicaFondo.setLooping(true);
            musicaFondo.setVolume(0.4f);
            musicaFondo.play(); // <-- ¡La reproducimos automáticamente al crear el objeto!
        } catch (Exception e) {
            Gdx.app.log("GestorAudio", "Error al cargar la musica de fondo: " + e.getMessage());
        }

        // Cargar efectos cortos (WAV)
        sonidoPlantar = Gdx.audio.newSound(Gdx.files.internal("sonidos/plantar.wav"));
        apretarBoton = Gdx.audio.newSound(Gdx.files.internal("sonidos/apretarBoton.wav"));
    }

    public void reproducirMusicaFondo() {
        if (musicaFondo != null && !musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    public static void apagarMusicaGlobal() {
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.dispose();
            musicaFondo = null;
        }
    }

    public void reproducirPlantar() {
        if (sonidoPlantar != null) {
            sonidoPlantar.play(1.0f);
        }
    }
    
    public void reproducirApretarBoton() {
        if (apretarBoton != null) {
            apretarBoton.play(1.0f);
        }
    }

    public void dispose() {
        if (sonidoPlantar != null) {
            sonidoPlantar.dispose();
        }
        if (apretarBoton != null) {
            apretarBoton.dispose();
        }
    }
}
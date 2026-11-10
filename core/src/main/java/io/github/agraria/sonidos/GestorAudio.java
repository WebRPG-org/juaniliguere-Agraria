
package io.github.agraria.sonidos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestorAudio {

    private static Music musicaFondo;

    private Sound sonidoPlantar;
    private Sound apretarBoton;

    public GestorAudio() {

        // Recuperamos la carga de los efectos de sonido
        sonidoPlantar = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/plantar.wav")
        );

        apretarBoton = Gdx.audio.newSound(
            Gdx.files.internal("sonidos/apretarBoton.wav")
        );

        // Recuperamos el comportamiento original:
        // la música de fondo comienza al crear el gestor.
        reproducirMusicaFondo();
    }

    public void reproducirMusicaFondo() {
        if (musicaFondo == null) {
            musicaFondo = Gdx.audio.newMusic(
                Gdx.files.internal("sonidos/musicaFondo.wav")
            );

            musicaFondo.setLooping(true);
            musicaFondo.setVolume(0.4f);
        }

        if (!musicaFondo.isPlaying()) {
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

    // =========================
    // EFECTOS DE SONIDO
    // =========================

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

    // =========================
    // LIBERAR RECURSOS
    // =========================

    public void dispose() {

        if (sonidoPlantar != null) {
            sonidoPlantar.dispose();
            sonidoPlantar = null;
        }

        if (apretarBoton != null) {
            apretarBoton.dispose();
            apretarBoton = null;
        }
    }

    public static void disposeMusicas() {

        if (musicaFondo != null) {
            musicaFondo.dispose();
            musicaFondo = null;
        }
    }
}

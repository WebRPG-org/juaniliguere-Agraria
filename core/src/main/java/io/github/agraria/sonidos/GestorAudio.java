package io.github.agraria.sonidos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestorAudio {
	private static Music musicaFondo;
	private Sound sonidoPlantar;
	private Sound sonidoCosechar;
	private Sound apretarBoton;
	private Sound sonidoLluvia;

	public GestorAudio() {
        // Si ya existe música de fondo sonando o cargada previamente, la detenemos/liberamos antes de crear otra
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.dispose();
            musicaFondo = null;
        }

        // Cargar música de fondo (loop)
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("sonidos/temaFondo.wav"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.4f);

        // Cargar efectos cortos (WAV)
        sonidoPlantar = Gdx.audio.newSound(Gdx.files.internal("sonidos/plantar.wav"));
        apretarBoton = Gdx.audio.newSound(Gdx.files.internal("sonidos/apretarBoton.wav"));
    }

    public void dispose() {
        // IMPORTANTE: En el dispose del gestor de audio NO hacemos el dispose de musicaFondo 
        // si queremos que persista entre pantallas, O bien la detenemos si salimos al menú.
        // Lo dejamos limpio así:
        
        if (sonidoPlantar != null) {
            sonidoPlantar.dispose();
        }
        
        if (apretarBoton != null) {
            apretarBoton.dispose();
        }
    }
    
}

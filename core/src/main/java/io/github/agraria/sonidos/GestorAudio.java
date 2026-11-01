package io.github.agraria.sonidos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GestorAudio {
	private Music musicaFondo;
	private Sound sonidoPlantar;
	private Sound sonidoCosechar;
	private Sound apretarBoton;
	private Sound sonidoLluvia;

    public GestorAudio() {
        // Cargar música de fondo (loop)
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("sonidos/temaFondo.wav"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.4f);

        // Cargar efectos cortos (WAV)
        sonidoPlantar = Gdx.audio.newSound(Gdx.files.internal("sonidos/plantar.wav"));
        apretarBoton = Gdx.audio.newSound(Gdx.files.internal("sonidos/apretarBoton.wav"));
//        sonidoLluvia = Gdx.audio.newSound(Gdx.files.internal("sonidos/lluvia.wav"));
      //  sonidoCosechar = Gdx.audio.newSound(Gdx.files.internal("sonidos/cosechar.wav"));
        // sonidomoneda = Gdx.audio.newSound(Gdx.files.internal("sonidos/moneda.wav"));
    }

    public void reproducirMusicaFondo() {
        if (musicaFondo != null && !musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    public void detenerMusicaFondo() {
        if (musicaFondo != null && musicaFondo.isPlaying()) {
            musicaFondo.stop();
        }
    }

    public void reproducirPlantar() {
    	
    	if (sonidoPlantar != null) {
            sonidoPlantar.play(1.0f);
        }
    	
    }
    
//    public void reproducirCosechar() {
//    	
//    	if (sonidoCosechar != null) {
//    		sonidoCosechar.play(1.0f);
//        }
//    	
//    }
    
    public void reproducirApretarBoton() {
    	
    	if (apretarBoton != null) {
    		apretarBoton.play(1.0f);
        }
    	
    }
//    
//    public void reproducirsonidoLluvia() {
//    	
//    	if (sonidoLluvia != null) {
//    		sonidoLluvia.play(1.0f);
//        }
//    	
//    }
    
    public void dispose() {
        if (musicaFondo != null) {
            musicaFondo.dispose();
        }
        
        if (sonidoPlantar != null) {
        	sonidoPlantar.dispose();
        }
        
        if(apretarBoton != null) {
        	apretarBoton.dispose();
        }
        
//        if(sonidoCosechar != null) {
//        	sonidoCosechar.dispose();
//        }
    }
}

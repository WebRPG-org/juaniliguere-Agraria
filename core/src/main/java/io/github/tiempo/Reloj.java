package io.github.tiempo;

public class Reloj {

    private static final float SEGS_POR_MIN_JUEGO = 0.1f;
	
    private int minutos;
    private int horas;
    private int dias;

    private float acumuladorSegundos;

    public Reloj() {
        this.minutos = 0;
        this.horas = 6;
        this.dias = 1;

        this.acumuladorSegundos = 0;
    }

    public int actualizar(float delta) {
    	
        acumuladorSegundos += delta;
        int minutosPasados = 0;

        while (acumuladorSegundos >= SEGS_POR_MIN_JUEGO) {
            acumuladorSegundos -= SEGS_POR_MIN_JUEGO;

            avanzarUnMinuto();
            minutosPasados++;
        }
        
      //  System.out.println("MinPasados: " + minutosPasados);
        return minutosPasados;
        
    }

    public void avanzarUnMinuto() {
    	
        minutos++;
        if (minutos >= 60) {
        	
            minutos = 0;
            horas++;

            if (horas >= 24) {
            	
                horas = 0;
                dias++;
                
            }
        }
    }

    public int dormir(int horaDespertar) {
    	
    	int minutosPasados = 0;
    	int diaSiguiente = dias + 1;
    	
    	while(dias < diaSiguiente || horas < horaDespertar) {
    		
    		avanzarUnMinuto();
    		minutosPasados++;
    		
    	}
    	
    	acumuladorSegundos = 0;
    	
    	return minutosPasados;
    	
    }
    
    public int getMinutos() {
        return minutos;
    }

    public int getHoras() {
        return horas;
    }

    public int getDias() {
        return dias;
    }
    
    public String getHoraFormateada() {
        String horaStr = horas < 10 ? "0" + horas : String.valueOf(horas);
        String minStr = minutos < 10 ? "0" + minutos : String.valueOf(minutos);
        return horaStr + ":" + minStr;
    }
}
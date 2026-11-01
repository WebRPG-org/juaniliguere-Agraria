package io.github.agraria.cultivos;


public class Parcela{

    // Posición de esta parcela dentro de la grilla
//    private final int x;
//    private final int y;

    private final boolean esPlantable;

    private Cultivo cultivoActual;

    public Parcela(int x, int y, boolean esPlantable) {
//        this.x = x;
//        this.y = y;
        this.esPlantable = esPlantable;

        this.cultivoActual = null;
        
    }

    public boolean puedePlantar() {
        return esPlantable && cultivoActual == null;
    } 

    public boolean plantar(TipoCultivo tipo) {
    	
        if (!puedePlantar()) {
            return false;
        }
        
        cultivoActual = new Cultivo(tipo);
        return true;
        
    }
    
    // <-- NUEVO: Recibe la lluvia y afecta al cultivo
    public void recibirLluvia() {
        if (cultivoActual != null) {
        	System.out.println("4. ¡Parcela con cultivo encontró lluvia! Pudriendo...");
            cultivoActual.pudrir();
        }
    }

    public Cultivo cosechar() {

        if (cultivoActual != null && (cultivoActual.isCosechable() || cultivoActual.isPodrido())) {

            // Guardamos el cultivo antes de vaciar la parcela
            Cultivo cosechado = cultivoActual;

            cultivoActual = null;

            return cosechado;
        }

        return null;
        
    }

 // <-- NUEVO: Método para limpiar la parcela si está podrida (al hacerle clic)
    public boolean limpiarSiEstaPodrido() {
        if (cultivoActual != null && cultivoActual.isPodrido()) {
            cultivoActual = null; // Borra el cultivo podrido y libera la tierra
            return true;
        }
        return false;
    }
    
    public void pasarTiempo(int minutosPasados) {

        if (cultivoActual != null) {
            cultivoActual.hacerCrecer(minutosPasados);
        }
    }

    public boolean isEsPlantable() {
        return esPlantable;
    }

    public boolean tieneCultivo() {
        return cultivoActual != null;
    }

    public Cultivo getCultivoActual() {
        return cultivoActual;
    }
    

    
    
    
    
}
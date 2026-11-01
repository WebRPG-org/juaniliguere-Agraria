package io.github.agraria.cultivos;

import io.github.agraria.elementos.Item;
import io.github.agraria.inventario.Inventario;

public class Cultivo {

    private final TipoCultivo tipo;

    private int etapaActual;
    private int minutosAcumulados;
    private boolean podrido;

    public Cultivo(TipoCultivo tipo) {
        this.tipo = tipo;
        this.etapaActual = 0;
        this.minutosAcumulados = 0;
        this.podrido = false;
    }

    public void hacerCrecer(int minutosPasados) {

        if (minutosPasados <= 0 || isCosechable() || podrido) {
            return;
        }

        minutosAcumulados += minutosPasados;

        while (minutosAcumulados >= tipo.getMinutosPorEtapa()
                && !isCosechable()) {

            minutosAcumulados -= tipo.getMinutosPorEtapa();
            etapaActual++;
        }
    }
    
    public boolean isCosechable() {
        // Cosechable cuando llega a la última etapa (ej: si tiene 4 etapas, las etapas van de 0 a 3)
    	return !podrido && (etapaActual >= tipo.getCantidadEtapas() - 1);
    }

    /**
     * Intenta cosechar el cultivo agregando el item al inventario.
     * @param inventario El inventario del jugador
     * @return true si se cosechó con éxito (había espacio)
     */
    
    public void pudrir() {
        this.podrido = true;
    }

    public boolean isPodrido() {
        return podrido;
    }
    
    public boolean cosechar(Inventario inventario) {
        if (!isCosechable()) {
            return false;
        }

        // 1. Generamos el item de la cosecha
        Item producto = tipo.crearItemCosecha();
        int cantidadObtenida = 1; // Podés hacer un Math.random() acá si querés dar entre 1 y 3 frutos

        // 2. Intentamos guardar en el inventario
        boolean guardado = inventario.agregarItem(producto, cantidadObtenida);

        return guardado; // Si devuelve true, la lógica del mapa/tierra borra esta planta
    }

    public TipoCultivo getTipo() {
        return tipo;
    }

    public int getEtapaActual() {
        return etapaActual;
    }

}
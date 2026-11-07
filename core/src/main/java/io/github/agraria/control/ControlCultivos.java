
package io.github.agraria.control;

import com.badlogic.gdx.math.Vector2;

import io.github.agraria.cultivos.Cultivo;
import io.github.agraria.cultivos.GestorCultivos;
import io.github.agraria.cultivos.Parcela;
import io.github.agraria.cultivos.TipoCultivo;
import io.github.agraria.elementos.Item;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.sonidos.GestorAudio;

public class ControlCultivos {

    private GestorCultivos gestorCultivos;
    private Inventario inventario;
    private GestorAudio gestorAudio;

    public ControlCultivos(
            GestorCultivos gestorCultivos,
            Inventario inventario,
            GestorAudio gestorAudio) {

        this.gestorCultivos = gestorCultivos;
        this.inventario = inventario;
        this.gestorAudio = gestorAudio;
    }

    // PLANTACIÓN
    public void procesarCultivar(float mundoX, float mundoY) {

        Parcela parcela = gestorCultivos.getParcelaEnPx(mundoX, mundoY);

        if (parcela == null) {
            return;
        }

        if (parcela.tieneCultivo() || !parcela.isEsPlantable()) {
            return;
        }

        TipoCultivo cultivoSeleccionado = inventario.getCultivoSeleccionado();

        if (cultivoSeleccionado == null) {
            System.out.println("-> No tenés seleccionada ninguna semilla válida en la mano.");
            return;
        }

        boolean exito = gestorCultivos.plantarEn(
                mundoX, mundoY, cultivoSeleccionado);

        if (exito) {
            gestorAudio.reproducirPlantar();
            inventario.consumirSeleccionado(1);

            System.out.println("-> ¡Semilla plantada con éxito!");
        }
    }

    // COSECHA
    public void procesarCosechar(float mundoX, float mundoY) {

        Parcela parcela = gestorCultivos.getParcelaEnPx(mundoX, mundoY);

        if (parcela == null) {
            System.out.println("-> No hay un cultivo cerca del clic.");
            return;
        }

        Cultivo cosechado = parcela.cosechar();

        if (cosechado == null) {
            System.out.println("-> El cultivo todavía no está listo.");
            return;
        }

        TipoCultivo tipo = cosechado.getTipo();

        Item itemCosecha;

        if (cosechado.isPodrido()) {
            itemCosecha = tipo.crearItemPodrido();
        } else {
            itemCosecha = tipo.crearItemCosecha();
        }

        boolean guardado = inventario.agregarItem(itemCosecha, 1);

        if (guardado) {
            System.out.println(
                    "-> Cosechado y guardado en inventario: " + tipo.getNombre());
        } else {
            System.out.println("-> Cosechado pero el inventario está lleno.");
        }
    }
}

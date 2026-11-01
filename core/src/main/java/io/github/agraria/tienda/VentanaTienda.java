package io.github.agraria.tienda;

import io.github.agraria.economia.Billetera;
import io.github.agraria.inventario.Inventario;
import io.github.agraria.inventario.CasillaInventario;
import io.github.agraria.elementos.Item;
import io.github.agraria.cultivos.TipoCultivo;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class VentanaTienda {

    private Window ventana;
    private Billetera billetera;
    private Inventario inventario;
    private Skin skin;

    public VentanaTienda(Stage stage, Billetera billetera, Inventario inventario, Skin skin) {
        this.billetera = billetera;
        this.inventario = inventario;
        this.skin = skin;

        ventana = new Window("Tienda de la Granja", skin);
        ventana.setSize(600, 500);
        ventana.setPosition(175, 100);
        ventana.setMovable(true);
        
        actualizarContenidoVentana();
        stage.addActor(ventana);
        
        
    }

    public void actualizarContenidoVentana() {
        ventana.clearChildren();

        ventana.add(new Label("Tus Monedas: $" + billetera.getMonedas(), skin)).padBottom(10).colspan(2).row();
        
        // --- SECCIÓN: COMPRAR SEMILLAS ---
        ventana.add(new Label("--- COMPRAR SEMILLAS ---", skin)).padBottom(5).colspan(2).row();
        
        for (TipoCultivo tipo : TipoCultivo.values()) {
            int costo = tipo.getPrecioCompra();
            Label lblSemilla = new Label("Semilla de " + tipo.getNombre() + " - $" + costo, skin);
            TextButton btnComprar = new TextButton("Comprar", skin);

            btnComprar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (billetera.gastar(costo)) {
                        // Creamos el ítem de semilla
                        Item itemSemilla = tipo.crearItemSemilla();
                        boolean agregado = inventario.agregarItem(itemSemilla, 1);
                        
                        if (!agregado) {
                            // Si el inventario está lleno, devolvemos el dinero
                            billetera.ganar(costo);
                            System.out.println("¡Inventario lleno, no se pudo comprar!");
                        } else {
                            System.out.println("¡Compraste semilla de " + tipo.getNombre() + "!");
                        }
                        actualizarContenidoVentana();
                    } else {
                        System.out.println("¡No te alcanza el dinero!");
                    }
                }
            });

            ventana.add(lblSemilla).padRight(20).left();
            ventana.add(btnComprar).padBottom(5).right();
            ventana.row();
        }

        // --- SECCIÓN: VENDER COSECHAS ---
        ventana.add(new Label("--- VENDER COSECHAS ---", skin)).padTop(10).padBottom(5).colspan(2).row();
        
        boolean hayItems = false;
        for (int i = 0; i < 10; i++) {
            CasillaInventario casilla = inventario.getCasilla(i);
            if (casilla != null && !casilla.estaVacia() && !casilla.getItem().esSemilla()) {
                hayItems = true;
                Item item = casilla.getItem();
                int cantidad = casilla.getCantidad();
                
                // Obtenemos el precio de venta directamente desde el TipoCultivo asociado al ítem
                int precioVentaUnitario = (item.getTipoCultivo() != null) ? item.getTipoCultivo().getPrecioVenta() : 25;
                int precioTotal = precioVentaUnitario * cantidad;

                Label lblItem = new Label(item.getNombre() + " (x" + cantidad + ") - $" + precioTotal, skin);
                TextButton btnVender = new TextButton("Vender", skin);

                btnVender.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        casilla.limpiar();
                        billetera.ganar(precioTotal);
                        actualizarContenidoVentana();
                    }
                });

                ventana.add(lblItem).padRight(20).left();
                ventana.add(btnVender).padBottom(5).right();
                ventana.row();
            }
        }

        if (!hayItems) {
            ventana.add(new Label("No tenes cultivos para vender.", skin)).padBottom(10).colspan(2).row();
        }

        // Botón Cerrar
        TextButton btnCerrar = new TextButton("Cerrar", skin);
        btnCerrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ventana.remove();
            }
        });
        ventana.add(btnCerrar).padTop(15).colspan(2);
    }
    
    public boolean estaAbierta() {
        return ventana.hasParent();
    }
}
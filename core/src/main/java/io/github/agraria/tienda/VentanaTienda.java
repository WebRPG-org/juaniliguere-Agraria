package io.github.agraria.tienda;

import io.github.agraria.economia.Billetera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class VentanaTienda {

    private Window ventana;
    private Billetera billetera;
    private Inventario inventario;
    private Skin skin;
    
    private Texture texturaPanel;
    private Texture texturaTitulo;
    private Texture texturaSeparador;

    private Texture texturaPestanaComprar;
    private Texture texturaPestanaVender;
    private Texture texturaPestanaComprarInactiva;
    private Texture texturaPestanaVenderInactiva;

    private Texture texturaFilaProducto;
    private Texture texturaPrecio;

    private Texture texturaIconoComprar;
    private Texture texturaIconoVender;
    
    private void cargarRecursos() {

        texturaPanel = new Texture(
            Gdx.files.internal("tienda/panel_48x48.png")
        );

        texturaTitulo = new Texture(
            Gdx.files.internal("tienda/title_bar_80x16.png")
        );

        texturaSeparador = new Texture(
            Gdx.files.internal("tienda/divider_64x7.png")
        );

        texturaPestanaComprar = new Texture(
            Gdx.files.internal("tienda/tab_buy_active_36x16.png")
        );

        texturaPestanaVender = new Texture(
            Gdx.files.internal("tienda/tab_sell_active_36x16.png")
        );
        texturaPestanaComprarInactiva = new Texture(
        	Gdx.files.internal("tienda/tab_buy_inactive_36x16.png")
        	);

        texturaPestanaVenderInactiva = new Texture(
        	Gdx.files.internal("tienda/tab_sell_inactive_36x16.png")
        );
        texturaFilaProducto = new Texture(
            Gdx.files.internal("tienda/item_row_normal_120x20.png")
        );

        texturaPrecio = new Texture(
            Gdx.files.internal("tienda/price_tag_coin.png")
        );

        texturaIconoComprar = new Texture(
            Gdx.files.internal("tienda/icon_buy_bag.png")
        );

        texturaIconoVender = new Texture(
            Gdx.files.internal("tienda/icon_sell_hand.png")
        );

        // Evitamos el suavizado de los píxeles
        texturaPanel.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaTitulo.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaSeparador.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaPestanaComprar.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaPestanaVender.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaPestanaComprarInactiva.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaPestanaVenderInactiva.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaFilaProducto.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaPrecio.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaIconoComprar.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        texturaIconoVender.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }

    private ImageButton crearBotonImagen(
            Texture texturaActiva,
            Texture texturaInactiva) {

        ImageButton.ImageButtonStyle estilo =
            new ImageButton.ImageButtonStyle();

        estilo.imageUp = new TextureRegionDrawable(
            new TextureRegion(texturaActiva)
        );

        estilo.imageDown = new TextureRegionDrawable(
            new TextureRegion(texturaInactiva)
        );

        estilo.imageDisabled = new TextureRegionDrawable(
            new TextureRegion(texturaInactiva)
        );

        return new ImageButton(estilo);
    }
    
    public VentanaTienda(Stage stage, Billetera billetera, Inventario inventario, Skin skin) {

		this.billetera = billetera;
		this.inventario = inventario;
		this.skin = skin;
		
		// Primero cargamos las texturas
		cargarRecursos();
		
		// Creamos el fondo personalizado
		NinePatch panel = new NinePatch(
		texturaPanel,
		6, 6, 6, 6
		);
		
		// Copiamos el estilo original
		Window.WindowStyle estiloVentana =
		new Window.WindowStyle(skin.get(Window.WindowStyle.class));
		
		// Aplicamos el fondo personalizado
		estiloVentana.background = new NinePatchDrawable(panel);
		
		// Creamos la ventana
		ventana = new Window("", estiloVentana);
		
		ventana.setSize(600, 500);
		ventana.setPosition(
			    (stage.getWidth() - ventana.getWidth()) / 2,
			    (stage.getHeight() - ventana.getHeight()) / 2
			);
		ventana.setMovable(true);
		
		actualizarContenidoVentana();
		
		stage.addActor(ventana);
		
		
	}
    

    public void actualizarContenidoVentana() {

        ventana.clearChildren();
        ventana.defaults().pad(5);

        // FONDO DE CADA FILA
        TextureRegionDrawable fondoFila = new TextureRegionDrawable(
            new TextureRegion(texturaFilaProducto)
        );

     // =========================
     // ENCABEZADO
     // =========================

     Table encabezado = new Table();

     encabezado.setBackground(
         new TextureRegionDrawable(
             new TextureRegion(texturaTitulo)
         )
     );

     Label titulo = new Label("TIENDA DE LA GRANJA", skin);

     encabezado.add(titulo)
         .center()
         .pad(8);

     ventana.add(encabezado)
         .colspan(2)
         .width(570)
         .height(40)
         .padBottom(5)
         .row();

     // =========================
     // MONEDAS
     // =========================

     Table saldo = new Table();

     Image iconoMoneda = new Image(texturaPrecio);

     Label lblMonedas = new Label(
         " " + billetera.getMonedas(),
         skin
     );

     saldo.add(iconoMoneda).size(24);
     saldo.add(lblMonedas).padLeft(5);

     ventana.add(saldo)
         .colspan(2)
         .center()
         .padBottom(10)
         .row();

        // =========================
        // COLUMNA COMPRAR
        // =========================

        Table listaComprar = new Table();

        for (TipoCultivo tipo : TipoCultivo.values()) {

            int costo = tipo.getPrecioCompra();

            Table fila = new Table();
            fila.setBackground(fondoFila);

            Label lblSemilla = new Label(
                "Semilla de " + tipo.getNombre() + " - $" + costo,
                skin
            );
            lblSemilla.setFontScale(0.75f);
            ImageButton btnComprar = crearBotonImagen(
            	    texturaPestanaComprar,
            	    texturaPestanaComprarInactiva
            	);

            	btnComprar.setDisabled(billetera.getMonedas() < costo);

            btnComprar.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if (billetera.gastar(costo)) {

                        Item itemSemilla = tipo.crearItemSemilla();

                        boolean agregado = inventario.agregarItem(itemSemilla, 1);

                        if (!agregado) {

                            billetera.ganar(costo);

                            System.out.println("¡Inventario lleno!");

                        } else {

                            System.out.println(
                                "¡Compraste semilla de " + tipo.getNombre() + "!"
                            );
                        }

                        actualizarContenidoVentana();

                    } else {

                        System.out.println("¡No te alcanza el dinero!");
                    }
                }
            });

            fila.add(lblSemilla)
            .left()
            .expandX()
            .padLeft(8)
            .padRight(4);

            fila.add(btnComprar)
            .right()
            .padRight(8)
            .size(90, 40);

            listaComprar.add(fila)
                .width(260)
                .pad(5)
                .row();
        }

        // =========================
        // COLUMNA VENDER
        // =========================

        Table listaVender = new Table();

        boolean hayItems = false;

        for (int i = 0; i < 10; i++) {

            CasillaInventario casilla = inventario.getCasilla(i);

            if (casilla != null &&
                !casilla.estaVacia() &&
                !casilla.getItem().esSemilla()) {

                hayItems = true;

                Item item = casilla.getItem();
                int cantidad = casilla.getCantidad();

                int precioVentaUnitario;

                if (item.getId().equals("cultivo_podrido") ||
                    item.getTipoCultivo() == null) {

                    precioVentaUnitario = 5;

                } else {

                    precioVentaUnitario = item.getTipoCultivo().getPrecioVenta();
                }

                int precioTotal = precioVentaUnitario * cantidad;

                Table fila = new Table();
                fila.setBackground(fondoFila);

                Label lblItem = new Label(
                    item.getNombre() + " (x" + cantidad + ") - $" + precioTotal,
                    skin
                );
                lblItem.setFontScale(0.75f);
                ImageButton btnVender = crearBotonImagen(
                	    texturaPestanaVender,
                	    texturaPestanaVenderInactiva
                	);

                btnVender.addListener(new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        casilla.limpiar();

                        billetera.ganar(precioTotal);

                        actualizarContenidoVentana();
                    }
                });

                fila.add(lblItem)
                .left()
                .expandX()
                .padLeft(8)
                .padRight(4);

                fila.add(btnVender)
                .right()
                .padRight(8)
                .size(90, 40);

                listaVender.add(fila)
                    .width(260)
                    .pad(5)
                    .row();
            }
        }

        if (!hayItems) {

            listaVender.add(
                new Label("No tenés cultivos para vender.", skin)
            ).pad(10);
        }

     // =========================
     // ENCABEZADOS DE SECCIONES
     // =========================

     Table columnaComprar = new Table();

     columnaComprar.add(
         new Label("COMPRAR SEMILLAS", skin)
     )
     .center()
     .padBottom(5)
     .row();

     columnaComprar.add(new Image(texturaSeparador))
         .width(250)
         .height(7)
         .padBottom(5)
         .row();


     Table columnaVender = new Table();

     columnaVender.add(
         new Label("VENDER COSECHAS", skin)
     )
     .center()
     .padBottom(5)
     .row();

     columnaVender.add(new Image(texturaSeparador))
         .width(250)
         .height(7)
         .padBottom(5)
         .row();

        // =========================
        // SCROLLS
        // =========================

  // Estilo de desplazamiento sin fondo gris
     ScrollPane.ScrollPaneStyle estiloScroll =
         new ScrollPane.ScrollPaneStyle(
             skin.get(ScrollPane.ScrollPaneStyle.class)
         );

     estiloScroll.background = null;

     // Scroll de compra
     ScrollPane scrollComprar =
         new ScrollPane(listaComprar, estiloScroll);

     scrollComprar.setScrollingDisabled(true, false);
     scrollComprar.setFadeScrollBars(false);

     // Scroll de venta
     ScrollPane scrollVender =
         new ScrollPane(listaVender, estiloScroll);

     scrollVender.setScrollingDisabled(true, false);
     scrollVender.setFadeScrollBars(false);

        columnaComprar.add(scrollComprar)
            .width(270)
            .height(370)
            .expand()
            .fill();

        columnaVender.add(scrollVender)
            .width(270)
            .height(370)
            .expand()
            .fill();

        // =========================
        // AGREGAMOS LAS COLUMNAS
        // =========================

        ventana.add(columnaComprar)
            .width(285)
            .height(410)
            .top();

        ventana.add(columnaVender)
            .width(285)
            .height(410)
            .top();
    }
    
    public void cerrarVentana() {
        if (ventana != null) {
            ventana.remove();
        }
    }
    
    public boolean estaAbierta() {
        return ventana.hasParent();
    }
    
    public void dispose() {

        texturaPanel.dispose();
        texturaTitulo.dispose();
        texturaSeparador.dispose();

        texturaPestanaComprar.dispose();
        texturaPestanaVender.dispose();
        
        texturaPestanaComprarInactiva.dispose();
        texturaPestanaVenderInactiva.dispose();

        texturaFilaProducto.dispose();
        texturaPrecio.dispose();

        texturaIconoComprar.dispose();
        texturaIconoVender.dispose();
    }
}
package io.github.agraria.cultivos;

import io.github.agraria.inventario.*;
import io.github.agraria.elementos.Item;

public enum TipoCultivo {

	    // Nombre, Precio Compra, Precio Venta, Minutos por Etapa (4 etapas), XP, Ruta de Textura
	    ZANAHORIA("Zanahoria", 10, 12, 60, 15, "cultivos/zanahoriaEtapas.png", "cultivos/zanahoriaItem.png","cultivos/cultivoPodrido.png",  4),
	    PAPA("Papa", 25, 30, 60, 15, "cultivos/papaEtapas.png", "cultivos/papaItem.png", "cultivos/cultivoPodrido.png",5),
	    ARROZ("Arroz", 10, 12, 60, 15, "cultivos/arrozEtapas.png", "cultivos/arrozItem.png", "cultivos/cultivoPodrido.png",5),
	    BROCOLI("Brocoli", 25, 30, 60, 15, "cultivos/brocoliEtapas.png", "cultivos/brocoliItem.png", "cultivos/cultivoPodrido.png", 5),
	    COLRIZADO("Col rizado", 10, 12, 60, 15, "cultivos/colrizadoEtapas.png", "cultivos/colrizadoItem.png", "cultivos/cultivoPodrido.png", 5),
	    CALABAZA("Calabaza", 25, 30, 60, 15, "cultivos/calabazaEtapas.png", "cultivos/calabazaItem.png", "cultivos/cultivoPodrido.png",6),
	    MELON("Melon", 10, 12, 60, 15, "cultivos/melonEtapas.png", "cultivos/melonItem.png","cultivos/cultivoPodrido.png", 6);

	    private final String nombre;
	    private final int precioCompra;
	    private final int precioVenta;
	    private final int minutosPorEtapa;
	    private final int xpRecompensa;
	    private final String rutaTextura;
		private String rutaTexturaItem;
		private final String rutaTexturaPodrido;
	    private final int cantidadEtapas;

	    TipoCultivo(String nombre, int precioCompra, int precioVenta, int minutosPorEtapa, int xpRecompensa, String rutaTextura, String rutaTexturaItem, String rutaTexturaPodrido,int cantidadEtapas) {
	    	
	        this.nombre = nombre;
	        this.precioCompra = precioCompra;
	        this.precioVenta = precioVenta;
	        this.minutosPorEtapa = minutosPorEtapa;
	        this.xpRecompensa = xpRecompensa;
	        this.rutaTextura = rutaTextura;
	        this.rutaTexturaItem = rutaTexturaItem;
	        this.rutaTexturaPodrido = rutaTexturaPodrido;
	        this.cantidadEtapas = cantidadEtapas;
	        
	    }
	    public String getRutaTexturaItem() { return rutaTexturaItem; }
	    
	    public Item crearItemSemilla() {
	        String id = "semilla_" + this.name().toLowerCase();
	        // Acá le pasás la ruta de la imagen del sobre de semillas
	        return new Item(id, "Semilla de " + nombre, TipoItem.SEMILLA, this, "cultivos/sobre_" + this.name().toLowerCase() + ".png", 64);
	    }

	    public Item crearItemCosecha() {
	        String id = "cosecha_" + this.name().toLowerCase();
	        // Acá le pasás la ruta de la imagen del cultivo cosechado (la que ya usás)
	        return new Item(id, nombre, TipoItem.COSECHA, this, rutaTexturaItem, 64);
	    }
	    
	    public Item crearItemPodrido() {
	        String id = "cultivo_podrido";
	        // Le pasamos el nombre, precio de venta (5), tipo, la textura podrida y el stack máximo (64)
	        return new Item(id, "Cultivo Podrido", TipoItem.COSECHA, null, "cultivos/cultivoPodrido.png", 64);
	    }
	    

	    public String getNombre() { return nombre; }
	    public int getPrecioCompra() { return precioCompra; }
	    public int getPrecioVenta() { return precioVenta; }
	    public int getMinutosPorEtapa() { return minutosPorEtapa; }
	    public int getXpRecompensa() { return xpRecompensa; }
	    public String getRutaTextura() { return rutaTextura; }
	    public String getRutaTexturaPodrido() { return rutaTexturaPodrido; }
	    public int getCantidadEtapas() {return cantidadEtapas; }
	    
	}

package models;

public final class Producto {
	// Características de todo producto
	private String manufacturer;
	private String modelo;
	private int tiempoEntrega;
	private int precio;

	/*
	 * Constructor de la clase producto Pasaremos por parámetro sus
	 * características (marca,modelo,tiempo entrega y precio)
	 */
	public Producto(String manufacturer, String modelo, int tiempoEntrega, int precio) {
		this.manufacturer = manufacturer;
		this.modelo = modelo;
		this.tiempoEntrega = tiempoEntrega;
		this.precio = precio;
	}

	/* Devuelve la marca del producto */
	public String getManufacturer() {
		return manufacturer;
	}

	/* Devuelve el modelo del producto */
	public String getModelo() {
		return modelo;
	}

	/* Devuelve el tiempo de entrega al cliente del producto */
	public int getTiempoEntrega() {
		return tiempoEntrega;
	}

	/* Devuelve el precio del producto */
	public int getPrecio() {
		return precio;
	}
}

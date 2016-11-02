package utils;

import models.Producto;

public class TestValues {
	
	/*** Containers **/
	public final static String serverContainer = "Main-Container";
	public final static String tienda1Container = "Container-1";
	public final static String tienda2Container = "Container-2";
	public final static String tienda3Container = "Container-3";
	public static String clientContainer ="";

	/** Características tiendas */
	public final static int ntiendas = 3;
	public final static int nProductos = 4;
	
	/*** tienda 1 **/
	final static Producto tienda1Producto1 = new Producto ("Cisco", "123A", 4, 13);
	final static Producto tienda1Producto2 = new Producto ("Apple", "A321", 3, 120);
	final static Producto tienda1Producto3 = new Producto ("Sony", "B456", 4, 120);
	final static Producto tienda1Producto4 = new Producto ("Samsung", "456B", 5, 120);
	public static Producto[] tienda1Database = { tienda1Producto1, tienda1Producto2, 
											  tienda1Producto3, tienda1Producto4 };
	
	/** tienda 2 **/
	final static Producto tienda2Producto1 = new Producto ("Cisco", "123A", 2, 13);
	final static Producto tienda2Producto2 = new Producto ("Apple", "A321", 1, 140);
	final static Producto tienda2Producto3 = new Producto ("Sony", "B456", 4, 120);
	final static Producto tienda2Producto4 = new Producto ("Fujitsu", "456B", 5, 120);
	public static Producto[] tienda2Database = { tienda2Producto1, tienda2Producto2, 
		  									  tienda2Producto3, tienda2Producto4 };
	
	/** tienda 3 **/
	final static Producto tienda3Producto1 = new Producto ("Cisco", "123A", 6, 11);
	final static Producto tienda3Producto2 = new Producto ("Apple", "A321", 5, 140);
	final static Producto tienda3Producto3 = new Producto ("Sony", "B456", 8, 100);
	final static Producto tienda3Producto4 = new Producto ("Phillips", "456B", 5, 120);
	public static Producto[] tienda3Database = { tienda3Producto1, tienda3Producto2, 
		  									  tienda3Producto3, tienda3Producto4 };
}

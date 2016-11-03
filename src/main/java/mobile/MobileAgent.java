package mobile;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.mobility.MobilityOntology;
import utils.TestValues;

/**
 * Clase que inicializa el MobileAgent que "viajara" desde un container a otro.
 */
public final class MobileAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	public static final String AGENT_NAME = "mobile";
	
	private AID controller;
	private String manufacturer;
	private String modelo;
	private int precio = Integer.MAX_VALUE;
	private int tiempoEntrega = Integer.MAX_VALUE; // tiempoEntrega found
	private int maxTiempoEntrega;// delivery time required by the client
	private int nTiendas = 1; // number of shop to visit
	private String tienda;

	/**
	 * Ejecuta el protoco JADE, creando las caracteristicas y el comportamiento
	 * de un agente. Tambien se obtendran los argumentos, por lo que tendremos
	 * las caracteristicas del producto
	 */
	@Override
	protected void setup() {
		// Recupera los argumentos pasados durante la creacion del agente
	   	Object[] args = getArguments();
		controller = (AID) args[0];
		manufacturer = (String) args[1];
		modelo = (String) args[2];
		maxTiempoEntrega = (Integer) args[3];
		init();
	}
	
	/*
	 * Metodo que ejecuta algunas caracteristicas del agente
	 */
	private void init() {
		// Registra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
	}

	@Override
	protected void afterMove() {
		super.afterMove();
		System.out.println("He llegado al contenedor : " + this.here().getName());
		//addBehaviour(new SendMessage());
		init();
	}

	@Override
	protected void beforeMove() {
		super.beforeMove();
		System.out.println("Moviéndome al destino : " + this.here().getName());
	}
	
}

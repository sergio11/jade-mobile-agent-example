package main;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.mobility.MobilityOntology;

/**
 * Clase que implementa el servidor que controlara el movimiento del Mobile
 * Agent, con lo que visitara todas las tiendas para reunir la información
 * requerida.
 */
public final class ASAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Este metodo establece el lenguaje y la ontologia del agente
	*/
	private void init() {
		// Registra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
	}
}

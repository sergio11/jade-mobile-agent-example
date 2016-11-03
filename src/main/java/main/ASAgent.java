package main;

import behaviour.MoveAgentBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mobile.MobileAgent;
import utils.TestValues;

/**
 * Clase que implementa el servidor que controlara el movimiento del Mobile
 * Agent, con lo que visitara todas las tiendas para reunir la información
 * requerida.
 */
public final class ASAgent extends Agent {

	public static final String AGENT_NAME = "AS_AGENT";
	private static final long serialVersionUID = 1L;
	private int nMobileSaltos = 0;
	

	@Override
	protected void setup() {
		// Registra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
		this.addBehaviour(new ReceiveMessage());
	}

	
	/**
	 * Esta clase implementa el comportamiento necesario para recibir los differentes
	 * mensajes del Mobile Agent, y lo envia al destino siguiente (a una nueva tienda
	 * o de nuevo al cliente)
	 */
	class ReceiveMessage extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = myAgent.receive(mt);
			if (msg!=null) {
				nMobileSaltos++;
				
				if (nMobileSaltos <= TestValues.shops.length){
					System.out.println("Moviendo agente a tienda nª " + nMobileSaltos);
					myAgent.addBehaviour(new MoveAgentBehaviour(MobileAgent.AGENT_NAME, "Container-"+nMobileSaltos));
				}else{
					System.out.println("Moviendo agente al cliente");
					nMobileSaltos = 0;
					myAgent.addBehaviour(new MoveAgentBehaviour(MobileAgent.AGENT_NAME, "Container-4"));
				}
					
			}
			else {
				block();
			}
		}
	}
}

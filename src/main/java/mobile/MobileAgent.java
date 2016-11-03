package mobile;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import utils.TestValues;

/**
 * Clase que inicializa el MobileAgent que "viajara" desde un container a otro.
 */
public final class MobileAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	public static final String AGENT_NAME = "mobile";
	
	private AID controller;
	private Location destino;
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
		this.addBehaviour(new ReceiveCommands());
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
		addBehaviour(new SendMessage());
		init();
	}

	@Override
	protected void beforeMove() {
		super.beforeMove();
		System.out.println("Moviéndome al destino : " + this.here().getName());
	}
	
	
	/*
	 * Recibe todos los comandos del agente controlador
	 */
	class ReceiveCommands extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		/*
		 * Metodo que ejecuta la accion del agente movil para la espera de
		 * cualquier mensaje. Dependiendo el tipo del mensaje recibido, se
		 * implemnetaran las diferentes acciones
		 */
		public void action() {

			ACLMessage msg = receive();

			if (msg != null) {
				// Si se recibe una peticion (Request), se movera el agente a otro
				// container
				if (msg.getPerformative() == ACLMessage.REQUEST) {
					try {
						ContentElement content = getContentManager().extractContent(msg);
						Concept concept = ((Action) content).getAction();

						if (concept instanceof MoveAction) {
							MoveAction ma = (MoveAction) concept;
							Location l = ma.getMobileAgentDescription().getDestination();
							if (l != null)
								doMove(destino = l);
						}

						else if (concept instanceof KillAgent) {
							doDelete();
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				/*
				 * Si se recibe un informe (Inform), se mostrara en la pantalla el
				 * mensaje que hemos recibido y se extraeran las diferentes
				 * caracteristicas que se han recibido
				 */
				else if (msg.getPerformative() == ACLMessage.INFORM) {
					String request = msg.getContent();
					try {
						String aux[] = request.split("\t");
						int auxInt[] = new int[2];
						auxInt[0] = Integer.parseInt(aux[0]);
						auxInt[1] = Integer.parseInt(aux[1]);
						// Si son las mejores, se elegiran como las variables
						// locales
						if (auxInt[1] <= maxTiempoEntrega) {
							if (precio > auxInt[0] || ((precio == auxInt[0]) && (tiempoEntrega > auxInt[1]))) {
								precio = auxInt[0];
								tiempoEntrega = auxInt[1];
								tienda = aux[2];
							}
						}
					} catch (Exception e) {
					}
				} else {
					System.out.println("Mensaje inesperado del agente");
				}
			} else {
				block();
			}
			
		}
	}
	
	
	/*
	 * Subclase SendMessage que proveera de las diferentes posibilidades de
	 * mensajes que se pueden encontrar. Creara el contenido para los mismos,
	 * dependiendo si es un mensaje para el Application Server (AS), para el
	 * cliente o para la tienda.
	 */

	class SendMessage extends Behaviour {

		private static final long serialVersionUID = 1L;
		boolean finished = false;
		boolean kill = false;

		public void action() {
			ACLMessage msg;
			Location currentLocation = myAgent.here();
			// Mensaje para el Application Server (AS)
			if (currentLocation.getName().equals(TestValues.serverContainer)) {
				msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID("AS", AID.ISLOCALNAME));
				msg.setContent("");
			}
			// Mensaje para el cliente
			else if (currentLocation.getName().equals(TestValues.clientContainer)) {
				msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID("Cliente", AID.ISLOCALNAME));
				msg.setContent(precio + "\t" + tiempoEntrega + "\t" + tienda);
				kill = true;
			}
			// Mensaje para la tienda
			else {
				msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(new AID("Tienda" + nTiendas, AID.ISLOCALNAME));
				nTiendas++;
				msg.setContent(manufacturer + "\t" + modelo + "\t" + maxTiempoEntrega);
			}

			myAgent.send(msg);
			finished = true;

		}

		/*
		 * Dira si se ha terminado de implementar el mensaje a mandar
		 */
		public boolean done() {

			if (kill)
				myAgent.doDelete();
			return finished;

		}
	}
	
}

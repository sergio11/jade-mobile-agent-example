package shop;

import behaviour.MoveAgentBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mobile.MobileAgent;
import models.Producto;
import utils.TestValues;

public final class ShopAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	private String tienda;
	private Producto[] tiendaDatabase;

	@Override
	protected void setup() {
		//	Resgistra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());

		Object[] args = getArguments();
		if (args.length != 1) {
			System.out.println("Uso: Servidor(\"Tienda\")");
			System.exit(0);
		}
		else {
			tienda = (String) args[0];
			if (tienda.equals("Tienda1")) {
				tiendaDatabase = TestValues.tienda1Database;
			}
			else if (tienda.equals("Tienda2")) {
				tiendaDatabase = TestValues.tienda2Database;
			}
			else if (tienda.equals("Tienda3")) {
				tiendaDatabase = TestValues.tienda3Database;
			}
			else {
				System.out.println("El nombre de la Tienda debe ser: 'Tienda1', 'Tienda2' o 'Tienda3'");
				System.exit(0);
			}
		}
		addBehaviour(new ReceiveMessage());
	}
	
	/*
	 * Subclase que proporciona los metodos para esperar cualquier mensaje y
	 * realizar la correspondiente respuesta al mismo
	 */
	class ReceiveMessage extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		/*
		 * Metodo que realizara la busqueda del producto que se ajuste a las
		 * caracteristicas del producto que el Mobile Agent ha proporcionado
		 */
		private String encontrarProducto(String manufacturer, String modelo) {

			String productFound = "";

			for (int i = 0; i < TestValues.nProductos; i++) {
				Producto p = tiendaDatabase[i];

				if (p.getManufacturer().equals(manufacturer) && p.getModelo().equals(modelo)) {

					productFound = Integer.toString(p.getPrecio()) + "\t" + Integer.toString(p.getTiempoEntrega());
				}
			}
			return productFound;
		}

		/*
		 * Metodo que espera por cualquier mensaje. Se mostrara por pantalla y
		 * se repondera con el resultado de la busqueda del producto recibido (
		 * el tiempo de envio y el precio)
		 */
		public void action() {

			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage msg = myAgent.receive(mt);

			if (msg != null) {
				String request = msg.getContent();
				ACLMessage reply = msg.createReply();
				System.out.println("Mensaje recibido por tienda: " + request);
				String aux[] = request.split("\t");

				// Respuesta con los datos solicitados
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(encontrarProducto(aux[0], aux[1]) + "\t" + tienda);
				myAgent.send(reply);

				// Respuesta solicitando al agente que se mueva de vuelta hacia
				// el Main Server
				myAgent.addBehaviour(new MoveAgentBehaviour(MobileAgent.AGENT_NAME, "Main-Container"));
			} else {
				block();
			}

		}
	}
}

package behaviour;

import client.ClientAgentGUI;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/* 
 * Recibe mensajes desde el MobileAgent. Este mensaje contendra la informacion sobre 
 * el producto encontrado. Despues de haberlo analizado , es mostrado usando el ClientGUI.
*/
public final class NotifyResults extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private ClientAgentGUI clientGUI;
	
	public NotifyResults(ClientAgentGUI clientGUI) {
		super();
		this.clientGUI = clientGUI;
	}

	@Override
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage msg = myAgent.receive(mt);
		if (msg!=null) {
			String request = msg.getContent();
			// Analiza el mensaje
			String aux[] = request.split("\t");
			int precio = Integer.parseInt(aux[0]);
			int tiempoEntrega = Integer.parseInt(aux[1]);
			String tienda = aux[2];
			System.out.println(precio + "\t" + tiempoEntrega + "\t" + tienda);
			if (precio == Integer.MAX_VALUE)
				clientGUI.showDialog("Producto no encontrado");
			else
				clientGUI.showDialog("Producto encontrado en : \n" + "Tienda: " + tienda + "\nPrecio: " + precio + "\nTiempo Entrega: " + tiempoEntrega);
		}
		else {
			block();
		}
	
	}

}

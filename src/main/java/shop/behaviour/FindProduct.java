package shop.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import models.Producto;
import utils.TestValues;

public final class FindProduct extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	
	/* 
	* Metodo que realizara la busqueda del producto que se ajuste a las caracteristicas del producto
	* que el Mobile Agent ha proporcionado
	*/
	private String encontrarProducto(String manufacturer, String modelo) {
	
		String productFound;

		/*for (int i=0; i< TestValues.nProductos; i++) {
			Producto p = tiendaDatabase[i];
		
			if (p.getManufacturer().equals(manufacturer)
							&& p.getModelo().equals(modelo)) {
			
				return Integer.toString(p.getPrecio()) + "\t" + Integer.toString(p.getTiempoEntrega());
							}	
		}	*/
		return "";
	}

	/* 
	* Metodo que espera por cualquier mensaje. Se mostrara por pantalla y se repondera con 
	* el resultado de la busqueda del producto recibido ( el tiempo de envio y el precio)
	*/
	@Override
	public void action() {

		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt);

		if (msg!=null) {
			String request = msg.getContent();
			ACLMessage reply = msg.createReply();
			System.out.println ("Mensaje recibido por tienda: " + request);
			String aux[] = request.split("\t");
			// Respuesta con los datos solicitados
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(encontrarProducto(aux[0],aux[1])+"\t");
			myAgent.send(reply);
			
		}else {
			block();
		}
	
	}

}

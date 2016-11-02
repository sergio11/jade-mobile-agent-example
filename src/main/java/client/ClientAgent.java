package client;

import java.util.Map;
import java.util.HashMap;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Location;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public final class ClientAgent extends GuiAgent {
	

	private static final long serialVersionUID = 1L;
	private jade.wrapper.AgentContainer home;
	private Map<String, Location> locations = new HashMap<>();
	transient protected ClientAgentGUI clientGUI;

	private int precio;
	private int tiempoEntrega;
	private String tienda;

	// Consigue un JADE Runtime instance
	jade.core.Runtime runtime = jade.core.Runtime.instance();

	// Inicializa el agnet y sus caracteristicas, para poder crear un MobileAgent
	@Override
	protected void setup() {
		// Registra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());

	    try {
			// Crea el container del MobileAgent
			home = this.getContainerController();
			doWait(2000);
			// Obtiene las localizaciones disponible con el AMS
			locations = getLocations();
		}catch (Exception e) { e.printStackTrace(); }	
	    // Crea y muestra el GUI
	    clientGUI = new ClientAgentGUI(this);
	}



	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	// Obtiene del AMS todas las posibles localizaciones a las que se puede
	// mover
	private Map<String, Location> getLocations() {
		try {
			Map<String, Location> locations = new HashMap<>();
			// Get available locations with AMS
			enviarRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));

			// Recibe la respuesta del AMS
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(getAMS()),
					MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage resp = blockingReceive(mt);
			ContentElement ce = getContentManager().extractContent(resp);
			Result result = (Result) ce;
			jade.util.leap.Iterator it = result.getItems().iterator();
			while (it.hasNext()) {
				Location loc = (Location) it.next();
				locations.put(loc.getName(), loc);
			}
			return locations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Envia la Action recibida
	private void enviarRequest(Action action) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(MobilityOntology.getInstance().getName());
		try {
			getContentManager().fillContent(request, action);
			request.addReceiver(action.getActor());
			send(request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

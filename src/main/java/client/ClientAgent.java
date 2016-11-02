package client;

import java.util.Map;

import behaviour.NotifyResults;

import java.util.HashMap;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Location;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mobile.MobileAgent;

public final class ClientAgent extends GuiAgent {
	

	private static final long serialVersionUID = 1L;
	public static final int QUIT = 0;
	public static final int MOVE_AGENT = 2;
	private jade.wrapper.AgentContainer home;
	private Map<String, Location> locations = new HashMap<>();
	transient protected ClientAgentGUI clientGUI;


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
	    this.addBehaviour(new NotifyResults(clientGUI));
	}


	/* 
	 * Recibe un evento del ClientAgentGUI: una solicitud para mover  el MobileAgent o para desconectarlo.
	 */ 
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		int command = ev.getType();
		if (command == QUIT) {
			try {
				home.kill();
			}
			catch (Exception e) { 
				e.printStackTrace();
			}
			clientGUI.setVisible(false);
			clientGUI.dispose();
			doDelete();
			System.exit(0);
		}else if(command == MOVE_AGENT) { 
			// Crea el agente y manda el mensaje al MobileAgent, en orden para moverlo para otro destino
			try {
				Object[] args = new Object[20];
				args[0] = getAID();
				args[1] = (String) ev.getParameter(0); // Manufacturer
				args[2] = (String) ev.getParameter(1); // Modelo
				args[3] = (Integer) ev.getParameter(2); // tiempoEntrega
				jade.wrapper.AgentController agent = home.createNewAgent("mobile", MobileAgent.class.getName(), args);
				agent.start();
				agent.move(new jade.core.ContainerID("Main-Container", null));
			} catch (Exception ex) {
				System.out.println("Problem creating new agent");
			}
		}

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
	
	/*
	 * Mueve el MobileAgent al destino requerido, construyendo y mandándole un
	 * Request action
	 */
	private void moverAgente(String nombreAgente, String nombreDestino) {

		AID aid = new AID(nombreAgente, AID.ISLOCALNAME);
		// Crea la descripcion del agente (Agent Description) que contiene el
		// agente y su destino, y lo envia
		Location dest = (Location) locations.get(nombreDestino);
		MobileAgentDescription mad = new MobileAgentDescription();
		mad.setName(aid);
		mad.setDestination(dest);
		MoveAction ma = new MoveAction();
		ma.setMobileAgentDescription(mad);
		enviarRequest(new Action(aid, ma));
	}

}

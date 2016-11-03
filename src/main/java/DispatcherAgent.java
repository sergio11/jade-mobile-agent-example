import java.util.HashMap;
import java.util.Map;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DispatcherAgent extends Agent {

	private static final long serialVersionUID = 1L;
	protected final Map<String, Location> locations;
	

	public DispatcherAgent() {
		super();
		locations = this.getLocations();
	}
	

	@Override
	protected void setup() {
		// Registra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
	}

	/*
	 * Metodo para obtener la localizacion con el AMS de los Agentens que
	 * queremos para mandar los mensajes
	 */
	private Map<String, Location> getLocations() {
		try {
			Map<String, Location> locations = new HashMap<>();
			// Obtiene las localizaciones disponible cn el AMS
			sendRequest(new Action(getAMS(), new QueryPlatformLocationsAction()));
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
	
	protected void sendRequest(Action action) {
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
	
	protected void moveAgent(String nombreAgente, String nombreDestino) {
		AID aid = new AID(nombreAgente, AID.ISLOCALNAME);
		// Crea el Agent Description que contiene el agente y el destino, y lo envia
		Location dest = (Location) locations.get(nombreDestino);
		// Se crea la descripcion y el MoveAction y se envia la peticion(request)
		MobileAgentDescription mad = new MobileAgentDescription();
		mad.setName(aid);
		mad.setDestination(dest);
		MoveAction ma = new MoveAction();
		ma.setMobileAgentDescription(mad);
		sendRequest(new Action(aid, ma));
	}
	
	

}

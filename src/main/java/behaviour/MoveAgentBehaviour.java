package behaviour;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Location;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;

public final class MoveAgentBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private String agentName;
	private String targetContainer;
	
	public MoveAgentBehaviour(String agentName, String targetContainer) {
		super();
		this.agentName = agentName;
		this.targetContainer = targetContainer;
	}

	@Override
	public void action() {
		AID aid = new AID(agentName, AID.ISLOCALNAME);
		// Crea la descripcion del agente (Agent Description) que contiene el
		// agente y su destino, y lo envia
		Location dest = new jade.core.ContainerID(targetContainer, null);
		MobileAgentDescription mad = new MobileAgentDescription();
		mad.setName(aid);
		mad.setDestination(dest);
		MoveAction ma = new MoveAction();
		ma.setMobileAgentDescription(mad);
		// send request
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(MobilityOntology.getInstance().getName());
		Action action = new Action(aid, ma);
		try {
			myAgent.getContentManager().fillContent(request, action);
			request.addReceiver(action.getActor());
			myAgent.send(request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

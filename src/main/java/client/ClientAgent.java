package client;

import behaviour.MoveAgentBehaviour;
import behaviour.NotifyResults;
import jade.content.lang.sl.SLCodec;
import jade.domain.mobility.MobilityOntology;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import mobile.MobileAgent;

public final class ClientAgent extends GuiAgent {
	

	private static final long serialVersionUID = 1L;
	public static final int QUIT = 0;
	public static final int MOVE_AGENT = 2;
	private jade.wrapper.AgentContainer home;
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
				this.addBehaviour(new MoveAgentBehaviour(MobileAgent.AGENT_NAME, "Main-Container"));
			} catch (Exception ex) {
				System.out.println("Problem creating new agent");
			}
		}

	}

}

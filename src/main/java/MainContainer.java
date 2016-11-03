
import jade.core.Runtime;
import client.ClientAgent;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import main.ASAgent;
import shop.ShopAgent;
import utils.TestValues;

public final class MainContainer {
	
	
	private static void createClientContainer() throws ControllerException{
		Runtime rt = Runtime.instance();
		ProfileImpl pc = new ProfileImpl(false);
		pc.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		AgentContainer container = rt.createAgentContainer(pc);
		container.start();
		AgentController agentController = container.createNewAgent(ClientAgent.AGENT_NAME, ClientAgent.class.getName(), null);
		agentController.start();
	}
	
	private static void createMainContainer() throws ControllerException{
		Runtime rt = Runtime.instance();
		Properties p = new ExtendedProperties();
		p.setProperty("gui", "true");
		ProfileImpl pc = new ProfileImpl(p);
		AgentContainer container = rt.createMainContainer(pc);
		container.start();
		AgentController agentController = container.createNewAgent(ASAgent.AGENT_NAME, ASAgent.class.getName(), null);
		agentController.start();
		
	}
	
	private static void createShopsContainer() throws ControllerException{
		for(String shop: TestValues.shops){
			Runtime rt = Runtime.instance();
			ProfileImpl pc = new ProfileImpl(false);
			pc.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer container = rt.createAgentContainer(pc);
			container.start();
			AgentController agentController = container.createNewAgent(shop, ShopAgent.class.getName(), new Object[]{shop});
			agentController.start();
		}
	}

	public static void main(String[] args) {
		try {
			createMainContainer();
			createShopsContainer();
			createClientContainer();
		} catch (ControllerException e) {
			e.printStackTrace();
		}

	}

}

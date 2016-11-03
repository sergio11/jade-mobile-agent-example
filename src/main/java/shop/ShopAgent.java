package shop;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.mobility.MobilityOntology;

public final class ShopAgent extends Agent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		//	Resgistra el lenguaje y la ontologia
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());
	}
	
}

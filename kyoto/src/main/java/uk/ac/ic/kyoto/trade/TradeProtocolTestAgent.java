package uk.ac.ic.kyoto.trade;

import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import uk.ac.ic.kyoto.countries.TradeProtocol;
import uk.ac.ic.kyoto.singletonfactory.SingletonProvider;
import uk.ac.ic.kyoto.tradehistory.TradeHistory;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.network.Message;
import uk.ac.imperial.presage2.core.network.MulticastMessage;
import uk.ac.imperial.presage2.core.network.NetworkAddress;
import uk.ac.imperial.presage2.core.simulator.SimTime;
import uk.ac.imperial.presage2.util.fsm.FSMException;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;

public class TradeProtocolTestAgent extends AbstractParticipant {
	
	Logger logger = Logger.getLogger(TradeProtocolTestAgent.class);
	
	private TradeProtocol tradeProtocol;
	
	private int carbonOutput;
	private int emissionsTarget;
	private int carbonOffset;
	
	public TradeProtocolTestAgent(UUID id, String name, String ISO){
		super(id, name);
	}
	
//	public FakeCanadaAgent(UUID id, String name,String ISO, double landArea, double arableLandArea, double GDP,
//			double GDPRate, double emissionsTarget, double energyOutput, double carbonOutput){
//		super(id, name, ISO, landArea, arableLandArea, GDP,
//				GDPRate, emissionsTarget, energyOutput, carbonOutput);
//		// TODO Auto-generated constructor stub
//	}
	
	
	@Override
	protected void processInput(Input in) {
		if (this.tradeProtocol.canHandle(in)) {
			this.tradeProtocol.handle(in);
		}
		
		if(in instanceof Message){
			try{
				@SuppressWarnings("unchecked")
				Message<OfferMessage> m = (Message<OfferMessage>) in;
				Offer t = m.getData().getOffer();

				if(!this.tradeProtocol
						.getActiveConversationMembers()
							.contains(m.getFrom())){
					try {
						this.tradeProtocol.offer(
								m.getFrom(), 
								t.getQuantity(), 
								t.getUnitCost(), 
								t.reverse().getType());
					} catch (FSMException e) {
						e.printStackTrace();
					}
				}
			}catch(ClassCastException e){
				logger.warn("Class cast exception");
				logger.warn(e);
			}
		}			
	}
	
	@Override
	public void initialise() {
		super.initialise();
		carbonOutput = 80;
		emissionsTarget = 20;
		carbonOffset = 10;
		try {
			tradeProtocol = new TradeProtocol(getID(), authkey, environment, network, null) {
				@Override
				protected boolean acceptExchange(NetworkAddress from,
						Offer trade) {
					return true;
					/*if (carbonOutput - emissionsTarget + carbonOffset < 0) {
						return true;
					}
					return true;*/
				}
			};
		} catch (FSMException e) {
			e.printStackTrace();
		}		
		
	}
	
	private int counter = 0;

	@Override
	public void execute() {
		super.execute();
		this.tradeProtocol.incrementTime();
		//if(counter < 7){
			int quantity = 10;
			int unitCost = 2;
			Offer trade = new Offer(quantity, unitCost, TradeType.SELL);
			this.network.sendMessage(
						new MulticastMessage<OfferMessage>(
								Performative.PROPOSE, 
								Offer.TRADE_PROPOSAL, 
								SimTime.get(), 
								this.network.getAddress(),
								this.tradeProtocol.getAgentsNotInConversation(),
								new OfferMessage(trade))
			//TODO might need to acquire Trade ID here
					);
		counter++;
		//}
	}
	
}

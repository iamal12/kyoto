/**
 * 
 */
package uk.ac.ic.kyoto.trade;

import java.util.UUID;

/**
 * @author cmd08 and farhanrahman
 *
 */
public class Trade{
	final int quantity;
	final int unitCost;
	final TradeType type;
	final UUID tradeID;

	public static String TRADE_PROPOSAL = "Trade proposal";	
	
	public Trade(int quantity, int unitCost, TradeType type, UUID tradeID) {
		this.quantity = quantity;
		this.unitCost = unitCost;
		this.type = type;
		this.tradeID = tradeID;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getUnitCost() {
		return unitCost;
	}

	public int getTotalCost() {
		return unitCost * quantity;
	}
	
	public TradeType getType(){
		return this.type;
	}

	@Override
	public String toString() {
		return "Trade: "+quantity+" @ "+unitCost; 
	}

	public boolean equals(Trade t){
		if(this == t) {
			return true;
		} else if (	this.quantity == t.getQuantity() && 
					this.unitCost == t.getUnitCost() && 
					this.type == t.getType()) {
			return true;
		} else if (	this.quantity == -t.getQuantity() &&
					this.unitCost == t.getUnitCost() && 
					this.type == t.reverse().getType()){
			return true;
		} else if ( this.quantity == t.getQuantity() &&
					this.unitCost == -t.getUnitCost() &&
					this.type == t.reverse().getType()){
			return true;
		} else {
			return false;
		}
	}
	
	public Trade reverse(){
		TradeType t = this.type.equals(TradeType.BUY)?TradeType.SELL:TradeType.BUY;
		return new Trade(this.quantity, this.unitCost, t,this.tradeID);
	}

}

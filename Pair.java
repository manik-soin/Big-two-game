
/**
 * Models a Pair hand. This hand consists of two cards with the same rank. The card with a higher suit in a pair is referred to as the top card of this pair. A pair with a higher rank beats a pair with a lower rank. For pairs with the same rank, the one containing the highest suit beats the other.
 * @author manik
 */
public class Pair extends Hand {
	/**
	 * a constructor for building a Pair hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Pair hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		
		if (this.size() == 2) {
			
			
			Card card1 = getCard(0);
			Card card2 = getCard(1);
			
			int rank1 = card1.getRank();
			int rank2 = card2.getRank();
			
			if (rank1==rank2) 
			{
				return true;
			} 
			else 
			{
				return false;
			}
		} 
		else 
		{
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that is a Pair hand
	 */
	public String getType() {
		return "Pair";
	}

}

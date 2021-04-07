/**
 * Models a Flush Hand. This hand consists of five cards with the same suit. The card with the highest rank in a flush is referred to as the top card of this flush. A flush always beats any straights. A flush with a higher suit beats a flush with a lower suit. For flushes with the same suit, the one having a top card with a higher rank beats the one having a top card with a lower rank.
 * @author manik
 */
public class Flush extends Hand {

	/**
	 * a constructor for building a Flush hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for checking if this is a valid Flush hand.
	 * @return true if valid, false otherwise
	 */

	public boolean isValid() {
		this.sort();
		int n = this.getNumOfCards();
		
		if (n == 5) {
			for (int i = 0; i < n-1; i++) 
			{
				Card firstCard=this.getCard(i);
				Card secondCard=this.getCard(i+1);
			
				int suit1= firstCard.getSuit();
				int suit2= secondCard.getSuit();
					
				if(suit1 != suit2)
				{
					return false;
				}
			}
			return true;
		} 
		else 
		{
			return false;
		}
	}
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Flush hand
	 */
	public String getType() {
		return "Flush";
	}

}

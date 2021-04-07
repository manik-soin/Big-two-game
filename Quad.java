/**
 * Models a Quad hand. This hand consists of five cards, with four having the same rank. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad. A quad always beats any straights, flushes and full houses. A quad having a top card with a higher rank beats a quad having a top card with a lower rank.
 * @author manik
 */
public class Quad extends Hand {
	/**
	 * a constructor for building a Quad hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of Quad hand.
	 * @return Get top card of the hand of type Card. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
	 */
	public Card getTopCard() {
		this.sort();
		int[][] ranks = new int[13][];
		int n = this.getNumOfCards();
		Card topCard;
		
		for (int i = 0; i < n; i++) {
			
			Card card1 = this.getCard(i);
			int rank1 = card1.getRank();
			int suit1 = card1.getSuit();
			
			ranks[rank1][0]++;
			
			if (suit1 > ranks[rank1][1]) 
			{
				ranks[rank1][1] = suit1;
				ranks[rank1][2] = i;
			}
		}
		for (int i = 0; i <= 12; i++) {
			if (ranks[i][0] == 4) 
			{
				int topCardRank = ranks[i][2];
				topCard = this.getCard(topCardRank);
				return topCard;
				
			}
		}
		topCard = this.getCard(this.getNumOfCards()-1);
		return topCard;
	}
	
	/**
	 * a method for checking if this is a valid Quad hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		this.sort();
		int n = this.getNumOfCards();
		
		if (n == 5) {
			int[] ranks = new int[13];
			for (int i = 0; i < 5; i++) 
			{
				Card card1 = this.getCard(i);
				int rank1 = card1.getRank();
				
				ranks[rank1]++;
			}
			for (int i = 0; i <= 12; i++) {
				if (ranks[i] == n-1) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Quad hand
	 */
	public String getType() {
		return "Quad";
	}

}

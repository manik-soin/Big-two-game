/**
 * Models a Full House hand. This hand consists of five cards, with two having the same rank and three having another same rank. The card in the triplet with the highest suit in a full house is referred to as the top card of this full house. A full house always beats any straights and flushes. A full house having a top card with a higher rank beats a full house having a top card with a lower rank.
 * @author manik
 */
public class FullHouse extends Hand {

	/**
	 * a constructor for building a Full House hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of Full House hand.
	 * @return Get top card of the hand of type Card. The card in the triplet with the highest suit in a full house is referred to as the top card of this full house.
	 */
	public Card getTopCard() {
		this.sort();
		
		int[][] ranks = new int[13][20];
		Card topCard;
		
		int n = this.getNumOfCards();
		
	
		for(int p=0;p<13;p++) {
			ranks[p][0]=0;
		}
		
		
		for (int i = 0; i < n; i++) 
		{
			
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
		for (int i = 0; i <= 12; i++) 
		{
			if (ranks[i][0] == 3)
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
	 * a method for checking if this is a valid Full House hand.
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		
		this.sort();
		int n = this.getNumOfCards();
		if (n == 5) {
			int[] ranks = new int[13];
			for (int i = 0; i < n; i++) 
			{
				Card card1= this.getCard(i);
				int rank1= card1.getRank();
				ranks[rank1]++;
			}
			
			int Two_are_same = 0;
			int Three_are_same = 0;
			
			for (int i = 0; i <= 12; i++) {
				if (ranks[i] == 2) {
					Two_are_same++;
				} else if (ranks[i] == 3) {
					Three_are_same++;
				}
				if (Two_are_same>0 && Three_are_same>0) {
					return true;
				}
			}
			return false;
		} else 
		{
			return false;
		}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying that this is a Full House hand
	 */
	public String getType() {
		return "FullHouse";
	}

}

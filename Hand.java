/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It has a private instance variable for storing the player who plays this hand. It also has methods for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting the top card of this hand, and checking if it beats a specified hand.
 * @author manik
 */
public abstract class Hand extends CardList {
	
	
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player A specified player of time CardGamePlayer to associate the hand with.
	 * @param cards A CardList to make the hand from.
	 */
	Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		if(cards != null)
		{
			this.numOfCards = cards.size();
			for(int i = 0; i < cards.size(); ++i)
			{
				this.addCard(cards.getCard(i));
			}
		} else {
			this.numOfCards = 0;
		}
	}
	
	private CardGamePlayer player;
	private int numOfCards;
	
	/**
	 * a method for retrieving the player of this hand.
	 * @return Get player of the hand of type CardGamePlayer
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * @return Get top card of the hand of type Card
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	/**
	 * This method is used to retrieve the number of cards of this hand.
	 *	
	 * @return the number of cards of this hand
	 */
		public int getNumOfCards()
		{
			return this.numOfCards;
		}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand A hand to check with
	 * @return true if beats, false otherwise
	 */
	public boolean beats(Hand hand)
	{

		// Handle the edge case
		if(hand == null)
		{
			return true;
		}

		// Only the hand with the same number of cards may have
		// a chance to beat another
		if(this.getNumOfCards() != hand.getNumOfCards())
		{
			return false;
		} 
		else if (this.getType() == hand.getType()) 
		{
			return (this.getTopCard().compareTo(hand.getTopCard()) == 1 ) ? true : false;
		}
		else if (this.getType() == "StraightFlush") {return true;}
		else if (this.getType() == "Quad" && hand.getType()!="StraightFlush") {return true;}
		else if (this.getType() == "FullHouse" && hand.getType()!="StraightFlush" && hand.getType()!="Quad") {return true;}
		else if (this.getType() == "Flush" && hand.getType() != "FullHouse" && hand.getType()!="StraightFlush" && hand.getType()!="Quad") {return true;}
	return false;
			
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return true if valid, false otherwise
	 */
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType();
}

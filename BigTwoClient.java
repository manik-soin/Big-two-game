import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.net.*;
import java.io.*;
/**
 * BigTwoClient implements the CardGame interface and models the Big Two card game logics. Besides, it also implements the NetworkGame interface and handles the communication with other clients by sending and receiving messages to and from a game server. In order to allow players to exchange chat messages during the game
 * @author manik
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{

	
	BigTwoClient(){
		this.handsOnTable = new ArrayList<Hand>();
		this.playerList = new ArrayList<CardGamePlayer>();
		
		
		for (int i = 0; i < NUM_OF_PLAYERS; i++) {
			this.playerList.add(new CardGamePlayer());
		}
		
		this.numOfPlayers = this.playerList.size();
		
		this.table = new BigTwoTable(this);
		this.table.disable();
		
		this.setPlayerName (JOptionPane.showInputDialog("Please enter you name: ", "Default Name"));
		if(playerName == null) {
			playerName = "Default";
		}
	
		this.makeConnection();
		
	}
	private final static int NUM_OF_PLAYERS = 4;// Total Number of Players
	private int numOfPlayers; // number of players
	private Deck deck;// a deck of cards
	private ArrayList<CardGamePlayer> playerList;//a list of players
	private ArrayList<Hand> handsOnTable;//a list of hands played on the table
	private int playerID;// an integer specifying the playerID (i.e., index) of the local player
	
	
	private int currentIdx;//an integer specifying the index of the current player
	private int lastHandIdx;//an integer specifying the index of the last player
	boolean firstturn;// a boolean if it is the first turn
	private BigTwoTable table;//a Big Two table which builds the GUI for the game and handles all user actions
	
	
	private String playerName;//a string specifying the name of the local player
	private String serverIP;//a string specifying the IP address of the game server
	private int serverPort;//an integer specifying the TCP port of the game server
	private Socket sock;//a socket connection to the game server
	private ObjectOutputStream oos;// an object output stream object
	private ObjectInputStream ois;// an object input stream object
	
	/**
	 * a function to check if client is connected
	 * @return true if it is connected
	 */
	public boolean isConnected() {
		if (sock.isClosed()) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * A function to return the playerID
	 * @return returns the playerID
	 */
	@Override
	public int getPlayerID() {
		return this.playerID;
	}
	/**
	 * A function to set the playerID
	 * 
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * A function to return the player name
	 * @return returns the player name
	 */
	@Override
	public String getPlayerName() {
		return this.playerName;
	}
	/**
	 * A function to set the player name
	 * 
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	/**
	 * A function to return the Server IP
	 * @return returns the the Server IP
	 */
	@Override
	public String getServerIP() {
		return this.serverIP;
	}
	/**
	 * A function to set the Server IP
	 * 
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	/**
	 * A function to return the Server Port
	 * @return returns the the Server port
	 */
	@Override
	public int getServerPort() {
		return this.serverPort;
	}
	/**
	 * A function to set the Server Port
	 * 
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
/**
 * a method for making a socket connection with the game server
 * 
 */
	@Override
	public void makeConnection() {

		
		

			try {
				setServerIP("127.0.0.1");
				setServerPort(2396);
				
				sock = new Socket(this.getServerIP(),this.getServerPort());
				oos = new ObjectOutputStream(sock.getOutputStream());
				Thread  serverHandlerThread= new Thread(new ServerHandler());
				serverHandlerThread.start();
				
				CardGameMessage join = new CardGameMessage(1,-1,this.getPlayerName());///$
				sendMessage(join);
				
				CardGameMessage ready = new CardGameMessage(4,-1,null);
				sendMessage(ready);
				
			} catch (Exception ex) {}

	}
	/**
	 * a method for parsing the messages received from the game server. This method should be called from the thread 
	 * responsible for receiving messages from the game server. Based on the message type,
	 * different actions will be carried out.
	 */
	@Override
	public synchronized void parseMessage(GameMessage message) {
		
		int messageType = message.getType();
		
		if (messageType == 6) 
		{
			checkMove(message.getPlayerID(),(int[]) message.getData());
			this.table.repaint();
		} 
		else if (messageType == 0) {
			if(message.getData() != null) {
				for(int i=0; i<=NUM_OF_PLAYERS-1; i++) {
					String data = ((String[])message.getData())[i];
					if(data != null) {
						this.playerList.get(i).setName(data);
					}
				}
			}

			this.setPlayerID(message.getPlayerID());
			
			this.table.repaint();
		} else if (messageType == 2) {
			this.table.printMsg("Failed to join the game: server is full\n Please try again later!\n");
			playerID = -1;
			table.repaint();
		} else if (messageType == 3) {
			this.table.printMsg( playerList.get(message.getPlayerID()).getName() + " has left the game" + "\n");
			playerList.get(message.getPlayerID()).setName("");
			
			if(endOfGame() != true) {
				this.table.disable();
				
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				
				for(int i=0; i<=NUM_OF_PLAYERS-1; i++) {
					playerList.get(i).removeAllCards();
				}
			}
			
			table.repaint();
		} else if (messageType == 1) {
			this.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			this.table.repaint();
		} else if (messageType == 4) {
			this.handsOnTable = new ArrayList <Hand>();
			
			table.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!" + "\n");
			table.repaint();
		} else if (messageType == 5) {
			this.deck = new BigTwoDeck();
			this.deck = (BigTwoDeck) message.getData();
			start(this.deck);
			
			table.printMsg("Big Two game has started!!" + "\n");
			table.printMsg("Player " + playerList.get(currentIdx).getName() + ", it is your turn \n");
			table.enable();
			table.repaint();		
			
		} else if (messageType == 7) {
			this.table.printChatMsg((String) message.getData());
		}
	}
/**
 * method to send a message
 */
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (Exception e) {}
	}
/**
 * method to get the Number of Players
 * @return number of players
 */
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
/**
 * method to return the deck
 * @return the deck
 */
	@Override
	public Deck getDeck() {
		return this.deck;
	}
/**
 * method to return the player list
 * @return player list
 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}
/**
 * method to return the hands on table
 * @return hands on the table
 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
/**
 * method to return the current index
 * @return current index
 */
	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	/**
	 * for clearing the cards
	 */
	public void clearCards() {
		for(int i = 0; i < this.getNumOfPlayers(); i++) {
			this.getPlayerList().get(i).removeAllCards();
		}
		this.getHandsOnTable().clear();
	}
	/**
	 * Allotting the cards
	 */
	public void allotCards() {
		for (int i = 0; i < deck.size(); i++) 
		{
			this.getPlayerList().get(i%getNumOfPlayers()).addCard(deck.getCard(i));
			
		}
	}
	
	/**
	 * find and set the first player
	 */
	public void setFirstPlayer() {
		for(int i=0;i<deck.size();i++) {
			//finding the player who has first turn 
			if (deck.getCard(i).getRank() == 2 && deck.getCard(i).getSuit() == 0) 
			{
				currentIdx = i % getNumOfPlayers();
				table.setActivePlayer(this.getCurrentIdx());
			}
		}
		
		firstturn = true;
		//sort all the cards in hand
		for(int i=0; i<NUM_OF_PLAYERS;i++) 
		{
			playerList.get(i).sortCardsInHand();
	
		}
	}
	
	/**
	 * a method for starting the game with a (shuffled) deck of
cards supplied as the argument. It implements the Big Two game logics.
	 * @param deck A deck of cards to distribute and start with
	 */
	@Override
	public void start(Deck deck) {
		
		clearCards();
		allotCards();
		setFirstPlayer();		
		
		table.reset();
		table.repaint();
		table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
	}
	/**
	 * a method for making a move by a
	 * player with the specified playerID using the cards specified by the list of indices. This
	 * method should be called from the BigTwoTable when the local player presses either the
	 * Play or Pass button
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx);
		sendMessage(move);
	}
	/**
	 * a method for checking a move
	 * made by a player. This method should be called from the parseMessage() method from
	 * the NetworkGame interface when a message of the type MOVE is received from the game
	 * server
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		Hand attempt;
		
		if(cardIdx==null) {attempt=null;}
		else { attempt = composeHand(this.getPlayerList().get(playerID),this.getPlayerList().get(playerID).play(cardIdx));}
		
		boolean condt1;
		
		if((!this.getHandsOnTable().isEmpty() && playerID != lastHandIdx && attempt!=null )) {
			condt1=(!(attempt.beats(this.getHandsOnTable().get(this.getHandsOnTable().size() - 1))) || attempt.size() != this.getHandsOnTable().get(this.getHandsOnTable().size() - 1).size());
		}
		else condt1=false;
		
		if (( firstturn||playerID == lastHandIdx) && cardIdx == null ) {
			table.printMsg("{pass}");
			table.printMsg(" <== Not a legal move!!!\n");
		} else if (cardIdx == null){
			table.printMsg("{pass}\n");
			currentIdx = (playerID + 1) % NUM_OF_PLAYERS;
			table.setActivePlayer(this.getCurrentIdx());
			firstturn = false;
			table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
		} else if (attempt == null) {
			table.printCards(this.getPlayerList().get(playerID).play(cardIdx));
			table.printMsg(" <== Not a legal move!!!\n");
		} else if (!attempt.contains(new BigTwoCard(0,2)) && firstturn ) {
			table.printCards(attempt);
			table.printMsg(" <== Not a legal move!!!\n");
		} else if (condt1) {
			table.printCards(attempt);
			table.printMsg(" <== Not a legal move!!!\n");
		} else {
			this.getPlayerList().get(playerID).removeCards(this.getPlayerList().get(playerID).play(cardIdx));
			attempt.sort();
			this.getHandsOnTable().add(attempt);
			lastHandIdx = playerID;
			
			
			table.printMsg("{"+attempt.getType()+"} ");
			table.printCards(attempt);
			table.printMsg("\n");
			
			
			currentIdx = (playerID + 1) % NUM_OF_PLAYERS;
	
			table.setActivePlayer(this.getCurrentIdx());
			firstturn = false;
			table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"'s turn:\n");
		}
		
		
		table.resetSelected();
		table.repaint();
		
		if (endOfGame()) {
			table.printMsg("Game ends\n");
			for (int i = 0; i <= NUM_OF_PLAYERS-1; i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					table.printMsg(this.getPlayerList().get(i).getName() + " wins the game.\n");
				} else {
					table.printMsg(this.getPlayerList().get(i).getName() + " has " + this.getPlayerList().get(i).getNumOfCards() + " cards in hand.\n");
				}
			}
			table.disable();
		}
	}
	/**
	 * to check if it is the end of the game or not
	 * @return true if game has ended
	 */
	@Override
	public boolean endOfGame() {
		for (int i = 0; i < getNumOfPlayers(); i++) {
			if (this.getPlayerList().get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * an inner class that implements the Runnable interface. 
	 * implements the run() method from the Runnable interface and creates a thread
	 * with an instance of this class as its job in the makeConnection() method from the
	 * NetworkGame interface for receiving messages from the game server. Upon receiving a
	 * message, the parseMessage() method from the NetworkGame interface will be
	 * called to parse the messages accordingly
	 * @author manik
	 *
	 */
	class ServerHandler implements Runnable {
		@Override
		public void run() {	
			try {
				ois = new ObjectInputStream(sock.getInputStream());
				CardGameMessage message;
				// reading the incoming messages from the server
				while (!sock.isClosed()) {
					if ((message = (CardGameMessage) ois.readObject()) != null) {
						parseMessage(message);
					}
				}
				ois.close();
			} catch (Exception ex) {}
			
		}
		
	}
	/**
	 * a method for creating an instance of BigTwoClient
	 * @param args - not used
	 */
	public static void main(String[] args) {
		// Creating a new BigTwoClient object
		 BigTwoClient bigTwoClient = new BigTwoClient();

		 // Creating a new deck
		 BigTwoDeck bigTwoDeck = new BigTwoDeck();
	}
	/**
	 * a method for
	 * returning a valid hand from the specified list of cards of the player
	 * @param player - player who is playing the game
	 * @param cards - cards of the deck
	 * @return hand or null based on whether hand is valid or not
	 */	
public static Hand composeHand(CardGamePlayer player, CardList cards) {
		
		//If hand is empty
		if(cards==null) {return null;}
		
		Hand hand = new StraightFlush(player,cards);
		
		if (hand.isValid()) {
			return hand;
		}
		hand = new Quad(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Quad(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new FullHouse(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Flush(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Straight(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Triple(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Pair(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		hand = new Single(player,cards);
		if (hand.isValid()) {
			return hand;
		}
		return null;	
	}
}
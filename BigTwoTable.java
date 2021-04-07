import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
//import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultCaret;



/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author manik
 */
public class BigTwoTable implements CardGameTable {
	
	
	
	
	
	
	

	private BigTwoClient game;
	private final static int NUM_OF_PLAYERS = 4;
	private final static int MAX_CARD_NUM = 13;
	private boolean[] selected;//a boolean array indicating which cards are being selected
	private int activePlayer;//an integer specifying the index of the active player
	private JFrame frame;//the main window of the application
	private JPanel bigTwoPanel;//a panel for showing the cards of each player and the cards played on the table
	private JButton playButton;//a Play button for the active player to play the selected cards
	private JButton passButton;//a Pass button for the active player to pass his/her turn to the next player
	private JTextArea msgArea;//a text area for showing the current game status as well as end of game messages
	private Image[][] cardImages;//a 2D array storing the images for the faces of the cards
	private Image cardBackImage;//an image for the backs of the cards
	private Image[] avatars;//an array storing the images for the avatars
	private JMenu menu;//this is the top menu
	private Image bgImage;//a background image for aesthetics
	private boolean clickable;//is an object clickable or not
	
	private JTextArea chatArea;//a text area for showing the player chat

	private JTextField chatTypeArea;//text field for entering chats

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game A Card Game of BigTwo type to play through this GUI
	 */
	BigTwoTable(CardGame game){
		this.game = (BigTwoClient) game;
		this.selected = new boolean[13];
		
		bgImage = new ImageIcon("ds_wallp.jpg").getImage();
		
		
		avatars = new Image[4];
		avatars[0] = new ImageIcon("avatars/p1.gif").getImage();
		avatars[1] = new ImageIcon("avatars/p2.gif").getImage();
		avatars[2] = new ImageIcon("avatars/p3.gif").getImage();
		avatars[3] = new ImageIcon("avatars/p4.gif").getImage();
		
		
		cardImages = new Image[NUM_OF_PLAYERS][MAX_CARD_NUM];
		
		
		
		for(int i=2;i<10;i++) {
		
		cardImages[0][i-1] = new ImageIcon("cards/"+i+"d.gif").getImage();
		cardImages[1][i-1] = new ImageIcon("cards/"+i+"c.gif").getImage();
		cardImages[2][i-1] = new ImageIcon("cards/"+i+"h.gif").getImage();
		cardImages[3][i-1] = new ImageIcon("cards/"+i+"s.gif").getImage();
		
		}
	
		cardImages[0][9] = new ImageIcon("cards/td.gif").getImage();
		cardImages[1][9] = new ImageIcon("cards/tc.gif").getImage();
		cardImages[2][9] = new ImageIcon("cards/th.gif").getImage();
		cardImages[3][9] = new ImageIcon("cards/ts.gif").getImage();
		
		cardImages[0][10] = new ImageIcon("cards/jd.gif").getImage();
		cardImages[1][10] = new ImageIcon("cards/jc.gif").getImage();
		cardImages[2][10] = new ImageIcon("cards/jh.gif").getImage();
		cardImages[3][10] = new ImageIcon("cards/js.gif").getImage();
		
		cardImages[0][11] = new ImageIcon("cards/qd.gif").getImage();
		cardImages[1][11] = new ImageIcon("cards/qc.gif").getImage();
		cardImages[2][11] = new ImageIcon("cards/qh.gif").getImage();
		cardImages[3][11] = new ImageIcon("cards/qs.gif").getImage();
		
		cardImages[0][12] = new ImageIcon("cards/kd.gif").getImage();
		cardImages[1][12] = new ImageIcon("cards/kc.gif").getImage();
		cardImages[2][12] = new ImageIcon("cards/kh.gif").getImage();
		cardImages[3][12] = new ImageIcon("cards/ks.gif").getImage();
		
		cardImages[0][0] = new ImageIcon("cards/ad.gif").getImage();
		cardImages[1][0] = new ImageIcon("cards/ac.gif").getImage();
		cardImages[2][0] = new ImageIcon("cards/ah.gif").getImage();
		cardImages[3][0] = new ImageIcon("cards/as.gif").getImage();
		

		cardBackImage = new ImageIcon("cards/b.gif").getImage();
		
		
		
		
		
		//gui setup
		frame = new JFrame();
		frame.setSize(900, 735);
		frame.setResizable(false);
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(600,735));
	    frame.add(bigTwoPanel,BorderLayout.WEST);
		
	    JMenuBar menuBar = new JMenuBar();
	    menu = new JMenu("Game");
	    menuBar.add(menu);
	    
	    JMenuItem menuItem1 = new JMenuItem("Connect");
	    menuItem1.addActionListener(new ConnectMenuItemListener());
	    menu.add(menuItem1);
	    
	    JMenuItem menuItem2 = new JMenuItem("Quit");
	    menuItem2.addActionListener(new QuitMenuItemListener());
	    menu.add(menuItem2);
	    frame.add(menuBar, BorderLayout.NORTH);
	    
	    JPanel messages = new JPanel();
	    messages.setLayout(new BoxLayout(messages, BoxLayout.PAGE_AXIS));
	    
	    msgArea = new JTextArea(20,25);

	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setViewportView(msgArea);
	    messages.add(scrollPane);
	    
	    
	    
	    
	    chatArea = new JTextArea(21,24);
	    //chatArea.setEnabled(false);;
	    DefaultCaret caretChat = (DefaultCaret) chatArea.getCaret();
	    caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	    JScrollPane scrollPaneChat = new JScrollPane();
	    scrollPaneChat.setViewportView(chatArea);
	    messages.add(scrollPaneChat);
	    
	    JPanel chat = new JPanel();
	    chat.setLayout(new FlowLayout());
	    chat.add(new JLabel("Message:"));
	    chatTypeArea = new JTextField();
	    chatTypeArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	    chatTypeArea.addActionListener(new EnterListener());
	    chatTypeArea.setPreferredSize( new Dimension( 200, 24 ) );
	    chat.add(chatTypeArea);
	    messages.add(chat);
	    
	    
	    
	    
	    
	    
	    frame.add(messages, BorderLayout.EAST);
	    
	    JPanel buttonPanel = new JPanel();
	    playButton = new JButton("Play");
	    playButton.addActionListener(new PlayButtonListener());
	    passButton = new JButton("Pass");
	    passButton.addActionListener(new PassButtonListener());
	    buttonPanel.add(playButton);
	    buttonPanel.add(passButton);
	    frame.add(buttonPanel, BorderLayout.SOUTH);
	    
	    frame.setVisible(true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	
	
	
	
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer)
	{
		// verifying and setting the active player of the game
		if (activePlayer < 0 || activePlayer > NUM_OF_PLAYERS-1) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected() {
		ArrayList<Integer> selectedList = new ArrayList<Integer>();
		
		for (int i = 0; i <= selected.length-1; i++) {
			if (selected[i]) {
				selectedList.add(i);
			}
		}
		
		
		if (selectedList.size() >= 1) {
			int[] arr = new int[selectedList.size()];
			
			
			for(int i=0; i <= selectedList.size()-1; i++)
			   {arr[i] = selectedList.get(i);}
			
			return arr;
		} 
		else 
		{
			return null;
		}
		
	}
	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected() {
		this.selected = new boolean[MAX_CARD_NUM];
		this.repaint();
	}
	
	/**
	 * Prints the cards of CardList in a pretty way into the message area
	 * @param cards cards list of type CardList to be printed
	 */
	public void printCards(CardList cards) {

		if (cards.size() >= 1) 
		{
			for (int i = 0; i <= cards.size()-1; i++) 
			{
				String string = "[" + cards.getCard(i) + "]";

				if (i % MAX_CARD_NUM != 0) {
					string = " " + string;
				}
				this.printMsg(string);
			}
		} else 
		{
			this.printMsg("[Empty]");
		}
	}

	/**
	 * This method will repaint the GUI.
	 */
	public void repaint() {
		frame.repaint();
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Prints the specified string to the chat message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat message area of the card game
	 *            table
	 */
	public void printChatMsg(String msg) {
		chatArea.append(msg+"\n");
	}
	
	/**
	 * Clears the message area of the card game table.
	 */
	public void clearChatMsgArea() {
		chatArea.setText("");
	}



	/**
	 * This method will reset the GUI.
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	public void restart() {
		
		BigTwoDeck deck = new BigTwoDeck();
		deck.initialize();
		deck.shuffle();
		game.start(deck);
	}

	/**
	 * Enables user interactions.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		clickable = true;
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		clickable = false;
		
	}
	
	/**
	 * an inner class that extends the JPanel class and implements the MouseListener interface. Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 * @author manik
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * BigTwoPanel default constructor which adds the Mouse Listener and sets background of the card table.
		 */
		BigTwoPanel(){
			this.addMouseListener(this);
	        setBackground(new Color(0,0,0)); //black background
		}

		/**
		 * Draws the avatars, text and cards on card table
		 * @param g Provided by system to allow drawing
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bgImage,0,0,this);
			
			
			g.setColor(Color.WHITE);
			for(int i=1;i<=NUM_OF_PLAYERS;i++) {
			g.drawLine(0, 130*i, 900, 130*i);
			}
			
			for (int i = 0; i < game.getNumOfPlayers(); i++) {
				if (i == game.getPlayerID()) {
					if (i == game.getCurrentIdx()) {
						g.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
						g.setColor(Color.CYAN);
					} else {
						g.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
						g.setColor(Color.YELLOW);
					}
					g.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
					g.drawString(game.getPlayerList().get(i).getName()+" (You)",15,15+i*130);
					g.setColor(Color.WHITE);
				} else if (i == game.getCurrentIdx()) {
					g.setColor(Color.CYAN);
					g.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
					g.drawString(game.getPlayerList().get(i).getName(),15,15+i*130);
					g.setColor(Color.WHITE);
				} else {
					g.setFont(new Font("TimesNewRoman", Font.BOLD, 12));
					g.drawString(game.getPlayerList().get(i).getName(),15,15+i*130);
				}
				g.drawImage(avatars[i],5,20+i*130,this);
				for (int j = 0; j <= game.getPlayerList().get(i).getNumOfCards()-1; j++) {
					if (i == game.getPlayerID()) {
						int s = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
						int r = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
						if (selected[j] == true) {
							g.drawImage(cardImages[s][r],80+j*cardImages[0][0].getWidth(this)/2,10+i*130-10,this);
						} else {
							g.drawImage(cardImages[s][r],80+j*cardImages[0][0].getWidth(this)/2,10+i*130,this);
						}
					} else {
						g.drawImage(cardBackImage,80+j*cardImages[0][0].getWidth(this)/2,10+i*130,this);
					}
				}
			}
			
			if (game.getHandsOnTable().size()-1 > -1) {
				Hand lasthand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				g.drawString("Played by "+lasthand.getPlayer().getName(), 5, 10+4*130 - 10);
				for (int i = 0; i < lasthand.size();i++) {
					int s = lasthand.getCard(i).getSuit();
					int r = lasthand.getCard(i).getRank();
					g.drawImage(cardImages[s][r],5+i*20,10+4*130,this);
				}
			}
		}
		/**
		 * Defines what happens when mouse is clicked on the card table. Only allows clicks on cards of active player. Once cards are selected, the JPanel is repainted to reflect changes.
		 * @param e Mouse event created when Mouse Clicked
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (clickable && activePlayer == ((BigTwoClient) game).getCurrentIdx()) {
				int width = cardImages[0][0].getWidth(this);
				int height = cardImages[0][0].getHeight(this);
				int numberOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();
				
				int mouseX=e.getX();
				int mouseY=e.getY();
				
				int minX = 80;
				int minY = 10+activePlayer*130-10;
				
				int maxX = 80+(width/2)*numberOfCards+width;
				int maxY = 10+activePlayer*130+height;
			
				try {
					if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {	
						int card = (int)Math.ceil((mouseX-80)/(width/2));
						card = card / numberOfCards > 0 ? numberOfCards - 1 : card;
						
						if (selected[card]) {
							if (mouseY > (maxY - 10) && mouseX < (80+(width/2)*card + width/2) && selected[card-1] == false) {
								if (card != 0) {
									card -= 1;
								}
								selected[card] = true;
								this.repaint();
							} else if (mouseY < (maxY - 10)){
								selected[card] = false;
								this.repaint();
							}
						} else if (mouseY > (minY + 10)){
							selected[card] = true;
							this.repaint();
						} else if (selected[card - 1] && mouseX < (80+(width/2)*card + width/2)) {
							selected[card-1] = false;
							this.repaint();
						}
						this.repaint();
					}
				} catch (Exception e1) {}
			}
		}
		
		/**
		 * mouse pressed event
		 * @param e Mouse event created when Mouse Pressed
		 */
		@Override
		public void mousePressed(MouseEvent e) {
		}
		/**
		 * mouse released event
		 * @param e Mouse event created when Mouse Released
		 */
		@Override
		public void mouseReleased(MouseEvent e) {	
		}
		/**
		 * mouse entered event
		 * @param e Mouse event created when Mouse Clicked
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		/**
		 * mouse eexited event
		 * @param e Mouse event created when Mouse exited
		 */
		@Override
		public void mouseExited(MouseEvent e) {	
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the Play button. When the Play button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * @author manik
	 */
	class PlayButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
				if (getSelected() != null) {
					((BigTwoClient)game).makeMove(activePlayer, getSelected());
				} else {
					printMsg("No Cards Selected\n");
				}

		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the Ã¢â‚¬Å“PassÃ¢â‚¬ï¿½ button. When the Ã¢â‚¬Å“PassÃ¢â‚¬ï¿½ button is clicked, it calls the makeMove() method of CardGame object to make a move.
	 * @author manik
	 */
	class PassButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		
				((BigTwoClient)game).makeMove(activePlayer, null);
			
		}
	}
	
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the â€œSendâ€� button.
	 * @author manik
	 */
	class EnterListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,-1,chatTypeArea.getText());
			chatTypeArea.setText("");
			game.sendMessage(message);
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the â€œConnectâ€� menu item.
	 * @author manik
	 */
	class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(game.getPlayerID() == -1) // checks if the player is connected or not 
			{
				game.makeConnection();
			}
			else
			{
				printMsg("You are already Connected!");
			}
		}
		
	}
	
	/**
	 * an inner class that implements the ActionListener interface. Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the Ã¢â‚¬Å“QuitÃ¢â‚¬ï¿½ menu item. When the Ã¢â‚¬Å“QuitÃ¢â‚¬ï¿½ menu item is selected, it terminates application.
	 * @author manik
	 */
	class QuitMenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}


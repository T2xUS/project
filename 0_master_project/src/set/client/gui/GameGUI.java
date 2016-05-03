/*
 *  GameGUI.java
 *  
 */

package set.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
//import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;

import set.client.GameClient;

public class GameGUI extends JFrame {

	/********************/
	/** GameGUI fields **/
	/********************/
	
	// Client associated with this GUI
	private GameClient client;
	
	// States associated with this GUI
	public GameMode gameMode = GameMode.NONE;
	public GameState gameState = GameState.ROOM;
	public enum GameMode {
		NONE, SOLO, HOST, PLAYER, GAME_OVER
	}
	public enum GameState {
		ROOM, GAME
	}
	
	// Game data
	private int numCardsSelected;
	public int score;
	public int setsFound;
	public int penalties;
	public int cardsInDeck;

	// Panels
	private JPanel mainPanel;
	private JPanel playerPanel;
	private JPanel infoPanel;
	private JPanel boardPanel;
	private JPanel setPanel;
	private JPanel chatPanel;
	private JPanel optionPanel;
	
	// Labels
	public JLabel statusLabel;
	public JLabel scoreLabel;
	public JLabel deckLabel;
	
	// Buttons
	private JButton submitButton;
	private JButton startButton;
	private JButton exitButton;
	private JButton kickButton;
	private JToggleButton gameShark;
	private ArrayList<JToggleButton> cardArray;
	
	// Player
	private JScrollPane playerScrollPane;
	private JList playerList;
	private DefaultListModel<String> usernameList;

	/*************************/
	/** GameGUI Constructor **/
	/*************************/

	public GameGUI(GameClient client) {
		super("SET: The Online Version");
		this.client = client;
		PrepareGUI();
		PrepareLayout();
		PreparePlayerList();
	}
	
	// debug
	public GameGUI() {
		super("SET: The Online Version");
		PrepareGUI();
		PrepareLayout();
		PreparePlayerList();
		//GameRoomReset();
		//GameRoomSoloMode(); // test in pairs
		//GameSoloMode();
		//GameRoomHostMode();
		//GameHostMode();
		//GameRoomPlayerMode();
		//GamePlayerMode();
		
		//numCardsSelected = 0;
		//gameShark.setVisible(false);
	}
	
	/*********************/
	/** GameGUI classes **/
	/*********************/
	
	// GridLayout with overwritten layoutContainer
	public class AnotherGridLayout extends GridLayout {
		
		// AnotherGridLayout constructor
		public AnotherGridLayout(int rows, int cols) {
			super(rows, cols);
		}

		// Overwrite layoutContainer to orient grid from top-down, then left-right
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				Insets insets = parent.getInsets();
				int ncomponents = parent.getComponentCount();
				int nrows = getRows();
				int hgap = getHgap();
				int vgap = getVgap();
				int ncols = getColumns();
				int width = parent.getWidth();
				int height = parent.getHeight();

				if (ncomponents == 0) {
					return;
				}
				if (nrows > 0) {
					ncols = (ncomponents + nrows - 1) / nrows;
				} else {
					nrows = (ncomponents + ncols - 1) / ncols;
				}
				int w = width - (insets.left + insets.right);
				int h = height - (insets.top + insets.bottom);
				w = (w - (ncols - 1) * hgap) / ncols;
				h = (h - (nrows - 1) * vgap) / nrows;

				for (int r = 0, y = insets.top; r < nrows; r++, y += h + vgap) {
					for (int c = 0, x = insets.left; c < ncols; c++, x += w + hgap) {
						int i = c * nrows + r;
						if (i < ncomponents) {
							parent.getComponent(i).setBounds(x, y, w, h);
						}
					}
				}
			}
		}
	}
	
	/*******************/
	/** GameGUI modes **/
	/*******************/
	
	// Reset game settings/stats
	public void GameRoomReset() {
		
		numCardsSelected = 0;
		score = 0;
		setsFound = 0;
		penalties = 0;
		cardsInDeck = 81;
		
		statusLabel.setText("");
		scoreLabel.setText("Score: " + score + "     " + 
							"Sets found: " + setsFound + "     " +
							"Penalties: " + penalties);
		deckLabel.setText("Cards in deck: " + cardsInDeck);
		
		ResetBoard();
		DeselectAll();
		DisableSelection();
		submitButton.setEnabled(false);
		exitButton.setEnabled(true);
		gameShark.setVisible(false);
		gameShark.setEnabled(false);
		gameMode = GameMode.NONE;
		gameState = GameState.ROOM;
	}
	
	// Switch to this mode when in solitaire game room
	// Start button, no kick button
	public void GameRoomSoloMode() {
		GameRoomReset();
		startButton.setVisible(true);
		startButton.setEnabled(true);
		kickButton.setVisible(false);
		kickButton.setEnabled(false);
		gameShark.setVisible(true);
		gameMode = GameMode.SOLO;
	}
	
	// Switch to this mode when in game room as host
	// Start button, kick button
	public void GameRoomHostMode() {
		GameRoomReset();
		startButton.setVisible(true);
		startButton.setEnabled(true);
		kickButton.setVisible(true);
		kickButton.setEnabled(true);
		gameMode = GameMode.HOST;
	}
	
	// Switch to this mode when in multiplayer game room
	// No start button, no kick button
	public void GameRoomPlayerMode() {
		GameRoomReset();
		startButton.setVisible(false);
		startButton.setEnabled(false);
		kickButton.setVisible(false);
		kickButton.setEnabled(false);
		gameMode = GameMode.PLAYER;
	}

	// Switch to this mode when in solitaire game
	public void GameSoloMode() {
		EnableSelection();
		startButton.setEnabled(false);
		gameState = GameState.GAME;
	}
	
	// Switch to this mode when in multiplayer game as host
	public void GameHostMode() {
		EnableSelection();
		startButton.setEnabled(false);
		kickButton.setEnabled(false);
		gameState = GameState.GAME;
	}
	
	// Switch to this mode when in multiplayer game as player
	public void GamePlayerMode() {
		EnableSelection();
		gameState = GameState.GAME;
	}
	
	// Switch to this mode when set has been found
	public void GameFreeze() {
		numCardsSelected = 0;
		DeselectAll();
		DisableSelection();
		// highlight set
		submitButton.setEnabled(false);
		gameShark.setEnabled(false);
	}
	
	// Unfreeze game
	public void GameUnfreeze() {
		EnableSelection();
		// unhighlight set
		gameShark.setEnabled(true);
	}
	
	// Switch to this mode when game is over
	public void GameOver() {
		numCardsSelected = 0;
		DeselectAll();
		DisableSelection();
		submitButton.setEnabled(false);
		startButton.setEnabled(false);
		gameShark.setEnabled(false);
		gameMode = GameMode.GAME_OVER;
	}
	
	// Switch to this mode if you want to cheat
	public void GameCheatMode() {
		gameShark.setVisible(true);
		gameShark.setEnabled(true);
	}

	/*********************/
	/** GameGUI methods **/
	/*********************/
	
	/********************/
	/** Layout-related **/
	/********************/
	
	// Load GUI, prepare panels
	private void PrepareGUI() {
		
		// Frame
		setSize(1080, 768);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});	
		
		// Main panel
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		mainPanel.setLayout(new GridLayout(3, 1));
		
		// Player panel
		playerPanel = new JPanel();
		playerPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		playerPanel.setLayout(new GridLayout(5, 1));
		
		// Labels
		statusLabel = new JLabel("", JLabel.CENTER);
		scoreLabel = new JLabel("", JLabel.CENTER);
		deckLabel = new JLabel("", JLabel.CENTER);
		//statusLabel.setSize(350, 100);

		// Information panel, provides messages
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		infoPanel.setLayout(new GridLayout(3, 1));
		infoPanel.setSize(200, 300);
		infoPanel.add(scoreLabel);
		infoPanel.add(deckLabel);
		infoPanel.add(statusLabel);
		
		// Board panel, contains game board
		boardPanel = new JPanel();
		boardPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		//boardPanel.setLayout(new FlowLayout());
		// boardPanel.setBackground(Color.WHITE);
		boardPanel.setSize(300, 300);
		boardPanel.setLayout(new AnotherGridLayout(3, 0));
		
		// Set button panel, contains BIG SET BUTTON
		setPanel = new JPanel();
		setPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		setPanel.setLayout(new GridLayout(1, 1, 1, 1));
		setPanel.setSize(100, 300);
		
		// Chat panel
		chatPanel = new JPanel();
		chatPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		chatPanel.setLayout(new FlowLayout());
		chatPanel.setSize(100, 300);
		
		// Option panel, contains all player options
		optionPanel = new JPanel();
		optionPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		optionPanel.setLayout(new FlowLayout());
		optionPanel.setSize(20, 300);
		optionPanel.setLayout(new GridLayout(1, 4, 50, 1));

		// Add components to main panel
		mainPanel.add(boardPanel);
		mainPanel.add(setPanel);
		mainPanel.add(chatPanel);
		
		// Add components to main frame
		add(infoPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.WEST);
		add(optionPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	// Load content of GUI
	private void PrepareLayout() {
		
		//headerLabel.setText("SET: The Online Version");
		statusLabel.setText("");
		scoreLabel.setText("Score:     Sets found:     Penalties:");
		deckLabel.setText("Cards in deck:");
		
		// Add option buttons
		PrepareOptions();
		
		// Initialize card array
		cardArray = new ArrayList<JToggleButton>();
		UIManager.put("ToggleButton.select", Color.WHITE);
		
		// Add initial 12 cards to board
		PrepareCards(12);
		
		//cardArray.get(0).setHorizontalTextPosition(SwingConstants.LEFT);
		//setVisible(true);
	}
	
	// Add scroll pane for player listings
	public void PreparePlayerList() {

		usernameList = new DefaultListModel<String>();
		playerList = new JList<String>(usernameList);

		// Set up scroll pane and add to main panel
		playerScrollPane = new JScrollPane(playerList);
		playerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//JScrollBar playerScrollBar = playerScrollPane.getVerticalScrollBar();
		//playerScrollBar.setPreferredSize(new Dimension(10, 0));
		
		playerPanel.add(playerScrollPane);
	}
	
	/********************/
	/** Button-related **/
	/********************/
	
	// Add option buttons to board
	public void PrepareOptions() {
		
		// Add submit button
		submitButton = new JButton("Submit Set");
		setPanel.add(submitButton);

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// parse through board and check for selected cards
				ArrayList<Integer> selectedCards = new ArrayList<Integer>();
				for(int i = 0; i < cardArray.size(); i++) {
					JToggleButton card = cardArray.get(i);
					if(card.isSelected()) {
						//System.out.println("Card " + i + " is selected.");
						selectedCards.add(i);
					}
				}
				
				// send packet containing card positions
				int card1Pos = selectedCards.get(0).intValue();
				int card2Pos = selectedCards.get(1).intValue();
				int card3Pos = selectedCards.get(2).intValue();
				
				switch(gameMode) {
				case SOLO:
					client.sendPacket.GameRequestSubmitSetSolo(card1Pos, card2Pos, card3Pos);
					break;
				case HOST:
					client.sendPacket.GameRequestSubmitSetMult(card1Pos, card2Pos, card3Pos);
					break;
				case PLAYER:
					client.sendPacket.GameRequestSubmitSetMult(card1Pos, card2Pos, card3Pos);
					break;
				case NONE:
					break;
				}
				
				System.out.println("GameGUI: Card 1: " + (card1Pos + 1));
				System.out.println("GameGUI: Card 2: " + (card2Pos + 1));
				System.out.println("GameGUI: Card 3: " + (card3Pos + 1));
				System.out.println("GameGUI: Set submitted.");
				
				// Re-enable and deselect cards, disable submit button
				numCardsSelected = 0;
				selectedCards.clear();
				DeselectAll();
				EnableSelection();
				submitButton.setEnabled(false);
			}
		});
		
		// Add start button
		startButton = new JButton("Start Game");
		optionPanel.add(startButton);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(gameMode) {
				case SOLO:
					client.sendPacket.GameRoomRequestStartSolo();
					break;
				case HOST:
					client.sendPacket.GameRoomRequestStartHost();
					break;
				case PLAYER:
					break;
				case NONE:
					break;
				}
			}
		});
		
		// Add kick button
		kickButton = new JButton("Kick Player");
		optionPanel.add(kickButton);
		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		// Add game shark
		gameShark = new JToggleButton("Game Shark");
		optionPanel.add(gameShark);
		gameShark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gameShark.isSelected()) {
					client.sendPacket.GameRequestCheat();
				} else {
					DehighlightSet();
				}
			}
		});
		
		// Add exit button
		exitButton = new JButton("Exit Game");
		optionPanel.add(exitButton);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExitGamePopUp();
			}
		});
		
		// Set visibilities
		submitButton.setVisible(true);
		startButton.setVisible(true);
		kickButton.setVisible(true);
		exitButton.setVisible(true);
		gameShark.setVisible(false);
		
		// Set accessibilities
		submitButton.setEnabled(false);
		startButton.setEnabled(false);
		kickButton.setEnabled(false);
		exitButton.setEnabled(true);
		gameShark.setEnabled(false);
	}
	
	// Add cards to board
	public void PrepareCards(int numCards) {
		
		int currBoardSize = cardArray.size();
		final LineBorder defaultBorder = new LineBorder(Color.BLACK, 1); // change final
		final LineBorder selectedBorder = new LineBorder(Color.ORANGE, 3);
		
		// Set properties for cards
		for(int i = 0; i < numCards; i++) {
			
			cardArray.add(new JToggleButton());
			//System.out.println(cardArray.size());
			final JToggleButton card = cardArray.get(currBoardSize + i); // why final?
			
			card.setPreferredSize(new Dimension(95, 65));
			card.setBackground(Color.WHITE);
			card.setBorder(defaultBorder);
			
			boardPanel.add(card);
			
			card.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					// select/deselect cards and add/remove them to list of selected cards
					if (card.isSelected()) {
						card.setBorder(selectedBorder);
						numCardsSelected++;
					} else {
						card.setBorder(defaultBorder);
						numCardsSelected--;;
					}
					System.out.println("GameGUI: Number of selected cards: " + numCardsSelected);
					
					// disable card selection and enable submit when 3 cards are selected
					if(numCardsSelected == 3) {
						DisableSelection();
						submitButton.setEnabled(true);
					} else if(numCardsSelected < 3) {
						EnableSelection();
						submitButton.setEnabled(false);
					} else if(numCardsSelected > 3) {
						System.err.println("GameGUI: YOU CHEATED.");
						System.exit(-1);
					}
				}
			});
		}
	}
	
	/***********************/
	/** Card icon-related **/
	/***********************/
	
	// Re-enable selection of cards (when <3 cards are selected)
	public void EnableSelection() {
		for(int i = 0; i < cardArray.size(); i++) {
			JToggleButton card = cardArray.get(i);
			card.setEnabled(true);
		}
	}
	
	// Disable selection of more cards (when 3 cards are selected)
	public void DisableSelection() {
		for(int i = 0; i < cardArray.size(); i++) {
			JToggleButton card = cardArray.get(i);
			if(!card.isSelected()) {
				card.setEnabled(false);
			}
		}
	}
	
	// Deselect all cards
	public void DeselectAll() {
		for(int i = 0; i < cardArray.size(); i++) {
			JToggleButton card = cardArray.get(i);
			card.setSelected(false);
			card.setBorder(new LineBorder(Color.BLACK, 1));
		}
	}	
	
	// Update GUI after initial deal
	public void InitialDeal(String cardString) {
		for(int i = 0; i < 12; i++) {
			JToggleButton card = cardArray.get(i);
			String cardID = cardString.substring(4*i,4*i+4);
			String cardIconPath = "src/set/client/gui/Cards/" + cardID + ".gif";
			//System.out.println("GameGUI: " + cardIconPath);
			card.setIcon(new ImageIcon(cardIconPath));
		}
	}
	
	// Update GUI after replacing cards after a set has been found
	public void ReplaceCards(int card1Pos, int card2Pos, int card3Pos, String cardString) {
		JToggleButton card1 = cardArray.get(card1Pos);
		JToggleButton card2 = cardArray.get(card2Pos);
		JToggleButton card3 = cardArray.get(card3Pos);
		String card1ID = cardString.substring(0, 4);
		String card2ID = cardString.substring(4, 8);
		String card3ID = cardString.substring(8, 12);
		String card1IconPath = "src/set/client/gui/Cards/" + card1ID + ".gif";
		String card2IconPath = "src/set/client/gui/Cards/" + card2ID + ".gif";
		String card3IconPath = "src/set/client/gui/Cards/" + card3ID + ".gif";
		card1.setIcon(new ImageIcon(card1IconPath));
		card2.setIcon(new ImageIcon(card2IconPath));
		card3.setIcon(new ImageIcon(card3IconPath));
	}

	// Update GUI after adding cards after no possible sets on board
	public void AddCards(int boardSize, String cardString) {
		PrepareCards(3);
		for (int i = boardSize; i < boardSize + 3; i++) {
			int j = i - boardSize;
			JToggleButton card = cardArray.get(i);
			String cardID = cardString.substring(4*j, 4*j+4);
			String cardIconPath = "src/set/client/gui/Cards/" + cardID + ".gif";
			card.setIcon(new ImageIcon(cardIconPath));
		}
	}

	// Update GUI with highlighted set, cheating
	public void HighlightSet(int card1Pos, int card2Pos, int card3Pos) {
		LineBorder cheatBorder = new LineBorder(Color.RED, 3);
		JToggleButton card1 = cardArray.get(card1Pos);
		JToggleButton card2 = cardArray.get(card2Pos);
		JToggleButton card3 = cardArray.get(card3Pos);
		card1.setBorder(cheatBorder);
		card2.setBorder(cheatBorder);
		card3.setBorder(cheatBorder);
	}
	
	// De-highlight set, cheating
	public void DehighlightSet() {
		for(int i = 0; i < cardArray.size(); i++) {
			JToggleButton card = cardArray.get(i);
			if(card.isSelected()) {
				card.setBorder(new LineBorder(Color.ORANGE, 3));
			} else {
				card.setBorder(new LineBorder(Color.BLACK, 1));
			}
		}
	}
	
	// Reset board so that icons are empty, 12 cards
	public void ResetBoard() {
		cardArray.clear();
		boardPanel.removeAll();
		boardPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		boardPanel.setLayout(new AnotherGridLayout(3, 0));
		PrepareCards(12);
	}
	
	/*************/
	/** Pop-ups **/
	/*************/
	
	public void ExitGamePopUp() {

		String message = "Are you sure you want to quit?";
		String title = "Exit Game";
		int optionType = JOptionPane.YES_NO_OPTION;
		int messageType = JOptionPane.PLAIN_MESSAGE;
		int selection = JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
		
		if(selection == JOptionPane.YES_OPTION) {
			if(gameState == GameState.ROOM) {
				switch(gameMode) {
				case SOLO:
					client.sendPacket.GameRoomRequestExitSolo();
					break;
				case HOST:
					client.sendPacket.GameRoomRequestExitHost();
					break;
				case PLAYER:
					client.sendPacket.GameRoomRequestExitPlayer();
					break;
				case GAME_OVER:
					client.sendPacket.GameRoomRequestExitSolo();
					break;
				case NONE:
					break;
				}
			} else if(gameState == GameState.GAME) {
				switch(gameMode) {
				case SOLO:
					client.sendPacket.GameRequestExitSolo();
					break;
				case HOST:
					client.sendPacket.GameRequestExitHost();
					break;
				case PLAYER:
					client.sendPacket.GameRequestExitPlayer();
					break;
				case GAME_OVER:
					client.sendPacket.GameRequestExitSolo();
					break;
				case NONE:
					break;
				}
			}
		}
	}
	
	/******************/
	/** GameGUI main **/
	/******************/

	public static void main(String[] args) {
		GameGUI demo = new GameGUI();
		demo.InitialDeal("123112223312213312331333131232231133121233223121");
	}
}
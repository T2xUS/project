/*
 *  LobbyGUI.java
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.UIManager;

import set.client.GameClient;

public class LobbyGUI extends JFrame {

	/*********************/
	/** LobbyGUI fields **/
	/*********************/

	// Client associated with this GUI
	private GameClient client;
	
	// Selected game in this GUI
	private String selectedGame;
	private String selectedGameName;
	private String selectedHostName;

	// Panels
	private JPanel mainPanel;
	private JPanel playerPanel;
	private JPanel infoPanel;
	private JPanel optionPanel;
	private JPanel chatPanel;
	
	// Labels
	private JLabel lobbyLabel;
	
	// Game list
	private JScrollPane playerScrollPane;
	private JScrollPane gameScrollPane;
	private JList playerList;
	private JList gameList;
	private DefaultListModel<String> gameIDList;
	private DefaultListModel<String> usernameList;

	// Buttons
	private JButton soloButton;
	private JButton createButton;
	private JButton joinButton;
	private JButton exitButton;
	// private JButton selectButton;
	// private JButton cancelButton;
	
	// Dialogs
	//private JOptionPane createDialog;
	//private JOptionPane exitDialog;

	/**************************/
	/** LobbyGUI constructor **/
	/**************************/

	public LobbyGUI(GameClient client) {
		super("SET: The Online Version");
		this.client = client;
		PrepareGUI();
		PrepareLayout();
		selectedHostName = "";
		//setVisible(true);
	}

	// Debug
	public LobbyGUI() {
		super("SET: The Online Version");
		PrepareGUI();
		PrepareLayout();
		setVisible(true);
		selectedHostName = "";
	}

	/**********************/
	/** LobbyGUI methods **/
	/**********************/

	/********************/
	/** Layout-related **/
	/********************/

	// Load GUI, prepare panels
	public void PrepareGUI() {

		// Frame
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		// Main panel, contains game listing
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		mainPanel.setLayout(new GridLayout(1, 1));
		// mainPanel.setSize(200, 300);
		
		// Player panel, contains player listing
		playerPanel = new JPanel();
		playerPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		playerPanel.setLayout(new GridLayout(1, 1));
		//playerPanel.setSize(100, 200);
		
		// Information panel
		infoPanel = new JPanel();
		infoPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		infoPanel.setLayout(new GridLayout(1, 1));

		// Option panel, contains all player options
		optionPanel = new JPanel();
		optionPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		optionPanel.setLayout(new GridLayout(4, 1));

		// Chat panel
		chatPanel = new JPanel();
		chatPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		chatPanel.setLayout(new GridLayout(1, 1));

		// Add panels to main frame
		add(mainPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.WEST);
		add(optionPanel, BorderLayout.EAST);
		add(infoPanel, BorderLayout.NORTH);
		add(chatPanel, BorderLayout.SOUTH);
	}

	// Load content
	public void PrepareLayout() {
		PrepareLabels();
		PrepareGameList();
		PreparePlayerList();
		PrepareOptions();
	}

	/*********************/
	/** Content-related **/
	/*********************/
	
	// Add scroll pane for game listings
	public void PrepareGameList() {
		
		// Set up game list for scroll pane
		//String gameIDList[] = { "Game 1", "Game 2", "Game 3" };
		gameIDList = new DefaultListModel<String>();
		gameList = new JList<String>(gameIDList);
		
		// Set up scroll pane and add to main panel
		gameScrollPane = new JScrollPane(gameList);
		//scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setBounds(50, 30, 300, 50);
		mainPanel.add(gameScrollPane);
		
		//gameIDList.addElement("Game X"); // testing what happens if you add after		
		
		// Add mouse listener for when game is clicked
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 1) {
					
					// highlight game in yellow
					System.out.println("LobbyGUI: ~~~~~~~~~~~~~~~~~~~~~~~~");
					
					selectedGame = (String) gameList.getSelectedValue();
					System.out.println("LobbyGUI: Game selected: " + selectedGame);
					
					if(selectedGame != null) {
					
						// Extract game name (can remove)
						selectedGameName = selectedGame.substring(
								selectedGame.indexOf("Room: ") + ("Room: ").length(), selectedGame.indexOf(", Host: "));
						System.out.println("LobbyGUI: Game name: " + selectedGameName);
						
						// Extract host name (only need this)
						selectedHostName = selectedGame.substring(
								selectedGame.indexOf(", Host: ") + (", Host: ").length());
						System.out.println("LobbyGUI: Host name: " + selectedHostName);
					}
				}
			}
		};
		gameList.addMouseListener(mouseListener);
	}
	
	// Add scroll pane for player listings
	public void PreparePlayerList() {

		usernameList = new DefaultListModel<String>();
		playerList = new JList<String>(usernameList);

		// Set up scroll pane and add to main panel
		playerScrollPane = new JScrollPane(playerList);
		playerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		playerPanel.add(playerScrollPane);
	}
	
	// Add labels
	public void PrepareLabels() {
		
		lobbyLabel = new JLabel("", JLabel.CENTER);
		infoPanel.add(lobbyLabel);
		
		lobbyLabel.setText("Welcome to the lobby.");
	}

	// Add options
	public void PrepareOptions() {

		soloButton = new JButton("Create Solitaire");
		createButton = new JButton("Create Game");
		joinButton = new JButton("Join Game");
		exitButton = new JButton("Exit Lobby");

		soloButton.setEnabled(true);
		createButton.setEnabled(true);
		joinButton.setEnabled(true);
		exitButton.setEnabled(true);

		optionPanel.add(soloButton);
		optionPanel.add(createButton);
		optionPanel.add(joinButton);
		optionPanel.add(exitButton);
		
		soloButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendPacket.LobbyRequestCreateSolo();
			}
		});
		
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateGamePopUp();
			}
		});
		
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!selectedHostName.equals("")) {
					client.sendPacket.LobbyRequestJoinGame(selectedHostName);
					selectedHostName = "";
				} else {
					JOptionPane.showMessageDialog(null, "Please select a game.",
							"Error", JOptionPane.WARNING_MESSAGE);
					selectedHostName = "";
				}
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				ExitLobbyPopUp();
			}
		});
	}
	
	/*************/
	/** Pop-ups **/
	/*************/
	
	public void CreateGamePopUp() {
		
		String message = "Choose your game name.";
		String title = "Create Game";
		int messageType = JOptionPane.PLAIN_MESSAGE;
		String gameName = JOptionPane.showInputDialog(null, message, title, messageType);

		boolean isAlphaNumSpace = gameName.matches("[A-Za-z0-9\\s]+");
		if(gameName != null && isAlphaNumSpace) {
			client.gameName = gameName;
			client.sendPacket.LobbyRequestCreateGame(gameName);
		} else if(!isAlphaNumSpace) {
			JOptionPane.showMessageDialog(null, "Invalid game name. Must be alphanumeric (including space).",
					"Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void ExitLobbyPopUp() {

		String message = "Are you sure you want to exit?";
		String title = "Exit Lobby";
		int optionType = JOptionPane.YES_NO_OPTION;
		int messageType = JOptionPane.PLAIN_MESSAGE;
		int selection = JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
		
		if(selection == JOptionPane.YES_OPTION) {
			//client.sendPacket.LobbyRequestExit();
			System.out.println("LobbyGUI: Exit YES");
		}
	}
	
	/**************************/
	/** Managing lobby lists **/
	/**************************/

	public void AddPlayer(String username) {
		usernameList.addElement(username);
	}

	public void RemovePlayer(String username) {
		usernameList.removeElement(username); // look into remove later
	}

	public void AddGame(String gameName, String username) {
		String gameID = "Room: " + gameName + ", Host: " + username;
		gameIDList.addElement(gameID);
	}

	public void RemoveGame(String gameName, String username) {
		String gameID = "Room: " + gameName + ", Host: " + username;
		gameIDList.removeElement(gameID);
	}

	/*******************/
	/** LobbyGUI main **/
	/*******************/

	public static void main(String[] args) {

		LobbyGUI demo = new LobbyGUI();
		
		// demo.ExitLobbyPopUp();
		// demo.CreateGamePopUp();
		
		demo.usernameList.addElement("Player 1");
		demo.usernameList.addElement("Player 2");
		demo.usernameList.addElement("Player 3");
		demo.usernameList.removeElement("Player 1");
		
		//demo.gameIDList.addElement("Room 1");
		//demo.gameIDList.addElement("Room 2");
		//demo.gameIDList.addElement("Room 3");
		//demo.gameIDList.removeElement("Room 2");
		
		for(int i = 0; i < 5; i++) {
			String gameID = "Room: Game " + i + ", Host: User " + i;
			demo.gameIDList.addElement(gameID);
		}
		
		//System.out.println("I love you poopypie. I love you too <3 <3 <3");
	}

}

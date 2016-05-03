package set.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import set.client.gui.*;
import set.packet.DataReader;


public class GameClient {
	
	/***********************/
	/** GameClient fields **/
	/***********************/
	
	private Socket clientSocket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	public ClientPacketGenerator sendPacket;
	private boolean on;
	
	// Characteristics/stats associated with this client
	// Note: Again, create Get and Set functions but ain't nobody got time for that
	public String username;
	public int userID;
	private int numWins;
	private int numTotalGames;
	private int score;
	private int highScore;
	
	// GUIs that belong to this client
	private LoginGUI loginGUI;
	public LobbyGUI lobbyGUI;
	public GameGUI gameGUI;
	
	// Game that the client are currently in
	public String gameName;
	
	/****************************/
	/** GameClient constructor **/
	/****************************/
	
	public GameClient(String serverAddress, int portNumber) {
		StartClient(serverAddress, portNumber);
		PrepareIO();
		LoadGUIs();
		sendPacket = new ClientPacketGenerator(toServer);
		on = true;
	}
	
	/*********************/
	/** GameClient main **/
	/*********************/

	public static void main(String args[]) throws Exception {

		//String serverAddress = (args.length == 0) ? "localhost" : args[1];
		String serverAddress = "192.168.1.59";
		int portNumber = 8901;
		GameClient client = new GameClient(serverAddress, portNumber);
		
		//client.sendPacket.LobbyRequestCreateSolo();
		//client.gameGUI.CheatMode();
		
		// Listen for packets
		while(client.on) {
			//add buffer?
			int availableBytes = client.fromServer.available();
			if(availableBytes > 0) {
				client.ParsePacket(client.fromServer);
			}
		}
		
		/*** Testing communication with server ***/
		/*
		GameGUI newGUI = new GameGUI();
		try {
			newClient.clientOutData.writeShort(0x00);
		} catch (IOException e) {
			System.out.println("GameClient: Error sending message.");
		}
		
		while(true) {
			if(newClient.clientInData.readShort() == 0x00) {
				int boardSize = newClient.clientInData.readInt();
				String cardString = newClient.clientInData.readUTF();
				//System.out.println("GameClient: Printing message: " + boardSize + cardString);
				newGUI.updateGUI(boardSize, cardString);
			}
		}
		*/
		
		/*
		try {
			
			clientOutData.writeShort(0x00);
			clientOutData.writeUTF("Jeffrey");
			clientOutData.writeUTF("Shih");
			clientOutData.flush();
			clientOutData.writeShort(0x01);
			clientOutData.writeUTF("supahotfiya7");
			clientOutData.writeUTF("123321");
			clientOutData.writeInt(999999);
			clientOutData.writeInt(69);
			clientOutData.flush();
			
		} catch (IOException e) {
			System.out.println("Client: Error sending message.");
		}
		*/
	}
	
	/************************/
	/** GameClient methods **/
	/************************/
	
	/********************/
	/** Client-related **/
	/********************/
	
	// Start the client by opening a new socket
	public void StartClient(String serverAddress, int portNumber) {
		try {
			clientSocket = new Socket(serverAddress, portNumber);
		} catch(IOException e){
			System.err.println("GameClient: Could not open client on port " + portNumber + ".");
        	System.exit(-1);
		}
	}

	// Retrieve socket's I/O streams
	public void PrepareIO() {
		try {
			fromServer = new DataInputStream(clientSocket.getInputStream());
			toServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch(IOException e){
			System.err.println("GameClient: Error opening I/O streams.");
		}
	}
	
	// Load client's GUIs
	public void LoadGUIs() {
		
		loginGUI = new LoginGUI(this);
		lobbyGUI = new LobbyGUI(this);
		gameGUI = new GameGUI(this);
		
		loginGUI.setVisible(false); // this is supposed to be true, false for now
		lobbyGUI.setVisible(true);
		gameGUI.setVisible(false);
	}
	
	/********************/
	/** Packet-related **/
	/********************/
	
	// Parse packets sent from server to client
	public void ParsePacket(DataInputStream packet) {
		
		// Initialize variables to be used
		int card1Pos = 0;
		int card2Pos = 0;
		int card3Pos = 0;
		int boardSize = 0;
		int userID = 0;
		
		String cardString = "";
		String boardString = "";
		String username = "";
		String gameName = "";

		// Read header and determine packet type
		DataReader dataReader = new DataReader();
		short header = dataReader.ReadShort(packet);

		switch (header) {
		
		/*** Player data ***/
		case -1:
			username = dataReader.ReadString(packet);
			userID = dataReader.ReadInt(packet);
			this.username = username;
			this.userID = userID;
			break;
		
		/*** State change ***/
		case 0: 

			int gameState = dataReader.ReadInt(packet);
			switch (gameState) {
			
			/*** Go to login ***/
			case 0:
				break;
				
			/*** Go to lobby from login ***/
			case 1:
				loginGUI.setVisible(false);
				lobbyGUI.setVisible(true);
				break;
				
			/*** Go to lobby from game ***/
			case 2:
				gameGUI.setVisible(false);
				gameGUI.GameRoomReset();
				lobbyGUI.setVisible(true);
				break;
			
			/*** Go to solitaire game room ***/
			case -2:
				lobbyGUI.setVisible(false);
				gameGUI.GameRoomSoloMode();
				gameGUI.setVisible(true);
				break;
			
			/*** Go to game room as host ***/
			case 3:
				lobbyGUI.setVisible(false);
				gameGUI.GameRoomHostMode();
				gameGUI.setVisible(true);
				break;
				
			/*** Go to game room as player ***/
			case 4:
				lobbyGUI.setVisible(false);
				gameGUI.GameRoomPlayerMode();
				gameGUI.setVisible(true);
				break;
			
			/*** Begin solitaire game ***/
			case -1:
				gameGUI.GameSoloMode();
				gameGUI.GameCheatMode();
				break;
				
			/*** Begin game as host ***/
			case 5:
				gameGUI.GameHostMode();
				gameGUI.GameCheatMode();
				break;

			/*** Begin game as player ***/
			case 6:
				gameGUI.GamePlayerMode();
				gameGUI.GameCheatMode();
				break;

			}
			break;

		/*** Update lobby information ***/
		case 1:
			
			int lobbyUpdateType = dataReader.ReadInt(packet);
			switch (lobbyUpdateType) {
			
			/*** Add player to lobby listing ***/
			case 1:
				username = dataReader.ReadString(packet);
				lobbyGUI.AddPlayer(username);
				break;
				
			/*** Remove player from lobby listing ***/
			case 2:
				username = dataReader.ReadString(packet);
				lobbyGUI.RemovePlayer(username);
				break;

			/*** Add game to lobby listing ***/
			case 3:
				gameName = dataReader.ReadString(packet);
				username = dataReader.ReadString(packet);
				lobbyGUI.AddGame(gameName, username);
				break;

			/*** Remove game from lobby listing ***/
			case 4:
				gameName = dataReader.ReadString(packet);
				username = dataReader.ReadString(packet);
				lobbyGUI.RemoveGame(gameName, username);
				break;
				
			}
			break;
			
		/*** Update cards ***/
		case 2:
			
			int cardUpdateType = dataReader.ReadInt(packet);
			
			switch(cardUpdateType) {
			
			/*** Initial deal ***/
			case 0:
				boardString = dataReader.ReadString(packet);
				gameGUI.InitialDeal(boardString);
				gameGUI.scoreLabel.setText("Score: " + gameGUI.score + "     " +
						"Sets found: " + gameGUI.setsFound + "     " +
						"Penalties: " + gameGUI.penalties);
				gameGUI.deckLabel.setText("Cards in deck: " + (gameGUI.cardsInDeck -= 12));
				//System.out.println("GameClient: " + boardString);
				break;
				
			/*** Replace cards ***/
			case 1:
				card1Pos = dataReader.ReadInt(packet);
				card2Pos = dataReader.ReadInt(packet);
				card3Pos = dataReader.ReadInt(packet);
				cardString = dataReader.ReadString(packet);
				gameGUI.ReplaceCards(card1Pos, card2Pos, card3Pos, cardString);
				if(gameGUI.cardsInDeck > 0) {
					gameGUI.deckLabel.setText("Cards in deck: " + (gameGUI.cardsInDeck -= 3));
				}
				break;

			/*** Add cards ***/
			case 2:
				boardSize = dataReader.ReadInt(packet);
				cardString = dataReader.ReadString(packet);
				gameGUI.AddCards(boardSize, cardString);
				gameGUI.deckLabel.setText("Cards in deck: " + (gameGUI.cardsInDeck -= 3));
				break;
				
			/*** Cheat ***/
			case 3:
				card1Pos = dataReader.ReadInt(packet);
				card2Pos = dataReader.ReadInt(packet);
				card3Pos = dataReader.ReadInt(packet);
				gameGUI.HighlightSet(card1Pos, card2Pos, card3Pos);
				break;
				
			}
			break;
			
		/*** Game room message ***/
		case 4:
			
			int gameRoomMessage = dataReader.ReadInt(packet);
			
			switch (gameRoomMessage) {
			case 0:
				gameGUI.statusLabel.setText("Not enough players to start a game. Need at least 2.");
				break;
			
			}
			break;

		/*** Game message ***/
		case 3:

			int gameMessage = dataReader.ReadInt(packet);

			switch (gameMessage) {

			/*** You found set ***/
			case 0:
				gameGUI.GameFreeze();
				gameGUI.statusLabel.setText("You found a Set! Score +2!");
				gameGUI.scoreLabel.setText("Score: " + (gameGUI.score += 2) + "     " +
											"Sets found: " + (gameGUI.setsFound += 1) + "     " +
											"Penalties: " + gameGUI.penalties);
				// test delay
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameGUI.statusLabel.setText("");
				gameGUI.GameUnfreeze();
				break;
			
			/*** Other player found set ***/
			case 1:
				username = dataReader.ReadString(packet);
				gameGUI.GameFreeze();
				gameGUI.statusLabel.setText(username + " has found a Set!");
				// test delay
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				gameGUI.statusLabel.setText("");
				gameGUI.GameUnfreeze();
				break;
			
			/*** You submitted invalid set ***/
			case 2:
				System.out.println("GameGUI: Invalid Set");
				gameGUI.statusLabel.setText("Invalid Set submission. Score penalty -1.");
				gameGUI.scoreLabel.setText("Score: " + (gameGUI.score -= 1) + "     " +
											"Sets found: " + gameGUI.setsFound + "     " +
											"Penalties: " + (gameGUI.penalties += 1));
				break;
				
			/*** Solitaire game over ***/
			case -1:
				gameGUI.GameOver();
				gameGUI.statusLabel.setText("Game over. No more possible Sets.");
				break;
				
			/*** Game over - You won ***/
			case 3:
				gameGUI.GameOver();
				gameGUI.statusLabel.setText("Congratulations, you are victorious!!");
				break;

			/*** Game over - Other player won ***/
			case 4:
				username = dataReader.ReadString(packet);
				gameGUI.GameOver();
				gameGUI.statusLabel.setText(username + " has won the game.");
				break;

			}
			break;
			
		}
	}

}

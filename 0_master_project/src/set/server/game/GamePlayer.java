/*
 *  GamePlayer.java
 *  
 *  Class that manages data associated with a player.
 *  Contains client associated with player and other
 *  fields associated with player such as its username
 *  and score. Contains the game room or game that the
 *  player is currently in, and the player's state.
 *  Provides functions that changes the game's data structures
 *  whenever a player enters/leaves the lobby, a game room,
 *  
 *  
 */

package set.server.game;

import set.server.*;

public class GamePlayer {

	/*******************/
	/** Player fields **/
	/*******************/

	// Client socket associated with this player
	public GameServerThread client;
	
	// Characteristics associated with this player
	public String username;
	public int userID;
	public int numWins;
	public int numTotalGames;
	public int score;
	public int highScore;
	
	// Games associated with this player
	public GameRoom gameRoom;
	public Game game;
	
	// Player's current state
	// (in login, in lobby, in game room, or in game)
	public GameState gameState;
	public enum GameState {
		LOGIN, LOBBY, ROOM, GAME
	}

	/************************/
	/** Player constructor **/
	/************************/

	public GamePlayer(GameServerThread client) {
		this.client = client;
		gameState = GameState.LOGIN; // Player starts off at login
		gameRoom = null;
		game = null;
	}
	
	/********************/
	/** Player methods **/
	/********************/
	
	/*************/
	/** In-game **/
	/*************/
	
	public void SubmitSet(Card a, Card b, Card c) {
		
	}
	
	/*****************/
	/** Out-of-game **/
	/*****************/
	
	public void Login(String username, String password) {
	}
	
	public void Register(String username, String password) {
	}
	
	public void SaveStats() {
	}

	// Executes whenever player enters game lobby
	public void EnterGameLobby() {
		GameEngine.gameLobby.AddPlayer(client);
		gameRoom = null;
		game = null;
		gameState = GameState.LOBBY;
	}
	
	// Executes whenever player creates game room
	public void CreateGameRoom(String gameName) {
		// can only create room when you're in lobby
		if(gameState == GameState.LOBBY) {
			GameEngine.gameLobby.AddGameRoom(client, gameName);
			JoinGameRoom(username); // changes state
			gameRoom.host = client; // change host of game room to client
		}
	}
		
	// Executes whenever player joins game room
	public void JoinGameRoom(String hostName) {
		// can only join room when you're in lobby
		if(gameState == GameState.LOBBY) {
			gameRoom = GameEngine.gameLobby.GetGameRoom(hostName);
			// join if game exists and hasn't started
			if(gameRoom != null) {
				GameEngine.gameLobby.RemovePlayer(client);
				gameRoom.AddPlayer(client);
				gameState = GameState.ROOM;
			}
		}
	}
	
	// Executes whenever player exits current game room
	public void ExitGameRoom() {
		// can only exit room when you're in room
		if(gameState == GameState.ROOM) {
			if(gameRoom != null) {
				gameRoom.RemovePlayer(client);
				// delete game if player is host
				if(gameRoom.hostName.equals(username)) {
					GameEngine.gameLobby.RemoveGameRoom(username);
				}
			}
		}
	}
	
	// Executes whenever player joins game
	public void JoinGame(String hostName) {
		// can only join game when you're in room
		if(gameState == GameState.ROOM) {
			game = GameEngine.gameManager.GetGame(hostName);
			if(game != null) {
				game.AddPlayer(client);
				score = 0;
				gameState = GameState.GAME;
			}
		}
	}
	
	// Executes whenever player quits game
	public void ExitGame() {
		// can only exit game when you're in game
		if(gameState == GameState.GAME) {
			if(game != null) {
				game.RemovePlayer(client);
				// delete game if player is host
				if(game.hostName.equals(username)) {
					GameEngine.gameManager.RemoveGame(username);
				}
			}
		}

	}
	
}
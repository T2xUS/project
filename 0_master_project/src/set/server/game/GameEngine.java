/*
 *  GameEngine.java
 *  
 *  Class that contains the main components of
 *  the game engine, including the logic, lobby,
 *  and game manager. Components are static; the
 *  game engine will also be initialized statically
 *  to remain common to all games in the server.
 *  
 */

package set.server.game;

public class GameEngine {

	/***********************/
	/** GameEngine fields **/
	/***********************/

	// Note: These components are common to all game engines
	public static GameLogic gameLogic;
	public static GameLogin gameLogin;
	public static GameLobby gameLobby;
	public static GameManager gameManager;

	/****************************/
	/** GameEngine constructor **/
	/****************************/

	public GameEngine() {
		gameLogic = new GameLogic();
		gameLogin = new GameLogin();
		gameLobby = new GameLobby();
		gameManager = new GameManager();
	}
	
}

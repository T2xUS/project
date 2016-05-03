/*
 *  GameManager.java
 *  
 *  Class that manages active games. Provides
 *  functionality to add/remove games from list of
 *  running games.
 *  
 */

package set.server.game;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import set.server.GameServerThread;

public class GameManager {
	
	/************************/
	/** GameManager fields **/
	/************************/
	
	public List<Game> GamesInProgress = Collections.synchronizedList(new LinkedList<Game>());
	
	/*************************/
	/** GameManager methods **/
	/*************************/
	
	// Fetch active game associated with host's user ID
	public Game GetGame(String hostName) {
		synchronized(GamesInProgress) {
			for(Iterator<Game> gameItr = GamesInProgress.listIterator(); gameItr.hasNext();) {
				Game game = gameItr.next();
				if(game.hostName.equals(hostName)) {
					return game;
				}
			}
		}
		return null;
	}
	
	// Add game to list of currently active games
	public void AddGame(GameServerThread host, String gameName) {
		synchronized(GamesInProgress) {
			// check if host already has game
			if(GetGame(host.GetUsername()) == null) {
				Game game = new Game(host, gameName);
				GamesInProgress.add(game);
			}
		}
	}
	
	// Delete game from list of currently active games
	public void RemoveGame(String hostName) {
		synchronized(GamesInProgress) {
			for(Iterator<Game> gameItr = GamesInProgress.listIterator(); gameItr.hasNext();) {
				Game game = gameItr.next();
				if(game.hostName.equals(hostName)) {
					synchronized(game) {
						// remove all players from game
						for(Iterator<GameServerThread> clientItr = game.ClientsInGame.listIterator(); clientItr.hasNext();) {
							GameServerThread client = clientItr.next();
							client.RemovePlayerFromGame();
							// send packet?
						}
					}
					gameItr.remove();
				}
			}
		}
	}

}

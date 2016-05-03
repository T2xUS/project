/*
 *  GameRoom.java
 *  
 *  Class that manages data associated with a game
 *  room. Contains the game room's host, host user ID,
 *  and game name. Provides functionality to add/remove
 *  clients in a game room.
 *  
 */

package set.server.game;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import set.server.GameServerThread;

public class GameRoom {

	/*********************/
	/** GameRoom fields **/
	/*********************/
	
	public GameServerThread host;
	public int hostUID;
	public String hostName;
	public String gameName;
	
	
	public List<GameServerThread> ClientsInGameRoom = Collections.synchronizedList(new LinkedList<GameServerThread>());
	
	/**************************/
	/** GameRoom constructor **/
	/**************************/
	
	public GameRoom(GameServerThread host, String gameName) {
		hostUID = host.GetUserID();
		hostName = host.GetUsername();
		this.gameName = gameName;
	}
	
	// for solitaire
	public GameRoom() {
		
	}
	
	/**********************/
	/** GameRoom methods **/
	/**********************/
	
	// Starting game removes it from list of available games in lobby
	public void StartGame() {
		// Add game to list of active games in GameManager
		GameEngine.gameManager.AddGame(host, gameName);
		Game game = GameEngine.gameManager.GetGame(host.GetUsername());
		if(game != null) {
			// Move all clients from game room to game
			synchronized (ClientsInGameRoom) {
				for (Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator(); clientItr.hasNext();) {
					GameServerThread client = clientItr.next();
					client.AddPlayerToGame();
					clientItr.remove(); //ClientsInGameRoom.remove(client); // use itr or you'll get rekt
					System.out.println("Game Room: Remaining: " + ClientsInGameRoom.size());
				}
			}
		}
		// Remove this game room from lobby, prevents new players from joining
		GameEngine.gameLobby.RemoveGameRoom(hostName);
	}
	
	// Fetch player in game room associated with user ID
	public GameServerThread GetPlayer(String username) {
		synchronized (ClientsInGameRoom) {
			for(Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if (client.GetUsername().equals(username)) {
					return client;
				}
			}
		}
		return null;
	}
	
	// Add player to game room
	public void AddPlayer(GameServerThread client) {
		synchronized(ClientsInGameRoom) {
			// check if player is already in lobby 
			if(GetPlayer(client.GetUsername()) == null) {
				ClientsInGameRoom.add(client);
			}
		}
	}
	
	// Remove player from game room
	public void RemovePlayer(GameServerThread targetClient) {
		synchronized(ClientsInGameRoom) {
			for(Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if(client == targetClient) {
					clientItr.remove();
				}
			}
		}
	}
	
	/*****************************************************/
	/** Packet broadcasting to all clients in game room **/
	/*****************************************************/
	
	/*******************************/
	/** Starting multiplayer game **/
	/*******************************/
	
	public void BroadcastStartGame() {
		synchronized(ClientsInGameRoom) {
			for(Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator();
					clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if(client.GetUsername().equals(hostName)) {
					client.sendPacket.GoToGameHost();
				} else {
					client.sendPacket.GoToGamePlayer();
				}
			}
		}
	}
	
	/**********************/
	/** Updating players **/
	/**********************/
	
	public void BroadcastAddPlayer(String username) {
		synchronized(ClientsInGameRoom) {
			for(Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator();
					clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.GameRoomUpdateAddPlayer(username);
			}
		}
	}
	
	public void BroadcastRemovePlayer(String username) {
		synchronized(ClientsInGameRoom) {
			for(Iterator<GameServerThread> clientItr = ClientsInGameRoom.listIterator();
					clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.GameRoomUpdateRemovePlayer(username);
			}
		}
	}

}

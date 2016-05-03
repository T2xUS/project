/*
 *  GameLobby.java
 *  
 *  Class that manages data associated with the game's
 *  main lobby. Provides functionality to add/remove
 *  clients/game rooms in the lobby.
 *  
 */

package set.server.game;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import set.server.GameServerThread;


public class GameLobby {

	/**********************/
	/** GameLobby fields **/
	/**********************/

	// For future reference: This is set to public for easy coding, but should implement get function.
	public List<GameServerThread> ClientsInLobby = Collections.synchronizedList(new LinkedList<GameServerThread>());
	public List<GameRoom> GameRoomsInLobby = Collections.synchronizedList(new LinkedList<GameRoom>());

	/***********************/
	/** GameLobby methods **/
	/***********************/
	
	// Fetch player in lobby associated with user ID
	public GameServerThread GetPlayer(String username) {
		synchronized (ClientsInLobby) {
			for(Iterator<GameServerThread> clientItr = ClientsInLobby.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if (client.GetUsername().equals(username)) {
					return client;
				}
			}
		}
		return null;
	}
	
	// Add player to lobby
	public void AddPlayer(GameServerThread client) {
		synchronized(ClientsInLobby) {
			// check if player is already in lobby 
			if(GetPlayer(client.GetUsername()) == null) {
				ClientsInLobby.add(client);
			}
		}
	}
	
	// Remove player from lobby
	public void RemovePlayer(GameServerThread targetClient) {
		synchronized(ClientsInLobby) {
			for(Iterator<GameServerThread> clientItr = ClientsInLobby.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if(client == targetClient) { // use == for identicality
					//ClientsInLobby.remove(client);
					clientItr.remove();
				}
			}
		}
	}
	
	// Fetch game room in lobby listing associated with player
	public GameRoom GetGameRoom(String hostName) {
		synchronized(GameRoomsInLobby) {
			for(Iterator<GameRoom> gameRoomItr = GameRoomsInLobby.listIterator(); gameRoomItr.hasNext();) {
				GameRoom gameRoom = gameRoomItr.next();
				if(gameRoom.hostName.equals(hostName)) {
					return gameRoom;
				}
			}
		}
		return null;
	}

	// Add game room to lobby listing
	public void AddGameRoom(GameServerThread host, String gameName) {
		synchronized(GameRoomsInLobby) {
			// check if host already has room
			if(GetGameRoom(host.GetUsername()) == null) {
				GameRoom gameRoom = new GameRoom(host, gameName);
				GameRoomsInLobby.add(gameRoom);
			}
		}
	}

	// Remove game room from lobby listing
	public void RemoveGameRoom(String hostName) {
		synchronized(GameRoomsInLobby) {
			for(Iterator<GameRoom> gameRoomItr = GameRoomsInLobby.listIterator(); gameRoomItr.hasNext();) {
				GameRoom gameRoom = gameRoomItr.next();
				if(gameRoom.hostName.equals(hostName)) {
					synchronized(gameRoom) {
						for(Iterator<GameServerThread> clientItr = gameRoom.ClientsInGameRoom.listIterator(); clientItr.hasNext();) {
							GameServerThread client = clientItr.next();
							client.RemovePlayerFromGameRoom();
							// send packet to server saying client has left?
						}
					}
					gameRoomItr.remove();
				}
			}
		}
	}
	
	// Get number of players in lobby
	public int GetPlayerCount() {
		return ClientsInLobby.size();
	}
	
	// Get number of players in lobby
	public int GetGameRoomCount() {
		return GameRoomsInLobby.size();
	}

}

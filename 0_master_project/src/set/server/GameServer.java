/*
 *  GameServer.java
 *  
 *  Class that implements the game's server.
 *  Contains static fields that store itself
 *  and the engine to make them common to all
 *  processes in the game. Initializes server by
 *  creating a socket that listens for client
 *  connections to the IP address of the server and
 *  the specified port. Maintains a list of clients
 *  currently connected to the server and provides
 *  functionality and add/remove hem.
 *  
 */

package set.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import set.server.game.GameEngine;

public class GameServer {
	
	/******************************/
	/** GameServer static fields **/
	/******************************/
	
	// Note: The engine and server are common to all instances of the server
	public static GameServer gameServer;
	public static GameEngine gameEngine;
	
	private List<GameServerThread> ClientList = Collections.synchronizedList(new LinkedList<GameServerThread>());
	
	/***********************/
	/** GameServer fields **/
	/***********************/
	
	private ServerSocket serverSocket;
	private boolean ServerOn = true;
	
	/****************************/
	/** GameServer constructor **/
	/****************************/

	public GameServer(int portNumber) {
		gameServer = this;
		StartGameEngine();
		StartServer(portNumber);
		ServerListening();
		CloseServer();
	}
	
	/************************/
	/** GameServer methods **/
	/************************/
	
	// Initialize game engine
	public void StartGameEngine() {
		gameEngine = new GameEngine();
	}
	
	// Initialize server on port
	public void StartServer(int portNumber) {
		
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e){
			System.err.println("GameServer: Could not start server on port " + portNumber + ".");
        	System.exit(-1);
		}
		
		System.out.println("GameServer: Server is running on port " + portNumber + ".");
	}
	
	// Listen for clients
	public void ServerListening() {
		
		System.out.println("GameServer: ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("GameServer: Listening for clients...");
		
		while(ServerOn) {
			try {
				Socket clientSocket = serverSocket.accept(); // listen for connection, then accept it
				AddClient(clientSocket);
				System.out.println("GameServer: ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("GameServer: Accepted client at address " + clientSocket.getInetAddress());
				System.out.println("GameServer: Number of clients: " + GetClientCount());
			} catch(Exception e) {
				System.err.println("GameServer: Could not accept client.");
			}
		}
	}
	
	// Close server
	public void CloseServer() {
		
		try {
			serverSocket.close();
			System.out.println("GameServer: Server closed.");
		} catch(Exception e) {
			System.err.println("GameServer: Could not close server.");
		}
		
		System.exit(0);
	}
	
	// Force shutdown server
	public void Shutdown() {
		ServerOn = false;
	}
	
	/*********************************/
	/** Methods for client managing **/
	/*********************************/
	
	// Get client
	public GameServerThread GetClient(int userID) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if (client.GetUserID() == userID) {
					return client;
				}
			}
		}
		return null;
	}
	
	// Add client socket to server and start it
	public void AddClient(Socket cSocket) {
		synchronized(ClientList) {
			
			 GameServerThread client = new GameServerThread(cSocket);
			 ClientList.add(client);
			 
			// Temporarily use number of client as its user ID
			// for debugging with no database
			client.SetUserID(ClientList.size());
			client.SetUsername("Player " + client.GetUserID());
			System.out.println("GameServer: " + client.GetUsername() + " wants to join.");
			
			client.start();
		}
	}
	
	// Remove client socket from server
	public void DeleteClient(GameServerThread targetClient) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				if (client == targetClient) {
					client.Close();
					clientItr.remove();
				}
			}
		}
	}
	
	// Get number of clients
	public int GetClientCount() {
		return ClientList.size();
	}

	/****************************************/
	/** Packet broadcasting to all clients **/
	/****************************************/
	
	/********************/
	/** Lobby updating **/
	/********************/
	
	public void BroadcastLobbyUpdateAddPlayer(String username) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.LobbyUpdateAddPlayer(username);
			}
		}
	}
	
	public void BroadcastLobbyUpdateRemovePlayer(String username) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.LobbyUpdateRemovePlayer(username);
			}
		}
	}
	
	public void BroadcastLobbyUpdateAddGame(String gameName, String username) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.LobbyUpdateAddGame(gameName, username);
			}
		}
	}
	
	public void BroadcastLobbyUpdateRemoveGame(String gameName, String username) {
		synchronized(ClientList) {
			for(Iterator<GameServerThread> clientItr = ClientList.listIterator(); clientItr.hasNext();) {
				GameServerThread client = clientItr.next();
				client.sendPacket.LobbyUpdateRemoveGame(gameName, username);
			}
		}
	}

	/*********************/
	/** GameServer main **/
	/*********************/

    public static void main(String[] args) throws Exception {
    	
    	//GameServer.gameServer.CloseServer();
    	
    	/*
        if (args.length != 1) {
            System.err.println("Usage: java GameServer <port number>");
            System.exit(1);
        }
        */
    	
    	//int portNumber = Integer.parseInt(args[0]);
    	int portNumber = 8901;
    	GameServer gameServer = new GameServer(portNumber);
    }
    
}
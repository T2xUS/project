/*
 *  ClientPacketGenerator.java
 *  
 *  Class that creates functions to generate
 *  all different packet possibilities to be
 *  sent from client to server.
 *  
 */

package set.client;

import java.io.DataOutputStream;

import set.packet.DataWriter;

public class ClientPacketGenerator {
	
	/****************************/
	/** PacketGenerator fields **/
	/****************************/
	
	public DataOutputStream packet;
	
	/*********************************/
	/** PacketGenerator constructor **/
	/*********************************/
	
	public ClientPacketGenerator(DataOutputStream toServer) {
		packet = toServer;
	}
	
	/*****************************/
	/** PacketGenerator methods **/
	/*****************************/
	
	/********************/
	/** Lobby requests **/
	/********************/
	
	// Packet telling server to make solitaire room
	public void LobbyRequestCreateSolo() {

		DataWriter dataWriter = new DataWriter();
		short header = 1;
		int lobbyRequest = -1;

		synchronized (packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(lobbyRequest, packet);
		}
	}
	
	// Packet telling server to create game room
	public void LobbyRequestCreateGame(String gameName) {

		DataWriter dataWriter = new DataWriter();
		short header = 1;
		int lobbyRequest = 0;

		synchronized (packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(lobbyRequest, packet);
			dataWriter.WriteString(gameName, packet);
		}
	}
	
	// Packet telling server that player has joined game room
	public void LobbyRequestJoinGame(String hostName) {

		DataWriter dataWriter = new DataWriter();
		short header = 1;
		int lobbyRequest = 1;

		synchronized (packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(lobbyRequest, packet);
			dataWriter.WriteString(hostName, packet);
		}
	}
	
	/************************/
	/** Game room requests **/
	/************************/
	
	// Packet telling server to start solitaire
	public void GameRoomRequestStartSolo() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 2;
		int gameRoomRequest = -1;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to start hosted game
	public void GameRoomRequestStartHost() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 2;
		int gameRoomRequest = 0;

		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to exit solitaire game room
	public void GameRoomRequestExitSolo() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 2;
		int gameRoomRequest = 1;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to exit game room as host
	public void GameRoomRequestExitHost() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 2;
		int gameRoomRequest = 2;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to exit game room as player
	public void GameRoomRequestExitPlayer() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 2;
		int gameRoomRequest = 3;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	/*******************/
	/** Game requests **/
	/*******************/
	
	// Packet sending server a request to cheat
	public void GameRequestCheat() {

		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRequest = -2;

		synchronized (packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRequest, packet);
		}
	}
	
	// Packet sending server submitted set from solitaire mode
	public void GameRequestSubmitSetSolo(int card1Pos, int card2Pos, int card3Pos) {
		
		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRequest = -1;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRequest, packet);
			dataWriter.WriteInt(card1Pos, packet);
			dataWriter.WriteInt(card2Pos, packet);
			dataWriter.WriteInt(card3Pos, packet);
		}
	}
	
	// Packet sending server submitted set from multiplayer mode
	public void GameRequestSubmitSetMult(int card1Pos, int card2Pos, int card3Pos) {

		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRequest = 0;

		synchronized (packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRequest, packet);
			dataWriter.WriteInt(card1Pos, packet);
			dataWriter.WriteInt(card2Pos, packet);
			dataWriter.WriteInt(card3Pos, packet);
		}
	}
	
	// Packet telling server to exit solitaire game
	public void GameRequestExitSolo() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRoomRequest = 1;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to exit game as host
	public void GameRequestExitHost() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRoomRequest = 2;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
	// Packet telling server to exit game as player
	public void GameRequestExitPlayer() {
		
		DataWriter dataWriter = new DataWriter();
		short header = 3;
		int gameRoomRequest = 3;
		
		synchronized(packet) {
			dataWriter.WriteShort(header, packet);
			dataWriter.WriteInt(gameRoomRequest, packet);
		}
	}
	
}
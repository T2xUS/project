/*
 *  DataReader.java
 *  
 *  Class that overrides functions that read
 *  from the desired stream.
 *  
 */

package set.packet;

import java.io.DataInputStream;
import java.io.IOException;

public class DataReader  {
	
	/************************/
	/** DataReader methods **/
	/************************/

	public char ReadChar(DataInputStream input) {
		char data = '\0';
		try {
			data = input.readChar();
		} catch(IOException e) {
			System.err.println("DataReader: Error reading char from input stream");
		}
		return data;
	}
	
	public boolean ReadBoolean(DataInputStream input) {
		boolean data = false;
		try {
			data = input.readBoolean();
		} catch(IOException e) {
			System.err.println("DataReader: Error reading boolean from input stream");
		}
		return data;
	}
	
	public short ReadShort(DataInputStream input) {
		short data = 0;
		try {
			data = input.readShort();
		} catch(IOException e) {
			System.err.println("DataReader: Error reading short from input stream");
		}
		return data;
		
	}
	
	public int ReadInt(DataInputStream input) {
		int data = 0;
		try {
			data = input.readInt();
		} catch(IOException e) {
			System.err.println("DataReader: Error reading int from input stream");
		}
		return data;
	}
	
	public String ReadString(DataInputStream input) {
		String data = "";
		try {
			data = input.readUTF();
		} catch(IOException e) {
			System.err.println("DataReader: Error reading string from input stream");
		}
		return data;
	}
	
}
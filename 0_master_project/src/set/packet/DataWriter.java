/*
 *  DataWriter.java
 *  
 *  Class that overrides functions that write
 *  to the desired stream.
 *  
 */

package set.packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class DataWriter  {
	
	/************************/
	/** DataWriter methods **/
	/************************/

	public void WriteChar(int data, DataOutputStream output) {
		try {
			output.writeChar(data);
		} catch(IOException e) {
			System.err.println("DataWriter: Error writing char to output stream");
		}
	}
	
	public void WriteBoolean(boolean data, DataOutputStream output) {
		try {
			output.writeBoolean(data);
		} catch(IOException e) {
			System.err.println("DataWriter: Error writing boolean to output stream");
		}
	}
	
	public void WriteShort(int data, DataOutputStream output) {
		try {
			output.writeShort(data);
		} catch(IOException e) {
			System.err.println("DataWriter: Error writing short to output stream");
		}
	}
	
	public void WriteInt(int data, DataOutputStream output) {
		try {
			output.writeInt(data);
		} catch(IOException e) {
			System.err.println("DataWriter: Error writing int to output stream");
		}
	}

	public void WriteString(String data, DataOutputStream output) {
		try {
			output.writeUTF(data);
		} catch(IOException e) {
			System.err.println("DataWriter: Error writing string to output stream");
		}
	}
	
}
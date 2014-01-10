package TTT.commons.communication;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

public class Communicator {
	private static Communicator instance = new Communicator();

	private MessagesFactory facto;
	private BufferedReader dataIn;
	private BufferedWriter dataOut;

	private Communicator(){
		this.facto = new MessagesFactory();
		this.dataIn = null;
		this.dataOut = null;
	}

	public static Communicator getInstance(){
		return instance;
	}

	public boolean isConnected(){
		return (this.dataIn != null && this.dataOut != null);
	}

	public boolean connect(InputStream in, OutputStream out){
		try{
			this.dataIn  = new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));
			this.dataOut = new BufferedWriter(new OutputStreamWriter(out,"ISO-8859-1"));
		} catch(UnsupportedEncodingException e){
			this.close();
			return false;
		}
		return true;
	}

	public void close(){
		if(this.dataIn != null){
			try{
				this.dataIn.close();
			} catch(IOException e){
			}
			this.dataIn = null;
		}
		if(this.dataOut != null){
			try{
				this.dataOut.close();
			} catch(IOException e){
			}
			this.dataOut = null;
		}
	}

	public boolean sendMessage(Message msg){
		try{
			this.dataOut.write(msg.toString());
			this.dataOut.newLine();
			this.dataOut.flush();
		} catch(IOException e){
			this.close();
			return false;
		}
		return true;
	}

	public Message readMessage(){
		Message msg = null;
		String in = null;
		try{
			in = this.dataIn.readLine();
		} catch(IOException e){
			in = null;
		} catch(NullPointerException e){
			this.close();
			return null;
		} catch(OutOfMemoryError e){
			this.close();
			return null;
		}
		if(in == null){
			this.close();
			return null;
		}
		try{
			msg = this.facto.getMessage(in);
		} catch(ParsingFailException e){
			msg = new Error(e.getMessage());
		}
		return msg;
	}
}

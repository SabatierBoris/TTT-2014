package TTT.PC.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTComm;

import TTT.commons.communication.Communicator;
import TTT.commons.communication.Message;

public class Connexion{
	private static Connexion instance = new Connexion();

	private Communicator comm;
	private NXTComm connection;

	private Connexion(){
		this.comm = Communicator.getInstance();
		this.connection = null;
	}

	public boolean isConnected(){
		return (this.comm.isConnected() && this.connection != null);
	}

	public boolean connect(){
		try{
			System.out.println("Wait");
			this.connection = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			this.connection.open(this.connection.search("NXT")[0]);
			System.out.println("Connect");
			this.comm.connect(this.connection.getInputStream(),this.connection.getOutputStream());
		} catch(NXTCommException e){
			this.close();
		}
		return true;
	}

	public void close(){
		this.comm.close();
		if(this.connection != null){
			try{
				this.connection.close();
			} catch(IOException e){
			}
			this.connection = null;
		}
	}

	public static Connexion getInstance(){
		return instance;
	}

	public String read(){
		Message in = this.comm.readMessage();
		if(in != null){
			return in.toString();
		} else {
			return "";
		}
	}
}



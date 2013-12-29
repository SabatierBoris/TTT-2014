package TTT.robots.communication;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.NXTConnection;

public class Connexion{
	private static Connexion instance = new Connexion();

	private NXTConnection connection;
	private BufferedWriter dataOut;


	private Connexion(){
		this.connection = USB.waitForConnection();
		try{
			this.dataOut = new BufferedWriter(new OutputStreamWriter(connection.openOutputStream(),"ISO-8859-1"));
		}catch(UnsupportedEncodingException e){
		}
	}

	public static Connexion getInstance(){
		return instance;
	}

	public void send(String data){
		try{
			this.dataOut.write(data);
			this.dataOut.newLine();
			this.dataOut.flush();
		} catch(IOException e){
			System.out.println(" write error " +e);
		}
	}
}

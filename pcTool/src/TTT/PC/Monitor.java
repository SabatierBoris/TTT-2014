package TTT.PC;

import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import TTT.commons.communication.Ping;
import TTT.commons.communication.SetConfig;

import TTT.PC.communication.Connexion;

public class Monitor{
	public static void main(String[] args){
		Connexion conn = Connexion.getInstance();
		conn.setDaemon(true);
		conn.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input,key,value;
		while(true){
			System.out.print("Config to set : ");
			try{
				input = br.readLine();
				StringTokenizer st = new StringTokenizer(input,"=");
				key = st.nextToken();
				value = st.nextToken();
				conn.send(new SetConfig(key,value));
			} catch(NoSuchElementException e){
			} catch(IOException ioe){
			}
		}
	}
}

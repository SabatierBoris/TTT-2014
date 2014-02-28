package TTT.libNXT.configuration;

import java.util.Properties;
import java.util.Queue;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

import TTT.commons.communication.Message;
import TTT.commons.communication.SetConfig;
import TTT.commons.communication.GetConfig;
import TTT.commons.communication.MessageListener;

import TTT.libNXT.communication.Connexion;

public class Configurateur extends Thread implements MessageListener {
	private static Configurateur instance = new Configurateur();

	private static final String configFile = "config.properties";
	private String name;
	private Properties prop;
	private final Hashtable<String,Collection<ConfigListener>> configListeners = new Hashtable<String, Collection<ConfigListener>>();
	private Connexion conn;
	private Queue<Message> queue;

	private Configurateur(){
		this.name = "default";
		this.prop = new Properties();
		this.load();
		this.conn = Connexion.getInstance();
		this.conn.addMessageListener(this,SetConfig.ID);
		this.conn.addMessageListener(this,GetConfig.ID);
		this.queue = new Queue<Message>();
	}

	public void addConfigListener(ConfigListener listener, String key){
		Collection<ConfigListener> tmp = this.configListeners.get(key);
		if(tmp == null){
			tmp = new ArrayList<ConfigListener>();
		}
		tmp.add(listener);
		this.configListeners.put(key,tmp);
	}

	public String get(String key, String d){
		String ret = this.prop.getProperty(key,d);
		if(ret.equals(d)){
			this.prop.setProperty(key,d);
		}
		return ret;
	}

	private void set(String key, String value){
		this.prop.setProperty(key,value);
		this.fireConfigChanged(key,value);
	}

	private void fireConfigChanged(String key, String value){
		if(this.configListeners != null){
			Enumeration<String> tmp = this.configListeners.keys();
			String k;
			while((k = tmp.nextElement()) != null){
				if(k.equals(key.substring(0,k.length()))){
					Collection<ConfigListener> listeners = this.configListeners.get(k);
					for(ConfigListener listener : listeners){
						listener.configChanged(key,value);
					}
				}
			}
		}
	}

	private void sendConfig(String key){
		Enumeration<?> tmp = this.prop.propertyNames();
		String k;
		while((k = (String)tmp.nextElement()) != null){
			if(k.substring(0,key.length()).equals(key)){
				this.conn.send(new GetConfig(k,this.prop.getProperty(k)));
			}
		}
	}

	@Override
	public void run(){
		SetConfig cf = null;
		GetConfig get = null;
		Message m = null;
		synchronized(this.queue){
			try{
				while(!this.isInterrupted()){
					if(!this.queue.empty()){
						m = (Message)this.queue.pop();
						if(m.getId() == SetConfig.ID){
						cf = (SetConfig)m;
						this.set(cf.getKey(),cf.getValue());
						} else if(m.getId() == GetConfig.ID){
							get = (GetConfig)m;
							this.sendConfig(get.getKey());
						}
					}
					this.queue.wait();
				}
			} catch(InterruptedException e){
				this.interrupt();
			}
		}
	}

	private String getConfigFileName(){
		return this.name + ".config";
	}

	public void save(){
		OutputStream output = null;
		try{
			output = new FileOutputStream(new File(this.getConfigFileName()));
			this.prop.store(output,"Configuration");
		} catch(IOException e){
		} finally {
			if(output != null){
				try{
					output.close();
				} catch(IOException e){
				}
			}
		}
	}

	private void load(){
		InputStream input = null;
		try{
			input = new FileInputStream(new File(this.getConfigFileName()));
			this.prop.load(input);
		} catch(IOException e){
		} finally {
			if(input != null){
				try{
					input.close();
				} catch(IOException e){
				}
			}
		}
	}

	@Override
	public void messageReceived(Message m){
		synchronized(this.queue){
			this.queue.push(m);
			this.queue.notify();
		}
	}

	public void setName(String name){
		this.name = name;
		this.load();
	}

	public static Configurateur getInstance(){
		return instance;
	}
}

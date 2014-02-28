package TTT.PC.models.config;

import javax.swing.event.EventListenerList;

import TTT.PC.models.communication.ConnexionModel;

import TTT.commons.communication.GetConfig;
import TTT.commons.communication.SetConfig;
import TTT.commons.communication.Message;
import TTT.commons.communication.MessageListener;

public class ConfigModel implements MessageListener {
	private ConnexionModel conn;
	private EventListenerList listeners;

	public ConfigModel(ConnexionModel conn){
		this.conn = conn;
		this.conn.addMessageListener(this,GetConfig.ID);
		this.listeners = new EventListenerList();
	}

	public void addConfigListener(ConfigListener listener){
		this.listeners.add(ConfigListener.class,listener);
	}

	public void removeConfigListener(ConfigListener listener){
		this.listeners.remove(ConfigListener.class,listener);
	}

	public void getConfigList(){
		this.conn.send(new GetConfig());
	}

	private void fireNewConfig(Config conf){
		ConfigListener[] listenerlist = this.listeners.getListeners(ConfigListener.class);
		for(ConfigListener listener : listenerlist){
			listener.newConfig(conf);
		}
	}

	public void messageReceived(Message m){
		if(m.getId() == GetConfig.ID){
			GetConfig c = (GetConfig)m;
			Config conf = new Config(c.getKey(),c.getValue());
			this.fireNewConfig(conf);
		}

	}

	public void updateConfig(Config conf){
		this.conn.send(new SetConfig(conf.getKey(),conf.getValue()));
	}
}


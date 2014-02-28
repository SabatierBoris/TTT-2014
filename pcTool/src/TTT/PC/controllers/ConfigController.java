package TTT.PC.controllers;

import TTT.PC.models.config.Config;
import TTT.PC.models.config.ConfigModel;
import TTT.PC.models.config.ConfigListener;

public class ConfigController {
	private ConfigModel model = null;

	public ConfigController(ConfigModel model){
		super();
		this.model = model;
	}

	public void addView(ConfigListener view){
		this.model.addConfigListener(view);
	}

	public void getConfigList(){
		this.model.getConfigList();
	}

	public void updateConfig(Config conf,String newValue){
		conf.setValue(newValue);
		this.model.updateConfig(conf);
	}

/*
	public void toogleConnexion(){
		if(this.model.isConnected()){
			this.model.close();
		} else {
			this.model.connect();
		}
	}
*/
}

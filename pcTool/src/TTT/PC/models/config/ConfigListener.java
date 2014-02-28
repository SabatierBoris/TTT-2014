package TTT.PC.models.config;

import java.util.EventListener;

public interface ConfigListener extends EventListener {
	public void newConfig(Config conf);
//	public void statutChanged(StatutChangedEvent event);
}

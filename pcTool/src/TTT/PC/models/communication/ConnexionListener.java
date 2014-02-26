package TTT.PC.models.communication;

import java.util.EventListener;

public interface ConnexionListener extends EventListener {
	public void statutChanged(StatutChangedEvent event);

}

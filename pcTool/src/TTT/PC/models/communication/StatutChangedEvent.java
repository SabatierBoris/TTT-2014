package TTT.PC.models.communication;

import java.util.EventObject;

public class StatutChangedEvent extends EventObject {
	static final long serialVersionUID = -8410038983663623303L;
	private boolean newStatut;
	public StatutChangedEvent(Object source, boolean newStatut){
		super(source);
		this.newStatut = newStatut;
	}
	public boolean getNewStatut(){
		return this.newStatut;
	}
}



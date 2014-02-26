package TTT.PC.views.actions;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

public class QuitAction extends AbstractAction {
	static final long serialVersionUID = 6462335780625577573L;
	public QuitAction(String text){
		super(text);
	}
	public void actionPerformed(ActionEvent e){
		System.exit(0);
	}
}

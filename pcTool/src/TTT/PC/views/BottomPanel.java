package TTT.PC.views;

import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class BottomPanel extends JPanel {
	static final long serialVersionUID = 4850254557017449808L;
	public BottomPanel(ConnexionViewStatusBar statusBar){
		super(new BorderLayout());

		JTextPane console = new JTextPane();
		MessageConsole mc = new MessageConsole(console);
//		mc.redirectOut();
	//	mc.redirectErr(Color.RED,null);


		this.add(new JScrollPane(console),BorderLayout.CENTER);
		this.add(statusBar, BorderLayout.SOUTH);
		this.setSize(100,500);
	}
}


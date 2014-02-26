package TTT.PC.views;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.config.ConfigTableModel;

import TTT.PC.controllers.ConnexionController;

import TTT.PC.views.actions.QuitAction;
import TTT.PC.views.actions.ToogleConnectAction;

public class MainView extends JFrame {
	static final long serialVersionUID = 4634524343922566095L;

	public MainView(){
		super();
		build();
	}

	private void build(){
		ConnexionModel connModel = new ConnexionModel();
		//TODO uncomment connModel.setDaemon(true);
		//TODO uncomment connModel.start();
		ConnexionController connControl = new ConnexionController(connModel);

		ConnexionViewStatusBar statusBar = new ConnexionViewStatusBar(connControl);
		this.getContentPane().add(new JScrollPane(new JTable(new ConfigTableModel())),java.awt.BorderLayout.CENTER);
		this.getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");

		menu1.add(new JMenuItem(new ToogleConnectAction(connControl)));
		menu1.add(new JMenuItem(new QuitAction("Quit")));
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);


		this.setTitle("TTT - Pc Monitor Tool");
		this.setSize(320,240);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

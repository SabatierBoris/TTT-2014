package TTT.PC.views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;


import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.config.ConfigModel;

import TTT.PC.models.task.TaskAsservInfo;

import TTT.PC.controllers.ConnexionController;
import TTT.PC.controllers.ConfigController;

import TTT.PC.views.actions.QuitAction;
import TTT.PC.views.actions.ToogleConnectAction;
import TTT.PC.views.actions.FixLinearAsservAction;
import TTT.PC.views.actions.FixAngularAsservAction;

public class MainView extends JFrame {
	static final long serialVersionUID = 4634524343922566095L;

	public MainView(){
		super();
		build();
	}

	private void build(){
		ConnexionModel connModel = new ConnexionModel();
		connModel.setDaemon(true);
		connModel.start();

		TaskAsservInfo asservInfo = new TaskAsservInfo(connModel);
		asservInfo.setDaemon(true);
		asservInfo.start();

		ConfigModel confModel = new ConfigModel(connModel);

		ConnexionController connControl = new ConnexionController(connModel);
		ConfigController confControl = new ConfigController(confModel);

		ConnexionViewStatusBar statusBar = new ConnexionViewStatusBar(connControl);
		JTable configTable = new JTable(new ConfigTableModel(connControl,confControl));
		configTable.setAutoCreateRowSorter(true);
		this.getContentPane().add(new JScrollPane(configTable),BorderLayout.CENTER);
		this.getContentPane().add(new BottomPanel(statusBar), BorderLayout.SOUTH);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");

		menu1.add(new JMenuItem(new ToogleConnectAction(connControl)));
		menu1.add(new JMenuItem(new FixLinearAsservAction(connControl)));
		menu1.add(new JMenuItem(new FixAngularAsservAction(connControl)));
		menu1.add(new JMenuItem(new QuitAction("Quit")));
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);


		this.setTitle("TTT - Pc Monitor Tool");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}

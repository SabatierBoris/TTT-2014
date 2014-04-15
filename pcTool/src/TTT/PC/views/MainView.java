package TTT.PC.views;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;


import TTT.PC.models.communication.ConnexionModel;
import TTT.PC.models.config.ConfigModel;
import TTT.PC.models.graph.GraphModel;

import TTT.PC.models.task.TaskAsservInfo;

import TTT.PC.controllers.ConnexionController;
import TTT.PC.controllers.ConfigController;
import TTT.PC.controllers.AsservController;

import TTT.PC.views.actions.QuitAction;
import TTT.PC.views.actions.ToogleConnectAction;
import TTT.PC.views.actions.FixLinearAsservAction;
import TTT.PC.views.actions.FixAngularAsservAction;
import TTT.PC.views.actions.BatteryAction;

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


		ConfigModel confModel = new ConfigModel(connModel);

		GraphModel linearGraphModel = new GraphModel();
		GraphModel angularGraphModel = new GraphModel();

		TaskAsservInfo asservInfo = new TaskAsservInfo(connModel,linearGraphModel,angularGraphModel);
		asservInfo.setDaemon(true);
		asservInfo.start();

		ConnexionController connControl = new ConnexionController(connModel);
		ConfigController confControl = new ConfigController(confModel);
		AsservController asservControl = new AsservController(confControl,connControl,linearGraphModel,angularGraphModel,asservInfo);

		ConnexionViewStatusBar statusBar = new ConnexionViewStatusBar(connControl);
		JTable configTable = new JTable(new ConfigTableModel(connControl,confControl));
		configTable.setAutoCreateRowSorter(true);
		this.getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 4;
		c.weightx = 0.0;
		c.weighty = 1;
		this.getContentPane().add(new JScrollPane(configTable),c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 1;
		this.getContentPane().add(new GraphView(linearGraphModel),c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 1;
		this.getContentPane().add(new GraphView(angularGraphModel),c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 4;
		c.weightx = 1;
		c.weighty = 1;
		this.getContentPane().add(new BottomPanel(statusBar), c);

		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");

		menu1.add(new JMenuItem(new ToogleConnectAction(connControl)));
		menu1.add(new JMenuItem(new FixLinearAsservAction(asservControl)));
		menu1.add(new JMenuItem(new FixAngularAsservAction(connControl)));
		menu1.add(new JMenuItem(new BatteryAction(connControl,asservInfo,linearGraphModel,angularGraphModel)));
		menu1.add(new JMenuItem(new QuitAction("Quit")));
		menuBar.add(menu1);
		this.setJMenuBar(menuBar);


		this.setTitle("TTT - Pc Monitor Tool");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}

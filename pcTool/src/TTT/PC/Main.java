package TTT.PC;

import javax.swing.SwingUtilities;

import TTT.PC.views.MainView;

public class Main implements Runnable {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Main());
	}

	@Override
	public void run(){
		MainView w = new MainView();
		w.setVisible(true);
	}
}

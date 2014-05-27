package TTT.robots.actions;

import TTT.libNXT.ia.Action;

import TTT.libNXT.navigation.Navigator;

public class FresqueAction extends Action {
	private Navigator nav;

	public FresqueAction(Navigator nav){
		super("PrintFresque");
		this.nav = nav;
	}

	@Override
	public void run(){
		this.nav.moveBackward(300);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
		}
	}
}

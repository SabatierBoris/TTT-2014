package TTT.PC.views;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

import TTT.PC.models.graph.GraphModel;
import TTT.PC.models.graph.GraphListener;

public class GraphView extends JPanel implements GraphListener {
	static final long serialVersionUID = 134422341760395438L;
	private GraphModel graph;

	public GraphView(GraphModel graph){
		super();
		this.graph = graph;
		this.graph.addGraphListener(this);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int x1,x2,y1,y2;
		int xScale,yScale,yMove;
		int dataSize = this.graph.getDataSize();
		if(dataSize < 2){
			return;
		}
		Rectangle dim = g.getClipBounds();

		//Calculate the scale and move
		xScale = dim.width/(dataSize-1);
		Integer min = null;
		Integer max = null;
		for(int i=0;i<dataSize;i++){
			int val = this.graph.getData(i);
			if(min == null || val < min){
				min = val;
			}
			if(max == null || val > max){
				max = val;
			}
			val = this.graph.getTarget(i);
			if(min == null || val < min){
				min = val;
			}
			if(max == null || val > max){
				max = val;
			}
		}
		max++;
		if(min > 0){
			min = 0;
		}
		yMove = Math.abs(min);
		if(max == min){
			return;
		}
		yScale = dim.height/(max-min);

		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
		g.setColor(Color.red);
		for(int i=0;i<dataSize;i++){
			x2 = i;
			y2 = this.graph.getData(i);
			if(i>0){
				g.drawLine(x1*xScale,dim.height-((y1+yMove)*yScale),x2*xScale,dim.height-((y2+yMove)*yScale));
			}
			x1 = x2;
			y1 = y2;
		}
		if(yMove != 0){
			g.setColor(Color.black);
			for(int i=0;i<dataSize;i++){
				x2 = i;
				y2 = 0;
				if(i>0){
					g.drawLine(x1*xScale,dim.height-((y1+yMove)*yScale),x2*xScale,dim.height-((y2+yMove)*yScale));
				}
				x1 = x2;
				y1 = y2;
			}
		}
		g.setColor(Color.blue);
		for(int i=0;i<dataSize;i++){
			x2 = i;
			y2 = this.graph.getTarget(i);
			if(i>0){
				g.drawLine(x1*xScale,dim.height-((y1+yMove)*yScale),x2*xScale,dim.height-((y2+yMove)*yScale));
			}
			x1 = x2;
			y1 = y2;
		}
	}

	@Override
	public void newData(){
		this.repaint();
	}
}

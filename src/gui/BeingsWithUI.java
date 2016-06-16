package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import model.AgentCamp;
import model.AgentFlag;
import model.AgentPlayer;
import model.AgentTrace;
import model.Beings;
import model.Blue;
import model.RectObstacle;
import model.Red;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.portrayal.simple.TrailedPortrayal2D;

public class BeingsWithUI extends GUIState {
	public static int FRAME_SIZE = 600;
	public Display2D display;
	public JFrame displayFrame;
	ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
	
	public BeingsWithUI(SimState state) {
		super(state);
	}
	public static String getName() {
		return "Simulation de prise de drapeaux"; 
	}
	public void start() {
		super.start();
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}
	public void setupPortrayals() {
		Beings beings = (Beings) state;	
		yardPortrayal.setField(beings.yard );
		yardPortrayal.setPortrayalForClass(AgentCamp.class, getTypeCampPortrayal());
		yardPortrayal.setPortrayalForClass(AgentFlag.class, getTypeFlagPortrayal());
		yardPortrayal.setPortrayalForClass(Red.class, getTypeRedPortrayal());
		yardPortrayal.setPortrayalForClass(Blue.class, getTypeBluePortrayal());
		yardPortrayal.setPortrayalForClass(RectObstacle.class, getTypeObstaclePortrayal());
		yardPortrayal.setPortrayalForClass(AgentTrace.class, getTypeTracePortrayal());
		display.reset();
		display.setBackdrop(new Color(120, 180, 110));
		display.repaint();
	}
	private LabelledPortrayal2D getTypeRedPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D()
		{
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				Red myobject = (Red)object;
				boolean a = myobject.confused>0;
				if(a)
					filled = false;
				else
					filled = true;
				super.draw(object, graphics, info);
			}
		};
		r.paint = Color.RED;
		r.scale = 1.5;
		LabelledPortrayal2D l = new LabelledPortrayal2D(r, null);
		return l;
	}
	private LabelledPortrayal2D getTypeBluePortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D()
		{
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				Blue myobject = (Blue)object;
				boolean a = myobject.confused>0;
				if(a)
					filled = false;
				else
					filled = true;
				super.draw(object, graphics, info);
			}
		};
		r.paint = Color.BLUE;
		r.scale = 1.5;
		LabelledPortrayal2D l = new LabelledPortrayal2D(r, null);
		return l;
	}
	private RectanglePortrayal2D getTypeFlagPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D(){
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				AgentFlag myobject = (AgentFlag)object;
				String a = myobject.color;
				if(a.equals("red"))
					paint = Color.RED;
				else if(a.equals("blue"))
					paint = Color.BLUE;
				super.draw(object, graphics, info);
			}
		};
		r.filled = false;
		r.scale = 1.5;
		return r;
	}
	
	private OvalPortrayal2D getTypeCampPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(){
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				AgentCamp myobject = (AgentCamp)object;
				String a = myobject.team;
				if(a.equals("red"))
					paint = Color.RED;
				else if(a.equals("blue"))
					paint = Color.BLUE;
				super.draw(object, graphics, info);
			}
		};
		r.filled = false;
		r.scale = 10.0;
		return r;
	}
	
	private SimplePortrayal2D getTypeObstaclePortrayal() {
		SimplePortrayal2D r = new SimplePortrayal2D(){
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				RectObstacle myobject = (RectObstacle)object;
				super.draw(object,graphics,info);
				double width = info.draw.width*myobject.width;
				double height = info.draw.height*myobject.height;
				graphics.setColor(new Color(200,200,200));
				graphics.fillRect((int)info.draw.x, (int)info.draw.y,(int)width,(int)height);
			}
		};
		return r;
	}
	
	private OvalPortrayal2D getTypeTracePortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.filled = true;
		r.paint = Color.BLACK;
		r.scale = 0.5;
		return r;
	}
	
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Beings");
		c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		displayFrame.setVisible(true);
		display.attach( yardPortrayal, "Yard" );
	}

	private int numBlues;
	public int getNumBlues() {
		return numBlues;
	}
	public void setNumBlues(int numBlues) {
		this.numBlues = numBlues;
	} 
	
	private int numReds;
	public int getNumReds() {
		return numReds;
	}
	public void setNumReds(int numReds) {
		this.numReds = numReds;
	} 

	public  Object  getSimulationInspectedObject()  {  return  state;  }
	public  Inspector  getInspector() {
		Inspector  i  =  super.getInspector();
		i.setVolatile(true);
		return  i;
	}
}

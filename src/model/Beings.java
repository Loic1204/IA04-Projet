package model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Beings extends SimState {
	public int numReds=0;
	public int numBlues=0;
	public int numBlueFlags=0;
	public boolean stopped = false;

	public Continuous2D yard = new Continuous2D(1.0, Constants.GRID_SIZE,Constants.GRID_SIZE);
	public Beings(long seed) {
		super(seed);
	}
	public void start() {
		System.out.println("Simulation started");
		super.start();
		yard.clear();
		addAgentsCamp();
		addAgentsBlue();
		addAgentsRed();
		addAgentsBlueFlag();
		addAgentsRedFlag();
		addObstacle();
	}
	
	public void stop(){
		stopped=true;
	}
	
	private void addAgentsBlue() {

		for(int  i  =  0;  i  <  Constants.NUM_BLUE-1;  i++) {
			Blue  blue  =  new  Blue();
			yard.setObjectLocation(blue,
					new Double2D((random.nextInt((int) yard.getWidth()))%(yard.getWidth()/4),
							(random.nextInt((int) yard.getHeight()))%(yard.getHeight()/4)));
			numBlues++;
			blue.x = yard.getObjectLocation(blue).x;
			blue.y = yard.getObjectLocation(blue).y;
			Stoppable stoppable = schedule.scheduleRepeating(blue);
			blue.stoppable = stoppable;
			blue.strategy = "attack";
		}  
		for(int  i  =  0;  i  <  1;  i++) {
			Blue  blue  =  new  Blue();
			yard.setObjectLocation(blue,
					new Double2D((random.nextInt((int) yard.getWidth()))%(yard.getWidth()/4),
							(random.nextInt((int) yard.getHeight()))%(yard.getHeight()/4)));
			numBlues++;
			blue.x = yard.getObjectLocation(blue).x;
			blue.y = yard.getObjectLocation(blue).y;
			Stoppable stoppable = schedule.scheduleRepeating(blue);
			blue.stoppable = stoppable;
			blue.strategy = "defend";
		}  
	}
	
	private void addAgentsRed() {

		for(int  i  =  0;  i  <  Constants.NUM_RED -1;  i++) {
			Red  red  =  new  Red();
			yard.setObjectLocation(red,
					new Double2D(yard.getWidth()*3/4 + random.nextInt((int) yard.getWidth()/4),
							yard.getHeight()*3/4 + random.nextInt((int) yard.getHeight()/4)));
			numReds++;
			red.x = yard.getObjectLocation(red).x;
			red.y = yard.getObjectLocation(red).y;
			Stoppable stoppable = schedule.scheduleRepeating(red);
			red.stoppable = stoppable;
			red.strategy = "attack";
		}  
		for(int  i  =  0;  i  <  1;  i++) {
			Red  red  =  new  Red();
			yard.setObjectLocation(red,
					new Double2D(yard.getWidth()*3/4 + random.nextInt((int) yard.getWidth()/4),
							yard.getHeight()*3/4 + random.nextInt((int) yard.getHeight()/4)));
			numReds++;
			red.x = yard.getObjectLocation(red).x;
			red.y = yard.getObjectLocation(red).y;
			Stoppable stoppable = schedule.scheduleRepeating(red);
			red.stoppable = stoppable;
			red.strategy = "defend";
		} 
	}
	
	private void addAgentsBlueFlag() {

		for(int  i  =  0;  i  <  Constants.NUM_BLUE_FLAGS;  i++) {
			AgentFlag  bf  =  new  AgentFlag();
			yard.setObjectLocation(bf,
					new Double2D(yard.getWidth()*1/12 + (random.nextInt((int) yard.getWidth()))%(yard.getWidth()/12),
							yard.getHeight()*1/12 + (random.nextInt((int) yard.getHeight()))%(yard.getHeight()/12)));
			numBlueFlags++;
			bf.x = yard.getObjectLocation(bf).x;
			bf.y = yard.getObjectLocation(bf).y;
			Stoppable stoppable = schedule.scheduleRepeating(bf);
			bf.stoppable = stoppable;
			bf.color = "blue";
		}  
	}
	
	private void addAgentsRedFlag() {

		for(int  i  =  0;  i  <  Constants.NUM_RED_FLAGS;  i++) {
			AgentFlag  rf  =  new  AgentFlag();
			yard.setObjectLocation(rf,
					new Double2D(yard.getWidth()*10/12 + (random.nextInt((int) yard.getWidth()))%(yard.getWidth()/12),
							yard.getHeight()*10/12 + (random.nextInt((int) yard.getHeight()))%(yard.getHeight()/12)));
			numBlueFlags++;
			rf.x = yard.getObjectLocation(rf).x;
			rf.y = yard.getObjectLocation(rf).y;
			Stoppable stoppable = schedule.scheduleRepeating(rf);
			rf.stoppable = stoppable;
			rf.color = "red";
		}  
	}
	
	private void addAgentsCamp() {
		AgentCamp  rf  =  new  AgentCamp();
		yard.setObjectLocation(rf,
				new Double2D(yard.getWidth()*10.5/12, yard.getHeight()*10.5/12));
		rf.team = "red"; 
		schedule.scheduleRepeating(rf);
		
		AgentCamp  bf  =  new  AgentCamp();
		yard.setObjectLocation(bf,
				new Double2D(yard.getWidth()*1.5/12, yard.getHeight()*1.5/12));
		bf.team = "blue"; 
		schedule.scheduleRepeating(bf);
	}
	
	private void addObstacle() {
		for(int i=0 ; i<12 ; i+=2){
			for(int j=0 ; j<12 ; j+=2){
				if((i<3 && j<3) || (i>8 && j>8))
					continue;
				int choice = random.nextInt((int) 2);
				
				RectObstacle  rf = null;
				if(choice==0)
					rf =  new  RectObstacle(random.nextInt((int) 10), 1.0);
				else if(choice==1)
					rf =  new  RectObstacle(1.0, random.nextInt((int) 10));
					
				if(rf!=null)
					yard.setObjectLocation(rf, new Double2D(yard.getWidth()*i/12 + random.nextInt((int) 6), yard.getHeight()*j/12 + random.nextInt((int) 6)));
			}
		}
	}
	
	public void addTrace(Double2D location, int life) {
		AgentTrace  trace  =  new  AgentTrace();
		trace.life = life;
		yard.setObjectLocation(trace, location); 
		Stoppable stoppable = schedule.scheduleRepeating(trace);
		trace.stoppable = stoppable;
	}

	public int getNumRed() {
		return numReds;
	}
	public void setNumReds(int numReds) {
		this.numReds = numReds;
	} 
	
	public int getNumBlue() {
		return numBlues;
	}
	public void setNumBlues(int numBlues) {
		this.numBlues = numBlues;
	} 
}

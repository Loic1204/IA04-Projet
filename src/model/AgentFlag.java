package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class AgentFlag implements Steppable{
	public double x, y;
	public static int LEVEL = 2;
	public Stoppable stoppable;
	public String color = "";
	//public int WEIGHT;
	public boolean isTaken = false;
	
	public boolean isInCamp(Beings beings, String team){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), 4.0);
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(neighbors.get(i).getClass().getName().equals(AgentCamp.class.getName()))
				if(((AgentCamp)neighbors.get(i)).team.equals(team))
					return true;
		}
		return false;
	}

	@Override
	public void step(SimState state) {
	}
}
package model;

import sim.app.woims.Obstacle;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;

public class AgentCamp implements Steppable {
	public String team ="";
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		
		if(beings.stopped)
			return ;
		
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), 10.0);
		
		int flagNb = 0;
		
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(neighbors.get(i).getClass().getName().equals(AgentFlag.class.getName()) && ((AgentFlag)neighbors.get(i)).isInCamp(beings, team)){
				if(!((AgentFlag)neighbors.get(i)).color.equals(team))
					flagNb++;
				
			}
		}
		
		if(team.equals("red") && flagNb==Constants.NUM_BLUE_FLAGS){
			System.out.println(team + " has won!");
			beings.stop();
		}
		
		if(team.equals("blue") && flagNb==Constants.NUM_RED_FLAGS){
			System.out.println(team + " has won!");
			beings.stop();
		}
	}
}

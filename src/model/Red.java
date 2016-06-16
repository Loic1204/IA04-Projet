package model;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Double2D;

public class Red extends AgentPlayer {
	public Double2D adverseCampLocation = null;
	public String strategy = "";
	
	@Override
	public String getTeam(){
		return "red";
	}
	
	@Override
	public Double2D getOwnCampLocation(Beings beings) {
		return new Double2D(beings.yard.getWidth()*10.5/12.0, beings.yard.getHeight()*10.5/12.0);
	}

	public boolean perceiveEnnemyCamp(Beings beings){
		Bag neighbors = this.perceiveCamps(beings);
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(!((AgentCamp)neighbors.get(i)).team.equals(this.getTeam())){
				adverseCampLocation = beings.yard.getObjectLocation(neighbors.get(i));
				return true;
			}
		}
		return false;
	}
	
	int perceivedFlagsAtEnnemies = 5;
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		
		if(beings.stopped)
			return ;
		
		boolean done = false;
		perceiveEnnemyCamp(beings);
		
		if(confused>0){ 
			done = escape(beings);
		}
		if(done) return;
		
		done = takeFlag(beings);
		if(done) return;
		
		if(hasFlag){
			done = moveTowardHome(beings);
			if(done) return;
			
			done = putFlagDown(beings);
			if(done) return;
		}
		else{
			done = attack(beings);
			if(done) return;
			
			if(strategy.equals("defend")){
				done = moveTowardPerceivedBeing(beings, false, true);
				if(done) return;

				int choice = beings.random.nextInt((int) 2);
				if(choice==1){
					done = moveTowardPerceivedBeing(beings, true, true);
					if(done) return;
				}
				
				done = moveTowardHome(beings);
				if(done) return;
			}
			else{
			
				if(perceivedFlagsAtEnnemies>0){
					done = moveTowardAdverseCamp(beings);
					if(this.x<beings.yard.getWidth()*11.0/12 && this.x>beings.yard.getWidth()*10.0/12
							&& this.y<beings.yard.getHeight()*11.0/12 && this.y>beings.yard.getHeight()*10.0/12){
						perceivedFlagsAtEnnemies = perceiveFlags(beings).numObjs;
					}
					if(done)return;
				}
	
				done = moveTowardPerceivedBeing(beings, false, true);
				if(done) return;
	
				int choice = beings.random.nextInt((int) 2);
				if(choice==1){
					done = moveTowardPerceivedBeing(beings, true, true);
					if(done) return;
				}
				
				done = moveRandom(beings);
				if(done) return;
			}
		}
	}
	
	public boolean moveTowardAdverseCamp(Beings beings){
		perceiveEnnemyCamp(beings);
		
		if(adverseCampLocation!=null){
			return this.moveToward(beings, this.adverseCampLocation);
		}
		return false;
	}
}

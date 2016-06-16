package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public class AgentTrace implements Steppable {
	public int life;
	public Stoppable stoppable;
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		
		life--;
		if(life<1){
			stoppable.stop();
			beings.yard.remove(this);
		}
	}
}

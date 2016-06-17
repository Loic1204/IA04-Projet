package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;

public class AgentPlayer implements Steppable {
	public double x, y;
	public static int LEVEL = 2;
	public Stoppable stoppable;

	public int Energie = Constants.MAX_ENERGY ;
	
	public boolean hasFlag = false;
	public int confused = 0;
	public Double2D ennemy = null;
	public double armLength = Constants.armLength;;
	public AgentFlag flag = null;
	public Double2D randomDestination = null;
	
	public String getTeam(){
		return "";
	}
	
	public Double2D getOwnCampLocation(Beings beings){
		return null;
	}

	@Override
	public void step(SimState state) {
	}
	
	public Bag perceive(Beings beings){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), Constants.DISTANCE_PERCEPTION);
		return neighbors;
	}
	
	public Bag perceiveCamps(Beings beings){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), Constants.DISTANCE_PERCEPTION+5.0);
		Bag camps = new Bag();
		
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(neighbors.get(i).getClass().getName().equals(AgentCamp.class.getName())){
				camps.add(neighbors.get(i));
			}
		}
		
		return camps;
	}
	
	public Bag perceiveFlags(Beings beings){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), Constants.DISTANCE_PERCEPTION);
		Bag flags = new Bag();
		
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(neighbors.get(i).getClass().getName().equals(AgentFlag.class.getName())){
				flags.add(neighbors.get(i));
			}
		}
		
		return flags;
	}
	
	public Bag perceivePlayers(Beings beings){
		Bag players = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), armLength);
		return players;
	}
	
	public Bag perceiveEnnemies(Beings beings){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), Constants.DISTANCE_PERCEPTION);
		Bag flags = new Bag();
		
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(this.getTeam().equals("blue")
					&& neighbors.get(i).getClass().getName().equals(Red.class.getName()) ){
				flags.add(neighbors.get(i));
			}
			else if(this.getTeam().equals("red")
					&& neighbors.get(i).getClass().getName().equals(Blue.class.getName()) ){
				flags.add(neighbors.get(i));
			}
		}
		
		return flags;
	}
	
	public boolean escape(Beings beings){
		double xSymetrie = 2*x - ennemy.x;
		double ySymetrie = 2*y - ennemy.y;
		
		moveToward(beings, new Double2D(xSymetrie, ySymetrie));
		update(beings);
		confused--;
		return true;
	}
	
	public boolean attack(Beings beings){
		Bag neighbors = perceivePlayers(beings);
		for(int i=0 ; i<neighbors.numObjs ; i++){
			Object obj = neighbors.get(i);
			if(this.getTeam().equals("red")){
				if(obj.getClass().getName().equals(Blue.class.getName())){
					if(((Blue) obj).confused>0)
						continue;
					((Blue) obj).getConfused(beings, beings.yard.getObjectLocation(this));
					if(!hasFlag)
						takeFlag(beings);
					return true;
				}
			}
			else if(this.getTeam().equals("blue")){
				if(obj.getClass().getName().equals(Red.class.getName())){
					if(((Red) obj).confused>0)
						continue;
					((Red) obj).getConfused(beings, beings.yard.getObjectLocation(this));
					if(!hasFlag)
						takeFlag(beings);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean takeFlag(Beings beings){
		if(hasFlag)
			return false;
		
		Bag neighbors = perceive(beings);

		for(int i=0 ; i<neighbors.numObjs ; i++){
			Object obj = neighbors.get(i);
			if(obj.getClass().getName().equals(AgentFlag.class.getName())){
				if(!((AgentFlag)obj).isInCamp(beings, this.getTeam())){
					double vecteurX = beings.yard.getObjectLocation(neighbors.get(i)).x - beings.yard.getObjectLocation(this).x;
					double vecteurY = beings.yard.getObjectLocation(neighbors.get(i)).y - beings.yard.getObjectLocation(this).y;
					double norme = Math.sqrt((vecteurX*vecteurX) + (vecteurY*vecteurY));

					if(norme<=armLength){
						AgentFlag f = ((AgentFlag) neighbors.get(i));
						if(f.isTaken)
							continue;
						flag = f;
						hasFlag = true;
						flag.isTaken = true;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean putFlagDown(Beings beings){
		flag.isTaken = false;
		flag=null;
		hasFlag=false;
		return true;
	}
	
	public boolean moveToward(Beings beings, Double2D direction){
		double vecteurX = direction.x - this.x;
		double vecteurY = direction.y - this.y;
		double distanceParcourue = 0.0;
		
		double norme = Math.sqrt((vecteurX*vecteurX) + (vecteurY*vecteurY));
		if(norme<1.0)
			return false;
		double vecteurUnitX = vecteurX/norme;
		double vecteurUnitY = vecteurY/norme;
		//double vecteurFinalX = vecteurUnitX*Constants.DISTANCE_DEPLACEMENT;
		//double vecteurFinalY = vecteurUnitY*Constants.DISTANCE_DEPLACEMENT;
		
		double destinationX = this.x + vecteurUnitX;
		double destinationY = this.y + vecteurUnitY;
		double angle = Math.PI/4.0;
		
		for(int unit=0 ; unit<Constants.DISTANCE_DEPLACEMENT ; unit++){
			int nbOfTrys = 0;
			Bag neighbors = beings.yard.getNeighborsWithinDistance(new Double2D(destinationX, destinationY), 50.0);
			for(int i=0 ; i<neighbors.numObjs && nbOfTrys<8 ; i++){
				if(neighbors.get(i).getClass().getName().equals(RectObstacle.class.getName())){		
					Double2D closestPoint = ((RectObstacle)neighbors.get(i)).closestPoint(new Double2D(destinationX, destinationY), beings.yard.getObjectLocation(neighbors.get(i)));
					if(/*Math.sqrt(((closestPoint.x-destinationX)*(closestPoint.x-destinationX)) + ((closestPoint.y-destinationY)*(closestPoint.y-destinationY)))<1.0
							|| */closestPoint.equals(new Double2D(destinationX, destinationY))){
						nbOfTrys++;
						double newDestinationX = this.x + (destinationX - this.x)*Math.cos(angle) - (destinationY - this.y)*Math.sin(angle);
						double newDestinationY = this.y + (destinationX - this.x)*Math.sin(angle) + (destinationY - this.y)*Math.cos(angle);
						destinationX = newDestinationX;
						destinationY = newDestinationY;
						i=-1;

						if(nbOfTrys==4){ // inutile de faire demi-tour
							nbOfTrys++;
							newDestinationX = this.x + (destinationX - this.x)*Math.cos(angle) - (destinationY - this.y)*Math.sin(angle);
							newDestinationY = this.y + (destinationX - this.x)*Math.sin(angle) + (destinationY - this.y)*Math.cos(angle);
							destinationX = newDestinationX;
							destinationY = newDestinationY;
							i=-1;
						}
					}			
				}
			}
			
			if(nbOfTrys>8)
				break;
			
			/*if(norme<Constants.DISTANCE_DEPLACEMENT){
				destinationX = direction.x;
				destinationY = direction.y;
			}*/
			
			if(destinationX>beings.yard.getWidth())
				destinationX=beings.yard.getWidth();
			if(destinationY>beings.yard.getHeight())
				destinationY=beings.yard.getHeight();
			if(destinationX<0)
				destinationX=0;
			if(destinationY<0)
				destinationY=0;

			this.x = destinationX;
			this.y = destinationY;
			beings.addTrace(beings.yard.getObjectLocation(this), 2);
			beings.yard.setObjectLocation(this, new Double2D(destinationX, destinationY));
			distanceParcourue++;
			moveFlagWithMe(beings);
			
			vecteurX = direction.x - this.x;
			vecteurY = direction.y - this.y;
			
			norme = Math.sqrt((vecteurX*vecteurX) + (vecteurY*vecteurY));
			if(norme==0.0)
				break;
			vecteurUnitX = vecteurX/norme;
			vecteurUnitY = vecteurY/norme;
			destinationX = this.x + vecteurUnitX;
			destinationY = this.y + vecteurUnitY;
		}
		
		if(distanceParcourue>0.0)
			return true;
		return false;
	}
	
	public boolean moveRandom(Beings beings){
		if(randomDestination!=null){
			boolean done = this.moveToward(beings, randomDestination);
			if(done) return done;
		}
		
		randomDestination = new Double2D(beings.random.nextInt((int) beings.yard.getWidth()),
				beings.random.nextInt((int) beings.yard.getHeight()));
		
		return this.moveToward(beings, randomDestination);
	}
	
	public boolean moveTowardHome(Beings beings){
		Bag neighbors = beings.yard.getNeighborsWithinDistance(beings.yard.getObjectLocation(this), 3.0);
		for(int i=0 ; i<neighbors.numObjs ; i++){
			if(neighbors.get(i).getClass().getName().equals(AgentCamp.class.getName()))
				if(((AgentCamp)neighbors.get(i)).team.equals(this.getTeam()))
					return false;
		}
		
		return this.moveToward(beings, this.getOwnCampLocation(beings));
	}
	
	public boolean moveTowardPerceivedBeing(Beings beings, boolean goToEnnemy, boolean goToFlag){
		Bag neighbors = perceive(beings);
		
		for(int i=0 ; i<neighbors.numObjs ; i++){
			Object obj = neighbors.get(i);
			if(goToFlag && obj.getClass().getName().equals(AgentFlag.class.getName())){
				if(!((AgentFlag)obj).isInCamp(beings, this.getTeam()) && !((AgentFlag)obj).isTaken){
					return moveToward(beings, beings.yard.getObjectLocation(obj));
				}
			}
			if(this.getTeam().equals("red")){
				if(goToEnnemy && obj.getClass().getName().equals(Blue.class.getName())){
					return moveToward(beings, beings.yard.getObjectLocation(obj));
				}
			}
			else if(this.getTeam().equals("blue")){
				if(goToEnnemy && obj.getClass().getName().equals(Red.class.getName())){
					return moveToward(beings, beings.yard.getObjectLocation(obj));
				}
			}
		}
		return false;
	}
	
	public void moveFlagWithMe(Beings beings){
		if(flag!=null)
			beings.yard.setObjectLocation(flag, beings.yard.getObjectLocation(this));
	}

	public void getConfused(Beings beings, Double2D ennemyLocation){
		confused = Constants.ATTACK;
		ennemy = ennemyLocation;
		if(hasFlag)
			putFlagDown(beings);
		//escape(beings);
	}
	
	public void update(Beings beings){
		x = beings.yard.getObjectLocation(this).x;
		y = beings.yard.getObjectLocation(this).y;
	}

	@Override
	public String toString() {
		if(hasFlag)
			return "F";
		else
			return "";
	}
}

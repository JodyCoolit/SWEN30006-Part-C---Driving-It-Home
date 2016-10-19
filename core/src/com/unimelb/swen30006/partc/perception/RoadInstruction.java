package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.perception.Planning.carDirections;
import com.unimelb.swen30006.partc.perception.Planning.trafficlightColor;
import com.unimelb.swen30006.partc.roads.Road;

public class RoadInstruction extends Instruction {
	private Road currentRoad;
	private carDirections currentDirection;
	
	public RoadInstruction(Car car, Road r) {
		super(car);
		// TODO Auto-generated constructor stub
		this.currentRoad = r;
	}
	
	@Override
	public void next(float delta, trafficlightColor tColor, Point2D.Double nextTrafficlight){
		currentDirection = getDirection();
		if(tColor == trafficlightColor.Green){
			super.goStraight(delta);
		}else{//Red and yellow
			if(currentDirection == carDirections.North){
				if ((car.getPosition().y + car.getLength() / 2) < nextTrafficlight.y - currentRoad.getWidth()|| car.getPosition().y > nextTrafficlight.y){
					super.goStraight(delta);
				}
			}else if(currentDirection == carDirections.South){
				if ((car.getPosition().y + car.getLength() / 2) > nextTrafficlight.y + currentRoad.getWidth()|| car.getPosition().y < nextTrafficlight.y){
					super.goStraight(delta);
				}
			}else if(currentDirection == carDirections.East){
				if ((car.getPosition().x + car.getLength() / 2) < nextTrafficlight.x - currentRoad.getWidth() || car.getPosition().x > nextTrafficlight.x){
					super.goStraight(delta);
				}
			}else if(currentDirection == carDirections.West){
				if ((car.getPosition().x + car.getLength() / 2) > nextTrafficlight.x + currentRoad.getWidth()|| car.getPosition().x < nextTrafficlight.x){
					super.goStraight(delta);
				}
			}
		}
	}
	
	public carDirections getDirection(){
		if(currentRoad != null){
			if(currentRoad.getStartPos().x == currentRoad.getEndPos().x){
				//North South
				if(car.getPosition().x > currentRoad.getStartPos().x){
					return carDirections.South;
				}else{
					return carDirections.North;
				}
				
			}else{
				//West East
				if(car.getPosition().y > currentRoad.getStartPos().y){
					return carDirections.East;
				}else{
					return carDirections.West;
				}
			}
		}
		return currentDirection;
	}
}

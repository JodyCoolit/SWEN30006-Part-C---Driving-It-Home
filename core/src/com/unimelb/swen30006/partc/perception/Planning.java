package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.controllers.AIController;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

public class Planning implements IPlanning {
	
	private Car car;
	private Road[] roads;
	private Road currentRoad = null;
	private Point2D.Double nextTrafficlight = null;
	private enum carDirections {North,South,West,East}
	private carDirections currentDirection = null;
	
	public Planning(Car car, Road[] roads){
		this.car = car;
		this.roads = roads;
	}
	
	@Override
	public boolean planRoute(Double destination) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(PerceptionResponse[] results, int visibility, float delta) {
		if(currentDirection == null){
			currentDirection = getDirection();
			System.out.println(currentDirection+"");
		}
		
		if(currentRoad == null){
			currentRoad = roadAtPoint(car.getPosition());
			System.out.println(currentRoad.toString());
		}
		if(nextTrafficlight == null){
			nextTrafficlight = nextTlight();
			double distance = Float.MAX_VALUE;
			for(PerceptionResponse pr : results){
				if(pr.objectType == pr.objectType.TrafficLight){
					double temp = Point2D.distance(car.getPosition().getX(), car.getPosition().getY(), pr.position.x, pr.position.y);
					if(temp < distance){
						distance = temp;
						nextTrafficlight = pr.position;
					}
				}
			}
		}
		//check direction
		if(currentDirection == carDirections.North){
			if ((car.getPosition().y + car.getLength() / 2) > nextTrafficlight.y){
				this.car.accelerate();
				this.car.update(delta);
			}
		}else if(currentDirection == carDirections.South){
			if ((car.getPosition().y + car.getLength() / 2) < nextTrafficlight.y){
				this.car.accelerate();
				this.car.update(delta);
			}
		}else if(currentDirection == carDirections.East){
			if ((car.getPosition().x + car.getLength() / 2) > nextTrafficlight.x){
				this.car.accelerate();
				this.car.update(delta);
			}
		}else if(currentDirection == carDirections.West){
			if ((car.getPosition().x + car.getLength() / 2) < nextTrafficlight.x){
				this.car.accelerate();
				this.car.update(delta);
			}
		}
		
	}

	@Override
	public float eta() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Road roadAtPoint(Point2D.Double pos){
		for(Road r: this.roads){
			if(r.containsPoint(pos)){
				return r;
			}
		}
		return null;
	}
	
	public Point2D.Double nextTlight(){
		System.out.println(car.getPosition());
		
		return car.getPosition();
	}
	
	public carDirections getDirection(){
		if(car.getVelocity().x == 0){
			if(car.getVelocity().y > 0){
				return carDirections.North;
			}else if(car.getVelocity().y < 0){
				return carDirections.South;
			}
		}else if(car.getVelocity().y == 0){
			if(car.getVelocity().x > 0){
				return carDirections.East;
			}else if(car.getVelocity().x < 0){
				return carDirections.West;
			}
		}
		
		return null;
	}
	
}

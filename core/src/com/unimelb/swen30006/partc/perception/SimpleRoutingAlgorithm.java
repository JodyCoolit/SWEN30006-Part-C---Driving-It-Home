package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D.Double;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.perception.Planning.carDirections;

public class SimpleRoutingAlgorithm implements RoutingAlgorithm {
	
	
	
	@Override
	public int runAlgorithm(Car car, Double distination) {
		carDirections direction = getDirection(car);
		System.out.println(direction);
		if(car.getPosition().y < distination.y){
			System.out.println(car.getPosition().y);
			System.out.println(distination.y);
			//go North
			if(direction == carDirections.North){
				return 0;
			}else if(direction == carDirections.South){
				//turn around
			}else if(direction == carDirections.East){
				return 1;
			}else if(direction == carDirections.West){
				return 2;
			}
		}else if(car.getPosition().y > distination.y){
			System.out.println(car.getPosition().y);
			System.out.println(distination.y);
			//go South
			if(direction == carDirections.North){
				//turn around
			}else if(direction == carDirections.South){
				return 0;
			}else if(direction == carDirections.East){
				return 2;
			}else if(direction == carDirections.West){
				return 1;
			}
			
		}else if(car.getPosition().x < distination.x){
			System.out.println(car.getPosition().x);
			System.out.println(distination.x);
			//go East
			if(direction == carDirections.North){
				return 2;
			}else if(direction == carDirections.South){
				return 1;
			}else if(direction == carDirections.East){
				return 0;
			}else if(direction == carDirections.West){
				//turn around
			}
		}else if(car.getPosition().x > distination.x){
			System.out.println(car.getPosition().x);
			System.out.println(distination.x);
			//go West
			if(direction == carDirections.North){
				return 1;
			}else if(direction == carDirections.South){
				return 2;
			}else if(direction == carDirections.East){
				//turn around
			}else if(direction == carDirections.West){
				return 0;
			}
		}
		return 0;
	}

	@Override
	public float calculateETA(Double carPosition, Double distination) {
		// TODO Auto-generated method stub
		return 0;
	}

	public carDirections getDirection(Car car){
		if(car.getVelocity().angle() > -10 && car.getVelocity().angle() < 10 ){
			return carDirections.East;
		}else if(car.getVelocity().angle() > 170 && car.getVelocity().angle() < 190 ){
			return carDirections.West;
		}else if(car.getVelocity().angle() > -80 && car.getVelocity().angle() < 100 ){
			return carDirections.North;
		}else{
			return carDirections.South;
		}
	}
}

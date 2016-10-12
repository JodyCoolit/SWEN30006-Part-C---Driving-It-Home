package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.perception.Planning.trafficlightColor;

public class Instruction {
	protected Car car;
	
	public Instruction(Car car){
		this.car = car;
	}
	
	public void goStraight(float delta){
		this.car.accelerate();
		this.car.update(delta);
	}
	
	public void turnLeft(){
		
	}
	
	public void turnRight(){
		
	}
	
	public void next(float delta, trafficlightColor tColor, Point2D.Double nextTrafficlight){
		
	}
}

package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.perception.Planning.trafficlightColor;

public class IntersectionInstruction extends Instruction {
	
	public IntersectionInstruction(Car car) {
		super(car);
	}
	@Override
	public void goStraight(float delta){
		
	}
	@Override
	public void turnLeft(){
		System.out.println(car.getVelocity().angle());
		if((car.getVelocity().angle() <= 180.5f && car.getVelocity().angle() >= 179.5f)){
			car.turn(0);
			System.out.println("stop turning");
		}else{
			car.turn(1.0f);
		}
		
	}
	@Override
	public void turnRight(){
		
	}
	
	@Override
	public void next(float delta, trafficlightColor tColor, Point2D.Double nextTrafficlight){
		turnLeft();
		super.goStraight(delta / 5);
	}
}
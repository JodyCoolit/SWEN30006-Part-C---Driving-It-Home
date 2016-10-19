package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.perception.Planning.carDirections;
import com.unimelb.swen30006.partc.perception.Planning.trafficlightColor;

public class IntersectionInstruction extends Instruction {
	
	private int code;
	private float initialDirection;
	
	public IntersectionInstruction(Car car, int code) {
		super(car);
		initialDirection = car.getVelocity().angle();
		this.code = code;
		System.out.println(car.getVelocity().angle());
		System.out.println(code);
	}
	@Override
	public void goStraight(float delta){
	}
	@Override
	public void turnLeft(){
		if((car.getVelocity().angle() <= initialDirection + 90.005f && car.getVelocity().angle() >= initialDirection + 89.995f)){
			car.turn(0);
		}else{
			car.turn(1.0f);
		}
		
	}
	@Override
	public void turnRight(){
		if((car.getVelocity().angle() >= initialDirection - 90.005f && car.getVelocity().angle() <= initialDirection - 89.995f)){
			car.turn(0);
		}else{
			car.turn(-1.0f);
		}
	}
	
	@Override
	public void next(float delta, trafficlightColor tColor, Point2D.Double nextTrafficlight){
		if(code == 0){//goStraight
			super.goStraight(delta);
		}else if(code == 1){//turnLeft
			turnLeft();
			super.goStraight(delta / 5);
		}else{//code == 2 turnRight
			turnRight();
			super.goStraight(delta / 2.25f);
		}
		
	}
}
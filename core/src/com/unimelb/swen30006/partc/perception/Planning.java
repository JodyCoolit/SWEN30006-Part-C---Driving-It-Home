package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

public class Planning implements IPlanning {
	
	private Car car;
	private Road[] roads;
	private Point2D.Double nextTrafficlight = null;
	protected enum carDirections {North,South,West,East}
	protected enum trafficlightColor {Red,Green,Yellow}
	private trafficlightColor tColor;
	private Instruction instruction = null;
	private SimpleRoutingAlgorithm algorithm = new SimpleRoutingAlgorithm();
	private Point2D.Double destination =new Point2D.Double(400,200);
	
	public Planning(Car car, Road[] roads){
		this.car = car;
		this.roads = roads;
	}
	
	@Override
	public boolean planRoute(Double destination) {
		// TODO Auto-generated method stub
		this.destination = destination;
		return false;
	}

	@Override
	public void update(PerceptionResponse[] results, int visibility, float delta) {
		if(instruction == null){
			instruction = new RoadInstruction(this.car, roadAtPoint(car.getPosition()));//since car is spawn on the road
			try{
				nextTlight(results);
			}catch(Exception e){
				//System.out.println("No more trafficlight");
			}
		}
		
		if(inIntersection(results)){//in the intersection
			if(!(instruction instanceof IntersectionInstruction)){
				int insCode = algorithm.runAlgorithm(car, destination);
				instruction = new IntersectionInstruction(this.car, insCode);
				System.out.println("intersection");
			}
		}else{//on the road
			if(!(instruction instanceof RoadInstruction)){
				instruction = new RoadInstruction(this.car, roadAtPoint(car.getPosition()));	
				System.out.println("road");
			}
			try{
				nextTlight(results);
			}catch(Exception e){
				//System.out.println("No more trafficlight");
			}
		}
		
		instruction.next(delta,tColor,nextTrafficlight);
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
		System.out.println("road is null");
		return null;
	}
	
	public Road[] roadsAroundPoint(Point2D.Double pos, int visibility){
		float xmin = (float) pos.x - visibility;
		float xmax = (float) pos.x + visibility;
		float ymin = (float) pos.y - visibility;
		float ymax = (float) pos.y + visibility;
		Rectangle2D.Double rect = new Rectangle2D.Double();
		rect.setFrameFromDiagonal(xmin, ymin, xmax, ymax);

		ArrayList<Road> visibleRoads = new ArrayList<Road>();
		for(Road r: this.roads){
			if(r.overlaps(rect)){
				visibleRoads.add(r);
			}
		}
		return visibleRoads.toArray(new Road[visibleRoads.size()]);
	}
	
	int secondHighestIndex(double... nums) {
		int index1 = -1;
		int index2 = -1;
		double high1 = -1;
		double high2 = -1;
	    for (int i = 0;i < nums.length;i++) {
	      if (nums[i] > high1) {
	        high2 = high1;
	        index2 = index1;
	        high1 = nums[i];
	        index1 = i;
	      } else if (nums[i] > high2) {
	        high2 = nums[i];
	        index2 = i;
	      }
	    }
	    return index2;
	}
	
	public void nextTlight(PerceptionResponse [] results) throws NullPointerException{
		
		double [] distance = new double[4];
		PerceptionResponse [] lights = new PerceptionResponse[4];
		int i = 0;
		for(PerceptionResponse pr : results){
			if(pr.objectType == Classification.TrafficLight){
				
				double tempDistance = Point2D.distance(car.getPosition().getX(), car.getPosition().getY(), pr.position.x, pr.position.y);
				
				if (tempDistance < 50){
					distance[i] = tempDistance;
					lights[i] = pr;
					/*
					if(temp > furthest){
						sndFurthest = furthest;
						furthest = temp;
						
						//nextTrafficlight = pr.position;
						furthestPr = pr;
						System.out.println(pr.position);
					}
					*/
					
					i++;
				}
			}
		}
		
		// Wait until all 4 traffic lights in an intersection are detected
		if (i < 4)
			return;
		
		int index = secondHighestIndex(distance);
		PerceptionResponse tempPr = lights[index];
		nextTrafficlight = tempPr.position;
		//System.out.println(nextTrafficlight);
		
		if(tempPr.information.get("state").toString().equals( "ff0000ff")){//red
			tColor = trafficlightColor.Red;
		}else if(tempPr.information.get("state").toString().equals("00ff00ff")){//green
			tColor = trafficlightColor.Green;
		}else{//ffff00ff
			tColor = trafficlightColor.Yellow;
		}
	}
	
	
	public boolean inIntersection(PerceptionResponse [] results){
		double distance = Math.sqrt(5) /2 * roads[0].getWidth() + 5;//roads[0].getWidth is the length of intersection edge
		
		double sX = 10000,sY = 10000,bX = 0,bY = 0;//smallest xy biggest xy
		for (PerceptionResponse pr : results){
			if(pr.objectType == pr.objectType.TrafficLight){
				if (Point2D.distance(car.getPosition().x, car.getPosition().y, pr.position.x, pr.position.y) < distance){
					sX = sX < pr.position.x ? sX : pr.position.x;
					sY = sY < pr.position.y ? sY : pr.position.y;
					bX = bX > pr.position.x ? bX : pr.position.x;
					bY = bY > pr.position.y ? bY : pr.position.y;
				}
			}
		}
		
		if(car.getPosition().x > sX && car.getPosition().x < bX && car.getPosition().y > sY && car.getPosition().y < bY){
			return true;
		}else{
			return false;
		}
	}
	
}

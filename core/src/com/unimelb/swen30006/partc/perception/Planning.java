package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
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
		if(instruction == null){
			instruction = new RoadInstruction(this.car, roadAtPoint(car.getPosition()));//since car is spawn on the road
			try{
				nextTlight(results);
			}catch(Exception e){
				System.out.println("No more trafficlight");
			}
		}
		
		if(inIntersection(results)){//in the intersection
			if(!(instruction instanceof IntersectionInstruction)){
				instruction = new IntersectionInstruction(this.car);
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
				System.out.println("No more trafficlight");
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
	
	public void nextTlight(PerceptionResponse [] results) throws NullPointerException{
		
		double distance = Float.MAX_VALUE;
		PerceptionResponse tempPr = null;
		for(PerceptionResponse pr : results){
			if(pr.objectType == pr.objectType.TrafficLight){
				double temp = Point2D.distance(car.getPosition().getX(), car.getPosition().getY(), pr.position.x, pr.position.y);
				if(temp < distance){
					distance = temp;
					nextTrafficlight = pr.position;
					tempPr = pr;
				}
			}
		}

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

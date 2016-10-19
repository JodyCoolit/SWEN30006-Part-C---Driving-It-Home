package com.unimelb.swen30006.partc.perception;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.partc.core.objects.Car;

public interface RoutingAlgorithm {
	public int runAlgorithm(Car car, Point2D.Double distination);
	
	public float calculateETA(Point2D.Double carPosition, Point2D.Double distination);
}

package com.gtnightrover.visualizer;

import java.io.IOException;

public interface PointsStream {
	
	public boolean hasPoints() throws Exception;
	
	public double[][] getPoints() throws Exception;

}

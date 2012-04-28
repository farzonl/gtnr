package com.gtnightrover.lidar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.gtnightrover.dfrduino.RoverCommand;

public class Path {

	public static final Point origin = new Point(0,0);
	
	private Point goal;
	
	public Path(Point goal) {
		this.goal = goal;
	}
	
	public List<RoverCommand> getCommands() {
		if (goal == null)
			return null;
		List<RoverCommand> commands = new ArrayList<RoverCommand>();
		RoverCommand turning = RoverCommand.turnToPoint(origin, goal);
		if (turning  != null)
			commands.add(turning);
		commands.add(RoverCommand.travelToPoint(origin, goal));
		return commands;
	}
}

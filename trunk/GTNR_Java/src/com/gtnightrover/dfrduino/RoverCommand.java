package com.gtnightrover.dfrduino;

import java.awt.Point;


public class RoverCommand {

	public static final byte sync = (byte)0xFF;
	
	private SerialProtocol command;
	private int data;
	private SerialProtocol data2;
	
	public RoverCommand(SerialProtocol command) {
		this(command, 0);
	}
	
	public RoverCommand(SerialProtocol command, int data) {
		this(command, data, SerialProtocol.NA);
	}
	
	public RoverCommand(SerialProtocol command, int data, SerialProtocol data2) {
		this.command = command;
		this.data = data;
		this.data2 = data2;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{ -1, ");
		sb.append((char)command.ordinal()).append(", ");
		sb.append(data).append(", ");;
		sb.append(data2.ordinal() == 0 ? 0 : (char)data2.ordinal()).append(" }");;
		return sb.toString();
	}
	
	public byte[] getBytes() {
		byte[] arr = new byte[5];
		arr[0] = sync;
		arr[1] = (byte)command.ordinal();
		arr[2] = (byte)((data >> 8) & 0xFF);
		arr[3] = (byte)(data & 0xFF);
		arr[4] = (byte)data2.ordinal();
		return arr;
	}
	
	public static RoverCommand turnToPoint(Point start, Point goal) {
		double _x = goal.x - start.x;
		double _y = goal.y - start.y;
		int deg = (int)Math.toDegrees(Math.atan2(_y, _x));
		SerialProtocol com = SerialProtocol.RHT;
		if (deg < 0) {
			com = SerialProtocol.LFT;
			deg = Math.abs(deg);
		}
		return new RoverCommand(com, deg, SerialProtocol.MOTOR_CONTROLLER);
	}
	
	public static RoverCommand travelToPoint(Point start, Point goal) {
		double _x = goal.x - start.x;
		double _y = goal.y - start.y;
		int dist = (int)Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2));
		return new RoverCommand(SerialProtocol.FWD, dist, SerialProtocol.MOTOR_CONTROLLER);
	}
}

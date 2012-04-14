package com.gtnightrover.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.util.ArrayList;

import com.gtnightrover.Graph.Point;

public class SerialWriteServer {

	
	public static ArrayList<byte[]> convert (ArrayList<Point> points) 
	{
		
		ArrayList<byte[]> store = new ArrayList<byte[]>();
		for(int i = 0; i < points.size()-1; i++)
		{
			int deltaX  = points.get(i+1).getX() - points.get(i).getX();
			int deltaY  = points.get(i+1).getX() - points.get(i).getX();
			byte[] byteMag = new byte[3];
			byte[] byteDeg = new byte[3];
			byteDeg[0] = byteMag[0] = -1;
			byteMag[2] = (byte) Math.sqrt(deltaX*deltaX*deltaY*deltaY);
			byteDeg[2] = (byte) Math.abs(Math.round(Math.toDegrees(Math.atan2(deltaY,deltaX))));
			
			if(deltaX > 0)
				byteDeg[1] = (byte) SerialProtocol.RHT.ordinal();
			else
				byteDeg[1] = (byte) SerialProtocol.LFT.ordinal();
			
			if(deltaY > 0 || deltaX > 0)
				byteMag[1] = (byte) SerialProtocol.FWD.ordinal();
			
			else if(deltaY < 0 || deltaX < 0)
				byteMag[1] = (byte) SerialProtocol.REV.ordinal();
			else
				byteMag[1] = (byte) SerialProtocol.STOP.ordinal();
			
			
			store.add(byteDeg);
			store.add(byteMag);
				
			
		}
		return store;
	}
	
	
	
	public static void main(String args[]) throws Exception
	{
		String portName = "";
		boolean listening = true;
		int speed = 9600;
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
		SerialPort port = (SerialPort) portId.open("serial talk", 4000);
		InputStream serialStream = port.getInputStream();
		
		System.out.println("connected to: "+port.getName());
		//TODO make this a call to the graph algorithm
		ArrayList<Point> points = new ArrayList<Point>();
		ArrayList<byte[]> 	writeStream = convert(points);
		while(listening)
		{
			if(serialStream.available() == 0)
			{
				for(int i = 0; i < writeStream.size(); i++)
					SerialComm.write(writeStream.get(i), port, speed);
			}
			else
			{
				
			}
		}
		port.close();
		
	}
}

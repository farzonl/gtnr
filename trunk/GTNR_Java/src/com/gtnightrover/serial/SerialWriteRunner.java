package com.gtnightrover.serial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.gtnightrover.Graph.Point;
import com.gtnightrover.lidar.LidarComm;
import com.gtnightrover.serial.SerialProtocol;

public class SerialWriteRunner {

	private byte[] byteArr;
	String portName;
	int speed;
	LidarComm comRW;
	SerialWriteRunner(String portName, int speed)
	{
		this.portName = portName;
		this.speed = speed;

		byteArr = new byte[5];
		try {
			this.comRW = new LidarComm(portName,speed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<byte[]> convert ( ArrayList<Point> points,byte sync,byte loc) 
	{

		ArrayList<byte[]> store = new ArrayList<byte[]>();
		for(int i = 0; i < points.size()-1; i++)
		{
			int deltaX  = points.get(i+1).getX() - points.get(i).getX();
			int deltaY  = points.get(i+1).getY() - points.get(i).getY();
			byte[] byteMag = new byte[5];
			byte[] byteDeg = new byte[5];
			short depth  = (short) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			short degree = (short) Math.abs(Math.round(Math.toDegrees(Math.atan2(deltaY,deltaX))));
			byteDeg[0]   = byteMag[0]  = sync;
			byteMag[2]   = (byte) (depth >> 8);
			byteMag[3]   = (byte) (depth & 0xFF);
			byteDeg[4]   = byteMag[4]  = loc;
			byteDeg[2]   = (byte) (degree >> 8);
			byteDeg[3]   = (byte) (degree & 0xFF);


			if(deltaX > 0)
				byteDeg[1] = (byte) SerialProtocol.RHT.getChar();
				//byteDeg[1] = (byte) SerialProtocol.RHT.ordinal();
			else if (deltaX < 0)
				byteDeg[1] = (byte) SerialProtocol.LFT.getChar();
				//byteDeg[1] = (byte) SerialProtocol.LFT.ordinal();
			else
				byteDeg[1] = (byte) SerialProtocol.STOP.getChar();
			
			if(deltaY > 0)
				 byteMag[1] = (byte) SerialProtocol.FWD.getChar();
				//byteMag[1] = (byte) SerialProtocol.FWD.ordinal();

			else if(deltaY < 0)
				byteMag[1] = (byte) SerialProtocol.REV.getChar();
				//byteMag[1] = (byte) SerialProtocol.REV.ordinal();
			else
				byteMag[1] = (byte) SerialProtocol.STOP.getChar();
				//byteMag[1] = (byte) SerialProtocol.STOP.ordinal();


			store.add(byteDeg);
			store.add(byteMag);


		}
		return store;
	}

	public void convert_write(ArrayList<Point> points, byte sync, byte loc )
	{
		ArrayList<byte[]> write_list = convert (points,sync,loc);
		for(int i = 0; i < write_list.size(); i++)
		{
			try{
				comRW.write(write_list.get(i));
				Thread.sleep(100);
				while(comRW.available() == 0);
				System.out.println("Expected: "+Arrays.toString(byteArr));
				System.out.println(Arrays.toString(comRW.read()));
				System.out.println("Done reading");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void protocol_write(int cmd, short depth,byte sync, byte loc )
	{
		byteArr[0] = sync;
		byteArr[2] = (byte) (depth >> 8);
		byteArr[3] = (byte) (depth & 0xFF);
		byteArr[4] = loc;
		try {
			//forward
			if (cmd==0)
			{
				System.out.println("FWD");
				//this.byteArr[1] = (byte) 'f';
				this.byteArr[1] = (byte) SerialProtocol.FWD.ordinal();
			}
			//down arrow -> rev
			else if (cmd==1)
			{
				System.out.println("REV");
				//this.byteArr[1] = (byte) 'b';
				this.byteArr[1] = (byte) SerialProtocol.REV.ordinal();
			}
			// right arrow
			else if (cmd==2)
			{
				System.out.println("RHT");
				//this.byteArr[1] = (byte) 'r';
				this.byteArr[1] = (byte) SerialProtocol.RHT.ordinal();
			}
			//left
			else
			{
				System.out.println("LFT");
				//this.byteArr[1] = (byte) 'l';
				this.byteArr[1] = (byte) SerialProtocol.LFT.ordinal();
			}
			/*SerialComm.write(byteArr, port, speed);
				read();*/
			comRW.write(byteArr);
			Thread.sleep(100);
			while(comRW.available() == 0);
			System.out.println("Expected: "+Arrays.toString(byteArr));
			System.out.println(Arrays.toString(comRW.read()));
			System.out.println("Done reading");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception
	{
		String portName = "/dev/ttyACM0";
		boolean listening = true;
		int speed = 9600;
		SerialWriteRunner swrite2 = new SerialWriteRunner(portName,speed);
		while(listening)
		{
			Scanner scan = new Scanner(System.in);
			swrite2.protocol_write(Integer.parseInt(scan.nextLine()), Short.MAX_VALUE,(byte)-1, (byte) 0 );
		}

	}
}

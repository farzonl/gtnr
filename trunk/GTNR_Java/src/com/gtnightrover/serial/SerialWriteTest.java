package com.gtnightrover.serial;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.gtnightrover.dfrduino.SerialProtocol;
import com.gtnightrover.lidar.LidarComm;


public class SerialWriteTest {

	private byte[] byteArr;
	String portName;
	int speed;
	LidarComm comRW;
	SerialWriteTest(String portName, int speed)
	{
		this.portName = portName;
		this.speed = speed;
		//byte 0 - sync byte
		//byte 1 - cmd  byte
		//byte 2 - MS byte 
		//byte 3   LS byte
		//byte 4   loc byte
		byteArr = new byte[5];
		byteArr[0] = -1;
		byteArr[3] = byteArr[2] = 1;
		byteArr[4] = 0;
		try {
			this.comRW = new LidarComm(portName,speed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*private void initializeCom() throws Exception
	{
		this.portId = CommPortIdentifier.getPortIdentifier(portName);
		this.port = (SerialPort) portId.open("serial talk", 4000);
		this.serialStream = port.getInputStream();
		System.out.println("connected to: "+port.getName());
	}
	private void read() throws Exception
	{
		while (serialStream.available() == 0);
		while (serialStream.available() > 0) {
				System.out.println(serialStream.read());
		}
	}*/

	public static void main(String args[]) throws Exception
	{
		String portName = "/dev/ttyACM0";
		boolean listening = true;
		int speed = 9600;
		SerialWriteTest swrite = new SerialWriteTest(portName,speed);
		while(listening)
			swrite.keyPress();
		
	}

	public void keyPress()
	{
		Scanner scan = new Scanner(System.in);
		System.out.print("Type: ");
		int choice = Integer.parseInt(scan.nextLine());
		try {
				//forward
				if (choice==1)
				{
					System.out.println("FWD");
					 //this.byteArr[1] = (byte) 'f';
					this.byteArr[1] = (byte) SerialProtocol.FWD.ordinal();
				}
				//down arrow -> rev
				else if (choice==2)
				{
					System.out.println("REV");
					//this.byteArr[1] = (byte) 'b';
					this.byteArr[1] = (byte) SerialProtocol.REV.ordinal();
				}
				// right arrow
				else if (choice==3)
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

}


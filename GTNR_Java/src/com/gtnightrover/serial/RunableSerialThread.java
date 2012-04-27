package com.gtnightrover.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gtnightrover.networking.MultiServer;

public class RunableSerialThread implements Runnable{
	
	public static ExecutorService executorService =
			Executors.newSingleThreadExecutor();
	
	private String portName;
	private int speed;
	private int[] depth_arr;
	public static int degree; 
	public RunableSerialThread(String portName, int speed, int depth_arr[])
	{
		this.portName = portName;
		this.speed = speed;
		this.depth_arr = depth_arr;
		RunableSerialThread.degree = 0;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Using port: " + portName);
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(portName);

			SerialPort port = (SerialPort) portId.open("serial talk", 4000);

			if (port != null) {
				Scanner input = new Scanner(port.getInputStream());
				port.setSerialPortParams(speed, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				//long time = System.currentTimeMillis();
				//long lapse = System.currentTimeMillis() - time;
				while (input.hasNext()) {
					String input_str = input.nextLine();
					//System.out.println((input_str));
					int input_int = Integer.parseInt(input_str);
					if(degree != depth_arr.length)
					{
						depth_arr[degree] = input_int*10;
						degree++;
					}
					else
					{
						System.out.println("ready");
						MultiServer.send_depth_arr = new ArrayList<Integer>();
						for(int i = 0; i < depth_arr.length; i++) MultiServer.send_depth_arr.add(depth_arr[i]);
						Thread.sleep(3000);
						degree = 0;
						
					}
					//lapse = System.currentTimeMillis() - time;
				}
				port.close();
			} else {
				System.out.println("Port was null...");
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

}

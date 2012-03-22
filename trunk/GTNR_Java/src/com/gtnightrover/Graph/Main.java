package com.gtnightrover.Graph;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.Scanner;

import javax.swing.JFrame;


import com.gtnightrover.serial.SerialComm;
public class Main {
	
	public static void readPort(String portName, int speed, int depth_arr[])
			throws Exception {
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
					int degree = 0;
					while (input.hasNext()) {
							String input_str = input.nextLine();
							System.out.println((input_str));
							int input_int = Integer.parseInt(input_str);
							if(degree != depth_arr.length)
							{
								depth_arr[degree] = input_int*10;
								degree++;
							}
							else break;
						//lapse = System.currentTimeMillis() - time;
					}
					port.close();
				} else {
					System.out.println("Port was null...");
				}
		}
	
	public static void main(String[] args) 
	{
		SerialComm.listPorts();
		PathBuilder path = null;
		int[] depth_arr = new int[360];
		
		/*Thread depth_update = new Thread()
		{
			private PathBuilder path;
			private int[] depth_arr;
			
			public void define(PathBuilder path, int[] depth_arr)
			{
				this.path = path;
				this.depth_arr = depth_arr;
			}
			public void run()
			{
				try
				{
					readPort("/dev/ttyACM0", 9600, depth_arr);
				} 
				catch(InterruptedException e) 
				{
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally 
				{
					path = new PathBuilder(depth_arr);
				}
			}
		};
		depth_update.start();*/
		
		try {
			readPort("/dev/ttyACM0", 9600, depth_arr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = new PathBuilder(depth_arr);
		
		//System.out.println(path.graph.toString());
		JFrame frame = new JFrame("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add( new PathDrawer(path.getRobo_pos(),path.getDepth_pos()));
		frame.pack(); 
		frame.setVisible(true);
	}

}

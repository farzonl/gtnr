package com.gtnightrover;

import com.gtnightrover.lidar.LidarComm;
import com.gtnightrover.lidar.LidarSerialStream;
import com.gtnightrover.visualizer.Graph;
import com.gtnightrover.visualizer.GraphWindow;
import com.gtnightrover.visualizer.StreamManager;

public class Main {

	/**
	 * To setup the hardware for this program:
	 * 
	 *  1. Double check connections:
	 *  	a) Lidar power to 5V and GND on Mega Board, and lidar data
	 *     		to RX1 pin.
	 *      b) Lidar motor red to M1+, black to M1-.
	 *      c) Power from power supply to motor terminal on DFRduino (M_VIN, GND)
	 *      d) Both Arduino boards are connected with USB.
	 * 	1. Load the 'LidarDriverTest' sketch onto a DFRduino using
	 * 		'Arduino Duemilanove w/ ATmega328 on /dev/USB#'
	 *  2. Turn on power supply to 11V. Make sure the 'Output On/Off' 
	 *    	button is lit.
	 *  3. Turn on lidar power switch (on 5V line from mega).
	 *  4. Start the GUI and note speed printing to console. Speed should settle
	 *    	to 0x4d## in less than 30 seconds. If speeds is greater than
	 *    	0x50 after setup time something is wrong.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// Set up GUI
		Graph g = new Graph(800,600,-12000,-12000,12000,12000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
		
		// Declare and initialize serial communication with lidar driver. This
		// is required to optimize lidar speed.
		/* FIXME: 
		LidarComm dfr = new LidarComm("/dev/ttyUSB0", 9600);
		/**/
		LidarComm dfr = null;
		
		// Declare and initialize serial communication with lidar module.
		// Note the LidarComm object being passed in. It is optional, but required
		// for speed control.
		/* FIXME:
		LidarSerialStream lss = new LidarSerialStream("/dev/ttyACM0", 115200, dfr);
		/**/
		LidarSerialStream lss = new LidarSerialStream(null, 115200, dfr);
		lss.start();
		
		// Create the thread which will automatically update the GUI with lidar data
		StreamManager sm = new StreamManager(g, f, lss);
		sm.dfr = dfr;
		sm.start();
	}

}

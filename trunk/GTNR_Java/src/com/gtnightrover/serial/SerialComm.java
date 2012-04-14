package com.gtnightrover.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class SerialComm {

	Scanner input;
	OutputStreamWriter output;
	SerialPort port;
	

	public SerialComm(String portName, int speed) throws Exception {
		CommPortIdentifier portId = CommPortIdentifier
				.getPortIdentifier(portName);

		port = (SerialPort) portId.open("serial talk", 4000);
		input = new Scanner(port.getInputStream());
		output = new OutputStreamWriter(port.getOutputStream());
		port.setSerialPortParams(speed, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	}
	
	public boolean hasNextByte() {
		return input.hasNextByte();
	}
	
	public Byte nextByte() {
		return input.nextByte();
	}
	
	public boolean hasNext() {
		return input.hasNext();
	}
	
	public boolean NextInt() {
		return input.hasNextInt();
	}
	
	public boolean hasNextLine() {
		return input.hasNextLine();
	}
	
	public String getNext() {
		return input.hasNext() ? input.next() : null;
	}
	
	public Integer getNextInt() {
		return input.hasNextInt() ? input.nextInt() : null;
	}
	
	public String getNextLine() {
		return input.hasNextLine() ? input.nextLine() : null;
	}
	
	public boolean canWrite(){
		return port != null;
	}
	
	public boolean write(String msg) {
		if (msg != null) {
			try {
				output.append(msg);
				output.flush();
				return true;
			} catch (IOException e) {
			}
		}
		return false;
	}
	
	public void close() {
		if (port != null) {
			port.close();
			port = null;
		}
	}

	public static void readPort(String portName, int speed, int limit)
			throws Exception {
		System.out.println("Using port: " + portName);
		CommPortIdentifier portId = CommPortIdentifier
				.getPortIdentifier(portName);

		SerialPort port = (SerialPort) portId.open("serial talk", 4000);

		if (port != null) {
			java.io.InputStream input = port.getInputStream();
			port.setSerialPortParams(speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			long time = System.currentTimeMillis();
			long lapse = System.currentTimeMillis() - time;
			while (lapse < limit) {
				while (input.available() > 0)
					System.out.print((char) (input.read()));
				lapse = System.currentTimeMillis() - time;
			}
			port.close();
		} else {
			System.out.println("Port was null...");
		}
	}

	public static void readPort2(String portName, int speed, int limit)
			throws Exception {
		System.out.println("Using port: " + portName);
		CommPortIdentifier portId = CommPortIdentifier
				.getPortIdentifier(portName);

		SerialPort port = (SerialPort) portId.open("serial talk", 4000);

		if (port != null) {
			Scanner input = new Scanner(port.getInputStream());
			port.setSerialPortParams(speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			long time = System.currentTimeMillis();
			long lapse = System.currentTimeMillis() - time;
			while (lapse < limit) {
				if (input.hasNextLine())
					System.out.println((input.nextLine()));
				lapse = System.currentTimeMillis() - time;
			}
			port.close();
		} else {
			System.out.println("Port was null...");
		}
	}
	
	public static void readPort3(String portName, int speed)
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
				while (input.hasNext()) {
					if (input.hasNextLine())
						System.out.println((input.nextLine()));
					//lapse = System.currentTimeMillis() - time;
				}
				port.close();
			} else {
				System.out.println("Port was null...");
			}
	}

	public static void listPorts() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		while (ports.hasMoreElements()) {
			CommPortIdentifier port = ports.nextElement();
			if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println(port.getName());
			}
		}
	}
	
	public static ArrayList<String> listPorts2() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> ret_arr_list = new ArrayList<String>() ;
		while (ports.hasMoreElements()) {
			CommPortIdentifier port = ports.nextElement();
			if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				ret_arr_list.add(port.getName());
			}
		}
		return ret_arr_list;
	}
	
	public static void write(byte[] data, String portName, int speed) throws Exception
	{
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

		SerialPort port = (SerialPort) portId.open("serial talk", 4000);

		if (port != null) {
			java.io.OutputStream output = port.getOutputStream();
			port.setSerialPortParams(speed, 
				       SerialPort.DATABITS_8, 
				       SerialPort.STOPBITS_1, 
				       SerialPort.PARITY_NONE);
			output.write(data);
		}
		port.close();
	}
	
	public static void write(byte[] data, SerialPort port, int speed) throws Exception
	{

		if (port != null) {
			java.io.OutputStream output = port.getOutputStream();
			port.setSerialPortParams(speed, 
				       SerialPort.DATABITS_8, 
				       SerialPort.STOPBITS_1, 
				       SerialPort.PARITY_NONE);
			output.write(data);
		}
	}

	public static void main(String[] args) throws Exception {
		listPorts();
		//readPort2("/dev/ttyACM0", 115200, 8000);
		//readPort2("/dev/ttyACM0", 9600, 2000);
		//readPort2("/dev/ttyUSB0", 9600, 2000);
		 readPort3("/dev/ttyACM0", 9600);
		//readPort3("/dev/ttyUSB0", 9600);
	}
	
}

package com.gtnightrover.networking;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

import com.gtnightrover.serial.RunableSerialThread;

public class MultiServerThread extends Thread
{
	private Socket socket = null;
	public MultiServerThread(Socket socket)
	{
		super("MultiServerThread");
		this.socket = socket;
		System.out.println("client socket created");
	}

	public void run() {

		System.out.println("run started");

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));

			String inputLine;
			System.out.println("printwriter and bufferreader created");
			while ((inputLine = in.readLine()) != null) 
			{
				System.out.println("input is: "+inputLine);

				if(inputLine.equals("depth_arr"))
				{
					System.out.println("you selected Depth");
					ObjectOutputStream outStream =
							new  ObjectOutputStream(socket.getOutputStream());
					//System.out.println(Arrays.toString(MultiServer.depth_arr));
					ArrayList<Integer> sendList = null;
					if(MultiServer.isSerial)
						sendList = MultiServer.sendDepth(MultiServer.stream.getDistances());
					else
						sendList = MultiServer.sendDepth();
						
					while(true)
					{
						if(sendList != null && sendList.size() == 360) 
						{
							System.out.println(Arrays.toString(sendList.toArray()));
							outStream.writeObject(sendList);
							break;
						}
					}
				}

				else if(inputLine.equals("email"))
				{
					System.out.println("you selected email");
					out.println("READY");
					/* delimiter */
					String delimiter = ",";
					String array_vals = in.readLine();
					String[] recv = array_vals.split(delimiter);
					//Socket_Mail.main(args);
					String args[] = MultiServer.setEmail(recv);
					Socket_Mail.caller(args);
				}
				else if(inputLine.equals("Text"))
				{
					System.out.println("you selected Text");
				}
				else
				{
					System.err.println("Bad data");
					break;
				}

			}
			System.out.println("closing socket");
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("run ended");
	}
}

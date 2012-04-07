package com.gtnightrover.networking;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

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
		           outStream.writeObject(MultiServer.sendDepth());
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

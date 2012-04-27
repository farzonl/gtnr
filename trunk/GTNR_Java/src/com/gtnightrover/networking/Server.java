package com.gtnightrover.networking;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


public class Server implements Runnable {
	
	  private static ExecutorService executorService =
    Executors.newSingleThreadExecutor();

	public static void main(String[] args) throws Exception {
		// Start a server to listen for a client
		System.out.println("running");
		executorService.submit(new Server());
	}
	
    @Override
	public void run() {
    	
    	ArrayList<Integer> depth_arr = new ArrayList<Integer>();
		for(int i = 0; i < 360;i++)
		{
			//depth_arr[i] = (int) (Math.random() * 200)+50;
			depth_arr.add(150);
		}
		
        try {
        	 /*ArrayList<Integer> integers =
                 new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));*/
            ServerSocket server = new ServerSocket(4444);
            Socket clientSocket = server.accept();
            ObjectOutputStream outStream =
                    new  ObjectOutputStream(clientSocket.getOutputStream());
           outStream.writeObject(depth_arr);
           clientSocket.close();
            server.close();
            executorService.shutdown();
        } catch (IOException e) {
            // TODO: Write me
            throw new UnsupportedOperationException("Not written");
        } 
    }
}
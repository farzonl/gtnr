package com.gtnightrover.networking;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

import com.gtnightrover.Graph.PathBuilder;
import com.gtnightrover.lidar.LidarSerialStream;
import com.gtnightrover.serial.RunableSerialThread;
import com.gtnightrover.serial.SerialComm;
public class MultiServer {
	
	static int[] depth_arr = new int[360];
	public static ArrayList<Integer> send_depth_arr = null;
	public static LidarSerialStream stream = null;
	public static boolean isSerial;
	
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        System.out.println("Server Running");
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }
        
        
        ArrayList<String> comm_posts = SerialComm.listPorts2();
        if(null == comm_posts || comm_posts.isEmpty() || 
        		comm_posts.get(0) == null  || comm_posts.get(0) == "")
        {
        	send_depth_arr = sendDepth();
        	System.out.println("No serial ports available");
        	isSerial = false;
        }
        else
        {
        	System.out.println(comm_posts.toString());
             
			try {
				stream = new LidarSerialStream("/dev/ttyACM0",115200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             stream.start();
          //stream.getDistances();sendDepth
        	//RunableSerialThread.executorService.submit(new RunableSerialThread("/dev/ttyACM0", 9600, depth_arr));
             isSerial = true;
        }
		
        while (listening)
        	new MultiServerThread(serverSocket.accept()).start();

        serverSocket.close();
        RunableSerialThread.executorService.shutdown();
    }
    
    public static String[] setEmail(String[] recv)
    {
    	ArrayList<String> args = new ArrayList();
    	args.add("gtnr@gtnr.org");
    	args.add(recv[0]);
    	//this will need to call terminal eventually
    	///args.add("test email");
    	
    	String cmd = "";
    	if(recv[0].equals("CPU Perf"))
    			cmd = cmdRun("mpstat");
    	
    	else if(recv[0].equals("CPU Temp"))
    		cmd = cmdRun("sensors");
    		
    	else
    		cmd = "CPU Perf\n"+cmdRun("mpstat")+"\nCPU Temp\n"+cmdRun("mpstat");
    	
    	args.add(cmd);
    	
    	for(int i = 1; i < recv.length;i++) args.add(recv[i]);
    	args.trimToSize();
	
		return Arrays.asList(args.toArray()).toArray(new String[args.size()]);
    	
    }
    public static String cmdRun(String cmd)
    {    	
    	Runtime run = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = run.exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String retStr ="";
		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		try {
			while ((line=buf.readLine())!=null) {
				System.out.println(line);
				retStr += line+"\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return retStr;		
    }
    public static ArrayList<Integer>  sendDepth(int[] anArray)
    {
    	ArrayList<Integer> depth_arr = new ArrayList<Integer>();
    	for(int i = 0; i < 360;i++)
    	{
    		//depth_arr[i] = (int) (Math.random() * 200)+50;
    		depth_arr.add(anArray[i]);
    	}
    	return depth_arr;
    }
    
    public static ArrayList<Integer>  sendDepth()
    {
    	ArrayList<Integer> depth_arr = new ArrayList<Integer>();
    	for(int i = 0; i < 360;i++)
    	{
    		//depth_arr[i] = (int) (Math.random() * 200)+50;
    		depth_arr.add(150);
    	}
    	return depth_arr;
    }
    
    public static void call(String mainClassName, String[] args)
    {
        Class<?> mainClass = null;
        try {
          mainClass = Class.forName(mainClassName);
        }
        catch (Exception ex) {
          throw new IllegalArgumentException("class not found in your jar file " + mainClassName);
        }

        Method mainMethod = null;
        try {
          mainMethod = mainClass.getMethod("main", String[].class);
        }
        catch (Exception ex) {
          throw new IllegalArgumentException("class to launch (" + mainClassName + ") does not have a public static void main(String[]) method");
        }
         
        // and invoke main()
        try {
			mainMethod.invoke(null, (Object) args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
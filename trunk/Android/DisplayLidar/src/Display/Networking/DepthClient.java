package Display.Networking;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import Display.datastructures.Point;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class DepthClient implements Callable<ArrayList<Point>> {

	public static ExecutorService executorService =
			Executors.newSingleThreadExecutor();

	private ArrayList<Point> points = null;

	private String IP;

	public DepthClient(String IP)
	{
		if(IP.equals("localhost"))
			this.IP = "10.0.2.2";
		else
			this.IP = IP;

	}

	/*public ArrayList<Point> get_Point_Array_List()
		{
			return this.points;
		}*/

	private void create_Points(ArrayList<Integer> depth_list)
	{
		points = new ArrayList<Point>();
		for(int degree = 0; degree < depth_list.size();degree++)
		{

			double rad = (Math.PI*degree)/180;
			int x = (int) (depth_list.get(degree)*Math.cos(rad));
			int y = (int) (depth_list.get(degree)*Math.sin(rad));
			points.add(new Point(x,y));
		}
		Log.d("create points","complete");
		//main.depth_pos = (ArrayList<Point>) points.clone();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Point> call() throws Exception {

		Socket s = new Socket();
		//s.connect(new InetSocketAddress("localhost", 4444));

		try {
			s.connect(new InetSocketAddress(IP, 4444));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(s.isConnected())
			{

				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println("depth_arr");
				out.flush();
				//wait(100);
				Log.d("Client","Requested Depth");
				ObjectInputStream in = new ObjectInputStream(s.getInputStream());
				Log.d("Client"," inputstream complete");
				Object o = in.readObject();
				Log.d("got o:",o.toString());
				executorService.shutdown();
				create_Points((ArrayList<Integer>) o);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return points;
	}

}



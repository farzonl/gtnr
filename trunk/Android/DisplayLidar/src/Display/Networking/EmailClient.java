package Display.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

import android.util.Log;

public class EmailClient implements Runnable {

	private ArrayList<String> send_list;
	private String IP; 

	public EmailClient(ArrayList<String> send_list, String IP)
	{
		if(IP.equals("localhost"))
			this.IP = "10.0.2.2";
		else
			this.IP = IP;
		this.send_list = send_list;
		this.send_list.trimToSize();
	}

	@Override
	public void run() 
	{
		Log.d("Client"," Start Run");
		Socket s = new Socket();
		Log.d("Client"," Socket Created");
		//s.connect(new InetSocketAddress("localhost", 4444));
		try {
			s.connect(new InetSocketAddress(IP, 4444));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("Client","Socket Connected");
		try
		{
			if(s.isConnected())
			{
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println("email");
				out.flush();
				Log.d("Client","Server notified for email");
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				if(in.readLine().equals("READY"))
				{
					Log.d("Client","Server Ready for data");
					for(int i = 0; i < send_list.size();i++)
					{
						if(i < send_list.size()-1)
							out.print(send_list.get(i)+",");
						else
							out.println(send_list.get(i));
					}
				}

			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}

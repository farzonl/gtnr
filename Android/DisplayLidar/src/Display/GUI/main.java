package Display.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Display.Networking.DepthClient;
import Display.datastructures.Point;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class main extends Activity {

	private Button Display_btn;
	private EditText IP_text;
	String  IP_str;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.IP_text = (EditText)this.findViewById(R.id.editText1);

		this.Display_btn = (Button)this.findViewById(R.id.Disp_btn);
		this.Display_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				IP_str = IP_text.getText().toString();

				if(null == IP_str || IP_str.equals(""))
				{
					Toast.makeText(getApplicationContext(), "please give an IP",  Toast.LENGTH_SHORT).show();
					return;
				}

				//Toast.makeText(getApplicationContext(), "IP str: "+IP_str,  Toast.LENGTH_SHORT).show();
				if (DepthClient.executorService.isShutdown())
				{
					DepthClient.executorService = Executors.newSingleThreadExecutor();  
				}
				Future<ArrayList<Point>> f = DepthClient.executorService.submit(new DepthClient(IP_str));
				DepthClient.executorService.shutdown();


				Log.d("before","before depth call");
				//ArrayList<Point> depth_pos  = Client.points;
				ArrayList<Point> depth_pos = null;
				try {
					depth_pos = f.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (null == depth_pos)
				{
					Toast.makeText(getApplicationContext(), "server not available",  Toast.LENGTH_SHORT).show();
					return;
				}

				Log.d("ALPoint arrival","first depth "+depth_pos.get(0));


				//Intent Display_screen = new Intent("Display.GUI.StartDraw");
				Intent Tab_screen = new Intent(main.this, Display.GUI.TabDisplay.class);

				Tab_screen.putParcelableArrayListExtra("points", (ArrayList<Point>) depth_pos);
				Tab_screen.putExtra("IP", IP_str);
				//Display_screen.putExtra("java.util.ArrayList", depth_pos);
				startActivity(Tab_screen);

			}
		});

	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//finish();
	}

}
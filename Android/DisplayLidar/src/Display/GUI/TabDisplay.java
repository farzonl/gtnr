package Display.GUI;

import java.util.ArrayList;

import Display.datastructures.Point;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

public class TabDisplay extends TabActivity {
	ArrayList<Point> depth_pos;
	String IP;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    depth_pos = getIntent().getParcelableArrayListExtra("points");
	    Bundle extras = getIntent().getExtras();
	    IP        = extras.getString("IP");
	    //Toast.makeText(getApplicationContext(), IP,  Toast.LENGTH_SHORT).show();
	    Log.d("TabDisplay","before Display Intent");
	    Intent Display_screen = new Intent(TabDisplay.this, Display.GUI.TabDisplay.class);
    	
    	Display_screen.putParcelableArrayListExtra("points", (ArrayList<Point>) depth_pos);
    	Display_screen.setClass(this, StartDraw.class);
	    //intent = new Intent().setClass(this, StartDraw.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Lidar").setIndicator("Lidar",
	                      res.getDrawable(R.drawable.icon))
	                  .setContent(Display_screen);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, WebDisplay.class).putExtra("IP", IP);
	    spec = tabHost.newTabSpec("Web").setIndicator("Web",
	                      res.getDrawable(R.drawable.icon))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, EmailView.class).putExtra("IP", IP);
	    spec = tabHost.newTabSpec("emal").setIndicator("Email",
	                      res.getDrawable(R.drawable.icon))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, ControlView.class).putExtra("IP", IP);
	    spec = tabHost.newTabSpec("control").setIndicator("Controls",
	                      res.getDrawable(R.drawable.icon))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(4);
	}

}

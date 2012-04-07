package Display.GUI;

import java.util.ArrayList;

import Display.datastructures.DrawView;
import Display.datastructures.Point;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class StartDraw extends Activity {
    DrawView drawView;
    ArrayList<Point> depth_pos;
    
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StartDraw","before parcable");
        depth_pos = getIntent().getParcelableArrayListExtra("points");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(display.getHeight(),display.getWidth());
        drawView = new DrawView(this,depth_pos,size);
        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);

    }
}
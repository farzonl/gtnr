package Display.datastructures;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class DrawView extends View {
    
	Paint paint = new Paint();
    private Point robo_pos;
	private ArrayList<Point> depth_pos;
	private int width_half,height_half;
    public DrawView(Context context, ArrayList<Point> depth_pos, Point p)
	{
		super(context);
		//WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		//Display display = wm.getDefaultDisplay();
		this.width_half = p.getY()/2;
		this.height_half = p.getX()/2;
		this.robo_pos = new Point(0,0);
		this.depth_pos = depth_pos;
	  
	     
	}    

    public int randomColor()
    {
		Random rand = new Random();
		
        return(Color.rgb(rand.nextInt(256), 
                         rand.nextInt(256),
                         rand.nextInt(256)));
    }
    
    public void  draw_line(Canvas canvas, Point p1, Point p2, int c1)
	{
    	paint.setColor(c1);
		canvas.drawLine(p1.getX()+width_half,p1.getY()+height_half,p2.getX()+width_half,p2.getY()+height_half, paint);
	}
    
    @Override
    public void onDraw(Canvas canvas) {
            /*canvas.drawLine(0, 0, 20, 20, paint);
            canvas.drawLine(20, 0, 0, 20, paint);*/
            for(int i = 0; i < depth_pos.size()-1;i++)
    		{
    			draw_line(canvas,robo_pos, 
    					depth_pos.get(i),randomColor());
    			
    			
    			draw_line(canvas, depth_pos.get(i), 
    					depth_pos.get(i+1),randomColor());
    			
    			//System.out.println(depth_pos.get(i).getData().toString());
    			
    		}
    }

}
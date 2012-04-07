package Display.GUI;

import Display.GUI.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{

	private MediaPlayer splash_sound;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		splash_sound = MediaPlayer.create(this,R.raw.startup);
		splash_sound.start();
		Thread timer = new Thread()
		{
			public void run()
			{
				try
				{
					sleep(3000);
				} 
				catch(InterruptedException e) 
				{
					e.printStackTrace();
				}
				finally 
				{
					Intent StartScreen = new Intent("Display.GUI.main");
					startActivity(StartScreen);
				}
			}
		};
		timer.start();
		//String[] args = {"smalltalk@greple.com","smalltalk","farzonl@gmail.com","good","hi Farzon","3"};

		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		splash_sound.release();
		finish();
	}
	

}

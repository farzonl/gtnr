package Display.GUI;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import Display.Networking.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class EmailView extends Activity {

	private Button send_btn;
	private EditText Email_text;
	private Spinner s;
	String  IP_str, Email_str;
	ArrayList<String> str_arr;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		IP_str        = extras.getString("IP");
		setContentView(R.layout.email);
		this.Email_text = (EditText)this.findViewById(R.id.emailText);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.array_name, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

		s = (Spinner) findViewById( R.id.email_spin );
		s.setAdapter( adapter );
		this.send_btn = (Button)this.findViewById(R.id.emailSend);
		this.send_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Email_str = Email_text.getText().toString();

				if(null == IP_str || IP_str.equals(""))
				{
					Toast.makeText(getApplicationContext(), "please give an email to send to",  Toast.LENGTH_SHORT).show();
					return;
				}

				str_arr = new ArrayList<String>();
				str_arr.add(s.getSelectedItem().toString());
				str_arr.add(Email_str);
				
				Log.d("EmailView","Thread starting");

				if (DepthClient.executorService.isShutdown())
				{
					DepthClient.executorService = Executors.newSingleThreadExecutor();
					DepthClient.executorService.submit(new EmailClient(str_arr,IP_str));
					DepthClient.executorService.shutdown();
					Log.d("EmailView","Thread Shutting Down");
					Toast.makeText(getApplicationContext(), "Email Sent",  Toast.LENGTH_SHORT).show();
				}
				else
				{
					Log.d("EmailView","could not launch thread");
					DepthClient.executorService.shutdownNow();
				}

			}
		});

	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//finish();
	}

}
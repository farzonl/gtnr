package Display.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebDisplay extends Activity{
	WebView mWebView;
	String IP;
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.web);
	    //IP       = getIntent().getParcelableExtra("IP");
	    Bundle extras = getIntent().getExtras();
	    IP        = extras.getString("IP");
	    IP = IP+":8080";
	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl(IP);
	}
}

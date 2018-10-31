package edu.temple.munz.basicbrowser;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    Button goButton, backButton, forwardButton;
    TextView urlBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = findViewById(R.id.buttonGo);
        backButton = findViewById(R.id.buttonBack);
        forwardButton = findViewById(R.id.buttonForward);
        urlBar = findViewById(R.id.urlBar);


        Intent intent = getIntent();

        //Go button listener
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Msg:", "Go Button Clicked");
                Toast.makeText(MainActivity.this, "Thread running", Toast.LENGTH_LONG).show();

                new Thread() {
                    @Override
                    public void run() {
                        Log.d("Msg:", "Thread Running");


                        //get string put the string into a message object
                        URL url = null;
                        try {
                            //read URL
                            url = new URL(urlBar.getText().toString());

                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(
                                            url.openStream()));

                            StringBuilder sb = new StringBuilder();
                            String tmpString;
                            while ((tmpString = reader.readLine()) != null) {
                                sb.append(tmpString);
                            }
                            //put the URL in a message object and send it to response handler
                            Message msg = Message.obtain();
                            msg.obj = sb.toString();
                            Log.d("trying to load: ", msg.obj.toString());
                            responseHandler.sendMessage(msg);


                        } catch( Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }


    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Log.d("msg: ", "handleMessage called successfully");
            //Create a new WebViewFragment for the site, and show it in the FrameLayout onscreen
            WebViewFragment wvf = WebViewFragment.newInstance((String)message.obj);

            //create a fragment manager to put this new webview onscreen
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frameLayout, wvf).commit();

            return false;
        }
    });


}

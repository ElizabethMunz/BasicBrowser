package edu.temple.munz.basicbrowser;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button goButton, backButton, forwardButton;
    TextView urlBar;
    ViewPager viewPager;
    FragmentStatePagerAdapter fspa;

    ArrayList<Fragment> webViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = findViewById(R.id.buttonGo);
        backButton = findViewById(R.id.buttonBack);
        forwardButton = findViewById(R.id.buttonForward);
        urlBar = findViewById(R.id.urlBar);
        viewPager = findViewById(R.id.viewPager);

        //initialize dynamic list to store the webpages for the fragmentstatepageradapter
        webViewList = new ArrayList<>();

        //initialize the fragmentstatepageradapter
        fspa = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return webViewList.get(i);
            }

            @Override
            public int getCount() {
                return webViewList.size();
            }
        };
        viewPager.setAdapter(fspa);


        //Go button listener
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Msg", "Go Button Clicked");
                new Thread() {
                    @Override
                    public void run() {
                        Log.d("Msg", "Thread Running");
                        //read URL from user entry
                        String urlText = urlBar.getText().toString();

                        //make sure parseURL didn't return null
                        if(parseURL(urlText) != null) {
                            //put the URL's html data (returned by parseURL) into a string
                            String urlContents = parseURL(urlText);
                            //put the URL in a message object and send it to response handler
                            Message msg = Message.obtain();
                            String[] msgObjs = {urlContents, urlText};
                            msg.obj = msgObjs;
                            Log.d("trying to load", ((String[]) msg.obj)[0]);
                            responseHandler.sendMessage(msg);
                        }
                    }
                }.start();
            }
        });

        //go to previous page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the current view in the viewPager
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        //go to next page
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the current view in the viewPager
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
    }


    Handler responseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Log.d("msg", "handleMessage called successfully");
            //Create a new WebViewFragment for the site, and show it in the FrameLayout onscreen
            WebViewFragment wvf = WebViewFragment.newInstance( ((String[])message.obj)[0], ((String[])message.obj)[1]);

            //I DONT KNOW WHERE TO PUT THIS LINE:
            //urlBar.setText(wvf.url);

            //add the new webView to fspa & update it
            webViewList.add(wvf);
            fspa.notifyDataSetChanged();
            viewPager.setCurrentItem(fspa.getCount() - 1);
            return false;
        }
    });

    public String parseURL(String urlText) {
        //find out if we can make this text into a url
        URL url = null;
        try {
            url = new URL(urlText);
        } catch(MalformedURLException e) {
            //the url was missing or had an incorrect protocol, so append "http://"
            //(this assumes the site uses http)
            String correctedURL = "http://" + urlText;
            try {
                url = new URL(correctedURL);
                Log.d("Corrected URL", correctedURL); //for testing
            } catch (MalformedURLException e1) {
                //the URL is still messed up even with added protocol, so just print error and exit parseURL function
                Log.d("Couldn't correct url", correctedURL); //for testing
                return null;
            }
        }
        //turn the URL contents into a string for us to return and eventually pass to the webview
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                sb.append(tmpString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}

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

public class MainActivity extends AppCompatActivity implements WebViewFragment.WebViewFragmentInterface {


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


                //read URL from user entry
                String urlText = urlBar.getText().toString();
                urlText = parseURL(urlText);
                //create a new fragment if it doesn't exist already
                if(fspa.getCount() == 0) {
                    WebViewFragment wvf = WebViewFragment.newInstance(urlText);
                    //add the new webView to fspa & update it
                    webViewList.add(wvf);
                    fspa.notifyDataSetChanged();
                    //make sure the viewPager is showing the right tab:
                    viewPager.setCurrentItem(fspa.getCount() - 1);
                }
                //else, fragment with a webView exists, so just load the new website into it
                else {
                    ((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.loadUrl(urlText);
                }
            }
        });

        //go to previous page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the current view in the viewPager
                //viewPager.setCurrentItem(viewPager.getCurrentItem() - 1); THIS IS FOR SWITCHING TABS

                //if the webView object has a back history, go back & update URL bar to correct url
                if(((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.canGoBack()) {
                    ((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.goBack();
                    urlBar.setText(((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.getUrl());
                }
            }
        });
        //go to next page
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the current view in the viewPager
                //viewPager.setCurrentItem(viewPager.getCurrentItem() + 1); THIS IS FOR SWITCHING TABS
                if(((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.canGoForward()) {
                    ((WebViewFragment)fspa.getItem(viewPager.getCurrentItem())).webView.goForward();
                }
            }
        });
    }



    /**
     *
     * @param urlText
     * @return String containing the same urlText, a corrected version, with http:// appended, or null
     */
    public String parseURL(String urlText) {
        //find out if we can make this text into a url
        URL url = null;
        try {
            url = new URL(urlText);
            return urlText;
        } catch(MalformedURLException e) {
            //the url was missing or had an incorrect protocol, so append "http://"
            //(this assumes the site uses http)
            String correctedURL = "http://" + urlText;
            try {
                url = new URL(correctedURL);
                Log.d("Corrected URL", correctedURL); //for testing
                return correctedURL;
            } catch (MalformedURLException e1) {
                //the URL is still messed up even with added protocol, so just print error and exit parseURL function
                Log.d("Couldn't correct url", correctedURL); //for testing
                return null;
            }
        }
    }

}

package edu.temple.munz.basicbrowser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    WebView webView;

    ArrayList<Site> history;

    /*String html;
    public String url;
    public static final String HTML_KEY = "htmlkey";
    public static final String URL_KEY = "urlkey"; */

    public static final String SITE_KEY = "sitekey";

    public WebViewFragment() {
        // Required empty public constructor
    }

    //newInstance should only be called the FIRST time a fragment is created
    public static WebViewFragment newInstance(String html, String url) {
        WebViewFragment wvf = new WebViewFragment();
        Bundle b = new Bundle();
        /*b.putString(HTML_KEY, html);
        b.putString(URL_KEY, url);*/
        //make a temporary list containing only the initial Site
        ArrayList<Site> temp = new ArrayList<>();
        temp.add(new Site(html, url));
        //set the temp list as arguments of the fragment object
        b.putParcelableArrayList(SITE_KEY, temp);
        wvf.setArguments(b);
        return wvf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get arguments from the bundle created in newInstance
        if(getArguments() != null) {
            //html = getArguments().getString(HTML_KEY);
            //url = getArguments().getString(URL_KEY);

            //initialize the history arrayList
            history = getArguments().getParcelableArrayList(SITE_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);
        //find & prepare webView element
        webView = v.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //load the site from the current page in history into the webView
        Log.d("OnCreateView called", "in WebViewFragment");
        webView.loadData(history.get(0).html, "text/html", "UTF-8");
        //TODO: replace that get(0) with some way of getting the current webpage in history
        return v;
    }


    public void addSiteToHistory(String html, String url) {
        history.add(new Site(html, url));
    }

}

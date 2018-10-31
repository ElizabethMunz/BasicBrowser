package edu.temple.munz.basicbrowser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    WebView webView;
    String url;

    public static final String URL_KEY = "urlkey";

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String url) {
        WebViewFragment wvf = new WebViewFragment();
        Bundle b = new Bundle();
        b.putString(URL_KEY, url);
        wvf.setArguments(b);
        return wvf;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //get arguments from the bundle created in newInstance
        if(getArguments() != null) {
            url = getArguments().getString(URL_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);

        //prepare webView
        webView = v.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //load the site from the URL into the webView
        webView.loadData((String)url, "text/html", "UTF-8");
        return v;
    }

}

package edu.temple.munz.basicbrowser;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    Context parent;
    WebView webView;
    public String url;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = context;
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ((WebViewFragmentInterface)parent).webViewChange(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ((WebViewFragmentInterface)parent).webViewChange(url);
            }
        });

        //load the site from the URL into the webView
        webView.loadUrl(url);
        return v;
    }


    //I need this for some reason
    interface WebViewFragmentInterface {
        void webViewChange(String url);
    }

}

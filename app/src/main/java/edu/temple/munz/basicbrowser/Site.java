package edu.temple.munz.basicbrowser;

import android.os.Parcel;
import android.os.Parcelable;


//all objects in our webpage history in each WebViewFragment are Site objects

public class Site implements Parcelable { //has to implement Parcelable b/c I'm putting Site objects in a bundle

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(int size) {
            return new Site[size];
        }
    };


    //I should probably make these private and add getters/setters,
    //...but I'm not worried about security here and need to finish the functionality
    public String html;
    public String url;

    public Site(String html, String url)  {
        this.html = html;
        this.url = url;
    }

    protected Site(Parcel in) {
        html = in.readString();
        url = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(html);
        parcel.writeString(url);
    }
}

package sg.edu.np.mad.Assignment1;

import android.net.Uri;

public class Listings {

    public String id;
    public String title;
    public String desc;
    public Uri image;

    public Listings(){

    }

    public Listings(String id, String title, String desc, Uri image) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}

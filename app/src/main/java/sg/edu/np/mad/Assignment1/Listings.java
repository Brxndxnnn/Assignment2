package sg.edu.np.mad.Assignment1;

import android.net.Uri;

public class Listings {

    public String id;
    public String title;
    public String desc;
    public String imageUrl;

    public Listings(){

    }

    public Listings(String id, String title, String desc, String imageUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String  getImageUrl() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

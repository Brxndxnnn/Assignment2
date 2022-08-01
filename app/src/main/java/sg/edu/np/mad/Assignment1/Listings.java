package sg.edu.np.mad.Assignment1;

import android.net.Uri;

public class Listings {

    public String id;
    public String title;
    public String desc;
    public String poster;
    public String location;
    public String imageUrl;
    public Boolean isLike;


    public Listings(){

    }

    public Listings(String id, String title, String desc, String imageUrl, String poster, String location, Boolean isLike) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.poster = poster;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isLike = isLike;
    }

    //Getter & Setter
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {this.imageUrl = imageUrl;}

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {this.poster = poster;}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {this.location = location;}

    public Boolean getLike() {
        return isLike;
    }
}

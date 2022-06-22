package sg.edu.np.mad.Assignment1;

import java.sql.Blob;

public class Listings {

    public String id;
    public String title;
    public String desc;
    public String image;

    public Listings(){

    }

    public Listings(String id, String title, String desc, String image) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}

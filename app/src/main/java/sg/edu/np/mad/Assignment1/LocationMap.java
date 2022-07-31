package sg.edu.np.mad.Assignment1;

public class LocationMap {

    String details, title, url, img_url, address;

    public LocationMap() {

    }

    public LocationMap(String details, String title, String url, String img_url, String address) {
        this.details = details;
        this.title = title;
        this.url = url;
        this.img_url = img_url;
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getAddress() {
        return address;
    }
}

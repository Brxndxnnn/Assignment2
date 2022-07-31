package sg.edu.np.mad.Assignment1;

public class EventList {

    String img_url, event_info, time, description;

    public EventList(String img_url, String event_info, String description,String time) {
        this.img_url = img_url;
        this.event_info = event_info;
        this.time = time;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getEvent_info() {
        return event_info;
    }

    public String getTime() {
        return time;
    }
}

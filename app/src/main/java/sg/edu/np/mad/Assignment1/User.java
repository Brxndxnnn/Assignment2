package sg.edu.np.mad.Assignment1;

public class User {

    public String email;
    public String username;
    public Integer points;

    //Constructer
    public User(){

    }

    public User(String email, String username, Integer points) {
        this.email = email;
        this.username = username;
        this.points = points;
    }

    //Getter & Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}

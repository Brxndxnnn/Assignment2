package sg.edu.np.mad.Assignment1;

public class User {

    public String email;
    public String username;

    //Constructer
    public User(){

    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
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
}

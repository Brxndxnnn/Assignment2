package sg.edu.np.mad.Assignment1;

public class FAQ {

    //Initialising variables
    private String question, description;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public FAQ(String question, String description) {
        this.question = question;
        this.description = description;
        this.expandable = false;
    }

    // Getter and Setter

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method

    @Override
    public String toString() {
        return "FAQ{" +
                "question='" + question + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

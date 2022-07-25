package sg.edu.np.mad.Assignment1;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.Date;

public class SearchHistory {
    public String listingName;
    public String searchTime;

    public SearchHistory(){
        // Default constructor required for calls to DataSnapshot.getValue(SearchHistory.class)
    }

    public SearchHistory(String listingName, String searchTime) {
        this.listingName = listingName;
        this.searchTime = searchTime;
    }

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }


    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
}

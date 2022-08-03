Zhe Wei's Contribution for Assignment 2

Feature Created: Basic search feature for the listing items, as well as the search history feature to allow users to view their recent searches. 
Created to complement the existing Listing Feature to allow user to search for their desired listing items. 
Realtime Firebase was used to store the Search History of each user.

Feature involves:

1. A new user interface for the users to search for listing items.
2. Allow users to view details of the listing item that the user search for. 
3. Open the SearchView wiget immediately when the user wants to search for a listing item.
4. Allow users to view their search history. 
5. Allow users to delete their search history records.
6. Allow users to clear their search history.

Improvements I made to Application since Assignment 1:

1. Added basic search feature for the user 
2. Added search history feature for the users to view the listing items that they searched previously. 
3. New Child added to the Realtime Firebase (SearchHistory) to store what items each user search
4. Created new user interface for the users to search for listing items (Added SearchView widget, RecyclerView)

### User Guide (Search Feature):

<img src="https://user-images.githubusercontent.com/92966900/182203739-19471e89-7ea4-453b-b655-f5b5bdf29851.png" width="160" height="350"><img src="https://user-images.githubusercontent.com/92966900/182203766-a3fc87a0-7743-433e-a727-273f34c63ba0.png" width="160" height="350">  

User will be navigated to the List Search Page (second image) when clicking on the Search Icon (shown in first image).

When the user submits his search query, he will be navigated to the listing details page of the item that he has searched for. 
His search history will be recorded and displayed in the page as well.
 
When the user clicks on the remove icon (in red), a toast message will pop up to indicate that his "Search History Record" has been successfully deleted. The record will disappear from the list search page. 

When the user clicks on the "Clear All" text, a toast message will pop up to indicate that the user's search history has been successfully cleared. The user's search history would be cleared. 

When the user clicks on "Cancel", the user will be navigated back to the listings page. 




# Movie Matchup
Android app created by Kylun Robbins (Group 6)


## Class Diagram
Class Diagram can be viewed at:
https://app.lucidchart.com/invitations/accept/93099114-63eb-4662-bcb2-74c31f1a8262


## Features Implemented

### Authentication (Sign In and Sign Up)
App allows for creating an account and loggin in by either:
  * Phone number
  * Email
  * Google Account

Test Account
Use test account to test login with Google:
  * Email :     css450test@gmail.com
  * Password :  tcss450!

User Stories
* As a user, I would like to sign in to an account to access my movie list.
* As a user, I would like to sign in using my Google log-in to save time and use a familiar password. 


### Movie Matchup
App presents the user with two movies.  User can select which movie they prefer over the other and those choices will reflect in their ranked list.  User also has the option to remove a movie from the choices by stating they have not seen either or both movies.  Movies that have been marked as has not seen will be incorporated in the recommendations section.

User Stories
* As a user, I would like to choose between two movies to create a ranked list of movies. 
* As a user, I would like an option to remove movies I have not seen from the rotation.


### View Favorite Movies
App allows the viewer to view their personalized ranked list of their favorite movies.  Every movie the user has ranked is displayed in order of preference.  User will also be able to share their top 10 favorite movies by pressing the share button.  Sharing is currently avaible through text and email.

User Stories
* As a user, I would like to view my ranked list of movies to see what my favorite movies are.
* As a user, I would like to be able to share my top-ten favorite movies.


### Search
App allows for searching of movies.  Once search is complete, a movie can be added to the user's ranked list by clicking the movie.

User Stories
* As a user, I would like to search and add a specific movie to my ranked list.


### View Recommended Movies
App allows the viewer to get recommendations for movies to watch.  The recommendation list is generated based on the movies the viewer has marked as not seen.

User Stories
* As a user, I would like to view a list of all the movies I have not seen, and get movie recommendations based on this list.



## Project Requirments

### Bugs from Sprint I
There were no bugs in the code of my Sprint I submission.


### User Stories
I was able to implement almost all user stories (high, medium, and low).  The only user story that is not implemented was a low priority story that involved viewing other user's ranked lists.

### Shared Preferences


### Content Sharing
The user is able to share their top 10 favorite movies in the "Favorite List" section.  This allows the user to share their top ten movies over text and email.  The user can also copy their top ten movies to their clipboard for easy sharing on social medias.  


### Graphics
The application shows many movie posters in multiple activites.  Posters are present in "Matchup", "Favorite List", "Search", and "Recommendations".
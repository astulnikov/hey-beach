# Hey Beach
### Summary

Android app capable of fetching and showing a list of beach pictures from a remote server without 
using any 3rd party library

#### Architecture

The application uses MVP Architecture. It applies the repository pattern to provide access
 to data from a remote data source. The beach list has infinite scrolling capabilities requesting
 more pictures from the API using pagination. Dependency injection / Factory is used to create and
 provide helper objects and repositories.
 
 The application is composed of 4 views:
 * *Beach list*: Shows a list of beach images and titles if available
 * *Login*: Allows users to input their credentials and log in
 * *Register*: Allows users to register in the app
 * *User Details*: Allows users to see stored information
 
 ### Next Steps
 * Improve list items layout and masonry using Google Flexbox - layout
 * Use local storage (SQLite) to cache API results and be able to show then without connection
 * Add Disk cache for images to prevent reloading from network when memory is limited, configuration 
 changes or there is no internet connection
 
 
 




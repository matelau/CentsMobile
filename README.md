**Abstract**
The aim of this application is to allow the user to easily consider more of the variables that
shape their job search decisions than what is traditionally available by other offerings. This
application will allow a user to complete a general job search by providing a job title and a city
or state. The application will then create a list of open job postings using indeeds web api,
and allow the user to click to be taken to Glassdoor’s job posting. The value of this application
comes when, the user taps into the cost of living tab to see relevant data on the area and then again on the spending tab to see a
suggested spending breakdown based on the job titles median wage in the given area. This
relevant data includes visualizations for the area’s cost of living compared to the national
average. The user can also add one or more areas to be shown in the comparison.


**To Run this Application**


-Load the project into Android Studio ( I used Android Studio 1.0 to develop and this required me to upgrade gradle, so you may need to do so as well)


-Load an emulator running Lollipop, or use a device with Lollipop ( I wanted to play with recyclerviews, toolbars, and cardviews )


-Use Android Studio to run the application on your device/emulator


**Time**

1. Main Search Screen

* Query Glassdoor.com and Indeed API - 8hrs

* View - 6hrs (First time using Custom shapes and Custom Animations, I also spent timelearning how to begin and end animations in a fluid manner

2. Job Listings

* View - 12hrs (I spent a great deal of time trying to get the list to contain logos, including writing a script to scrape popular company logos and store this list in a json file to load when the list is populated, but I was not successfull. So I decided to create a jobdetail activity)


3. Job Detail

* View - 4hrs

* Glassdoor Data processing - 1hr


4. Area Data

* View - 8hrs ( I originally created custom views that allowed you to scroll between the different fragments views, but I preferred the look of tabhost. So, I gutted the project and used tabhost)

* Dialog Fragment to select a second city - 2hrs

* Logic for including 2nd view - 2hrs

* Modified Viz Library to show text on axis differently - 1hr


5. Spending Breakdown 

* View - 6hrs



Total: 50 hours




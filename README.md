ParkWise

ParkWise is an Android parking app that I created for a university project.

The app allows a user to search for parking areas, save parkings, add personal notes, and leave reviews. The user can also see reviews from other users and check the average rating of each parking area.

What the app can do

The user can log in with an existing account, search for parking areas by city, area, or name, open a parking to see more details, save or unsave a parking, add a personal note when saving it, leave a review, edit or delete their own review, and view their saved parkings and reviews in the history screen.

Technologies used

Kotlin
Android Studio
Jetpack Compose
Material 3
Room Database
Firebase Firestore
Navigation Compose
Android Notifications

Main screens

Login Screen
Home Screen
Parking Results Screen
Parking Details Screen
Add Review Screen
My Parking History Screen

Database

The app uses both Firebase Firestore and Room.

Firestore is used as the cloud database. It stores the users, parking areas, saved parkings, and reviews.

Room is used as the local database on the device. The app loads parking data from Firestore and saves it locally in Room, so the search works through the local database.

The main Firestore collections are Users, parkings, saved_parkings, and reviews.

The main Room tables are parkings, saved_parkings, and local_review_parkings.

How the data works

When the app starts, it loads the parking areas from Firestore and stores them in Room.

After the user logs in, the app loads only that user’s saved parkings and reviews from Firestore and stores them locally in Room.

When the user saves a parking, removes a saved parking, adds a review, edits a review, or deletes a review, the app updates both Room and Firestore.

Parking Details shows reviews from all users for that parking.

My Parking History shows only the logged-in user’s saved parkings and reviews.

Reviews

A review has four rating categories:

Cleanliness
Security
Price satisfaction
Easy to find

The final rating is calculated from the average of these four values.

Notifications

The app shows a notification when the user saves a parking but has not reviewed it yet.

Firebase setup

The Firebase configuration file is not included in this public repository.

To run the project with Firebase, you need to add your own google-services.json file inside the app folder.

How to run

Clone the project, open it in Android Studio, add your own google-services.json file inside the app folder, sync Gradle, and run the app on an emulator or Android device.

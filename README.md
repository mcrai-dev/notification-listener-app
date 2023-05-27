# notification-listener-app
The Notification Listener App is an Android application that allows you to receive sound notifications for select social media apps. You can enable or disable notifications for different apps using a switch in the user interface.
__________________________________________________________________________________________________________________________________________________________________

# Project Structure
The Notification Listener App is structured into three main classes:

DatabaseHelper - This class manages the SQLite database used to store the switch state.

NotificationListenerServiceTwitter - This class extends NotificationListenerService and acts as the notification service to intercept and process notifications from social media apps.

MainActivity - This class is the main activity of the app, displaying the user interface and allowing the user to enable or disable notifications.
__________________________________________________________________________________________________________________________________________________________________

# Features
The Notification Listener App offers the following features:

Enable or disable sound notifications for different social media apps like Twitter, Facebook, Messenger, and WhatsApp.
Play a notification sound when a notification is received for an enabled app.
Manage the switch state using an SQLite database to persist it between app sessions.

DatabaseHelper Class
The DatabaseHelper class is a utility class that handles the creation and updating of the SQLite database used to store the switch state.

# Methods
DatabaseHelper(Context context): Constructor of the DatabaseHelper class, taking the application context as a parameter.

onCreate(SQLiteDatabase db): Method called when the database is created, creating a table to store the switch state.

onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion): Method called when the database is upgraded, deleting the existing table and recreating a new one.

getSwitchState(): Method to retrieve the current switch state from the database.

NotificationListenerServiceTwitter Class
The NotificationListenerServiceTwitter class extends NotificationListenerService to intercept notifications from social media apps and trigger the playing of a notification sound if the corresponding app is enabled.

# Methods
onCreate(): Method called when the service is created, initializing the media player for playing the notification sound.

onDestroy(): Method called when the service is destroyed, releasing resources used by the media player.

onNotificationPosted(StatusBarNotification sbn): Method called when a new notification is posted, checking if the corresponding app is enabled and triggering the notification sound playback.

onNotificationRemoved(StatusBarNotification sbn): Method called when a notification is removed. No action is needed in this implementation.

MainActivity Class
The MainActivity class is the main activity of the app. It displays the user interface and allows the user to enable or disable notifications.

# Methods
onCreate(Bundle savedInstanceState): Method called when the activity is created. It initializes the UI components, loads the current switch state, and sets event handlers for the switch and card.

onResume(): Method called when the activity is resumed. It checks if the notification service is enabled and updates the switch state accordingly.

openNotificationAccessSettings(): Method to open the Android system notification access settings.

isNotificationServiceEnabled(): Method to check if the notification service is enabled for the app.

loadSwitchState(): Method to load the current switch state from preferences and update the UI accordingly.

updateSwitchImage(): Method to update the GIF image in the UI based on the current switch state.

saveSwitchState(boolean switchState): Method to save the switch state in preferences and the database.

setSwitchState(boolean state): Method to update the switch state, save the state, and update the UI.

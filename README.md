##Inspiration
We did this project for the OpenVent Challenge from Infineon for hackaTUM C0dev1d19.

## Features
- monitoring the data of all vents directly on your phone
- get push-notifications of problems occur at patients beds
- get all changes made on the ventilators during home time

## Collection of resources
Before we stared programming our application we talked with anesthetist who gets in contact with professional ventilators in his everyday workday. He was able to tell us what are the most significant values of the vent that he needs for his job and what does values mean.

##How our project works
A python server collects the data from the ventilators. All necessary data gets forwarded to physicians phone trough an Android Application.

The Application has been built using Java and some OpenSource-libraries to process and display the present data from the ventilators. The data is only received when the human needs it, which is only when he looks in a patient profile. 

The server on the other hand checks all the time if a patient has a problem. After a problem is recognized, it sends an alarm in form on a push notification with the room number of the patient, to the doctors phone. This technique allows to give all important data to the physician, but at the same time saves battery and resources of the phone.

If a doctor sets the app in to break mode, he won't receive new alarms during the time he doesn't work. Nobody wants to be interrupted with work related things while at home. When he comes back all changes that his colleagues made are displayed.

## What we learned
- how medical ventilators work
- more about app development
- a lot about networks


## What's next for OpenVentSmartApplication
- Tests with real ventilators
- Find a better name

# Transcribe
## Projects 2&3 - Davis-2021-Sprint-CS402

### Full App Title: Transcribe - a Free Gender-Diverse Health Tracker & Community 


### Pitch:
**A health app built with you in mind.** 
Navigating your health, safety, and well-being can be difficult when you exist outside the realm of socially expected gender, with medical environments becoming stressful and alienating if you don't fit into your assigned-at-birth box. **Transcribe** aims to fit your expectations and needs, instead of the other way around, helping you to track and monitor important mental and physical health information, while also fostering community knowledge to help you feel more prepared and less alone.  

### Description:

Transcribe is a health and wellness app built for transgender and non-binary individuals.   
It has two main functions - an individual profile, where a user can store and track data about many different health factors, including:  

* Medication reminders  
* Pharmacy information   
* Body measurements
* And more!     

There is also a Transcribe community. Here users will be able to view details about common transgender and non-binary medical topics that have been added by other users.  The goal is to build off of the existing tradition among queer and trans people of community-based knowledge by making that knowledge more accessible and far-reaching. Everything is completely anonymous, and users can choose whether or not to contribute their own experiences. 

### Screenshots:
![](../../Pictures/transcribe_3_screenshots.png)

### Keywords:
```medical, health, tracker, symptoms, medication, mood, transgender, gender, LGBT, gay, queer, non-binary,```
``` surgery, HRT, hormones, mindful, depression, anxiety, doctor, reminders```

### Category: Health and Fitness:

# Developer Information:

## Start Activity
Now, when a user signs up, a user entry is saved in the (right now, just local) database using ObjectBox. And when a user is logging in, their credentials much match that of an already-existing user. 
When signing up, the provided  email is still being validated using the [mailboxlayer API](https://mailboxlayer.com/). 


## Profile
While the UI does not look much different than before, I've now implemented:  

+ Storing of added medications in local database.   
+ A relation exists between the medication data and the user data. Only medications belonging to the logged in user are fetched at activity creation.

Medications are added by tapping the bottle-plus icon. Now more data, such as medication frequency (what days the medication is to be taken on) and a reminder time can be saved with the medication information, as can be seen in the second screenshot.   
However, while that data is being stored in the database, it is not reflected in the created medication fragment back on the Profile activity -- I wanted to customize this implementation a bit more, and thus will implement later (plus, it's been updated fairly recently).  

The profile photo can still be changed with a tap and hold. 

## Community
Here, I have implemented a RecyclerView to later display the available topics in the Community section. Right now, they are just being labelled as per the example dummy repository. However, tapping the plus button on each topic will open up a (right now, filler) data graph. This graph is generated using [AAChartCore](https://github.com/AAChartModel/AAChartCore), a pretty powerful yet simple library that takes away a lot of the pain that other graph libraries I was trying to use caused. 

There is a display issue that causes the graph to not always load or load after you've scrolled away. I think this is caused how the RecyclerView decides to load or not load elements.  

## Contribute
By tapping on the "Contribute" button on each Community item, you are taken to a survey that is implemented using [SurveyKit](https://github.com/quickbirdstudios/SurveyKit). Once the survey is complete, the survey data collected is currently stored in the local database. A relation exists between this data and the user who contributed it, which will later be used to show the user what they have contributed to and to prevent duplicate entries.   
Later, this data will be what is used to create the graphs under each topic. 

## Other Info

A **signed app bundle** is available in the `export` folder..   
The **Play Store Icon** is available in the ``marketing` folder as `ic_launcher-playstore.png`. 

I got blindsided by a bunch of responsibilities these past weeks, so this submission is a little more lackluster than I wanted it to be, but c'est la vie!
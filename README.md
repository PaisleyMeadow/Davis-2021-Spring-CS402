# Davis-2021-Sprint-CS402

### Implementations for Homework #1:
- Landing Page:
  - Login button just takes you to profile page
  - Sign-up button takes you to a sign-up form  
    - Sign-up page requires all input to be filled
  - ? takes you to filler webpage -- idea for this is that it would take you to either activity or webpage about the app and its creation
- Profile:
  - The username is TmpUser if you used the login button to get here, because there's no login page at the moment, but otherwise it will fill in with the inputted username. The date should be displayed correctly, although it will be set to whatever the emulator device's time zone is. 
  - Medication can be deleted, the name and dosage can be changed, and the reminder switch will change the background color. 
  - "Track Weight" will change the displayed weight
  - "Track Mood" will display mood icons to choose from, but will not correctly change the displayed mood because drawables/resources are weird

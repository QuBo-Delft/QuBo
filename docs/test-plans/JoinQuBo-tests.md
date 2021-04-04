# JoinQuBo Tests

## Test Join Question Board Functionality
---

### Test 1: No Question Board Code nor Username
1. Start the application.
2. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: Could not find the requested question board!
                                   Please check if you inserted the code correctly!"
3. Enter the question board student code (or the moderator code).
4. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: No username was entered! Please enter a username!"
5. Enter a username. The error message should now disappear.
6. Remove the username. The error message should now reappear.
7. Enter a username and click "Join". You should now load into the application.

  

### Test 2: No Question Board Code
1. Start the application.
2. Enter a username.
3. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: Could not find the requested question board!
                                   Please check if you inserted the code correctly!"
4. Enter the question board student code (or the moderator code).
5. Click "Join". You should now load into the application.

  

### Test 3: Invalid Question Board Code
1. Start the application.
2. Enter a username.
3. Enter a random word instead of an existing question board code.
4. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: Could not find the requested question board!
                                   Please check if you inserted the code correctly!"
5. Enter the question board student code (or the moderator code).
6. Click "Join". You should now load into the application.

  

### Test 4: Valid Question Board Code and Username
1. Start the application.
2. Enter a username.
3. Enter the question board student code (or the moderator code).
4. Click "Join". You should now load into the application.

---
## Test Create Question Board Functionality
---

### Test 1: No Displayed Error Messages Allows Scene Transition
1. Start the application.
2. Click "Create Question Board". You should now load into the CreateQuBo view.

  

### Test 2: Displayed No Question Board Error Message
1. Start the application.
2. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: Could not find the requested question board!
                                   Please check if you inserted the code correctly!"
3. Click "Create Question Board". You should now load into the CreateQuBo view.

  

### Test 3: Displayed No Username Error Message
1. Start the application.
2. Enter the question board student code (or the moderator code).
3. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: No username was entered! Please enter a username!"
4. Click "Create Question Board". You should now load into the CreateQuBo view.
  
---
## Test Close Application Functionality
---

### Test 1: Correctly Blocks the User from Clicking Anything Outside of the Pop-up
1. Start the application.
2. Click the button used to close the window. A pop-up should appear.
3. Click on the "Join" button. Nothing should happen.
4. Click on the "Create Question Board" button. Nothing should happen.

  

### Test 2: Displayed No Question Board Error Message Correctly Launches the Pop-Up
1. Start the application.
2. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: Could not find the requested question board!
                                   Please check if you inserted the code correctly!"
3. Click the button used to close the window. A pop-up should appear.

  

### Test 3: Displayed No Username Error Message Correctly Launches the Pop-Up
1. Start the application.
2. Enter the question board student code (or the moderator code).
3. Click "Join". An error message should be displayed above the question board code and username text fields.
    This error message should say: "Error: No username was entered! Please enter a username!"
4. Click the button used to close the window. A pop-up should appear.

  

### Test 4: "No" should return the User to the Application and allow the User to Select Elements Again
1. Start the application.
2. Click the button used to close the window. A pop-up should appear.
3. Click "No". The pop-up should disappear.
4. Click on the "Join" button. An error message should appear.
5. Click on the "Create Question Board" button. You should now load into the CreateQuBo view.

  

### Test 5: "Yes" closes the Application
1. Start the application.
2. Click the button used to close the window. A pop-up should appear.
3. Click "Yes". The pop-up and application window should disappear and the program should terminate successfully.

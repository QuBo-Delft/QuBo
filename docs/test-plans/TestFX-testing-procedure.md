###Wednesday, 07 April 2021
#User Interface Testing
###OOPP Group 5
  
##How it’s done
**All of our ‘FXML sheet’-based scenes and stages can be automatically tested by making use of the TestFX library**. This is an open-source library that is partly built upon the AWT robot. The AWT Robot automatically performs UI interactions that would otherwise need to be done manually. Our application reacts naturally to these interactions as if the robot is an actual user. Based on the expected behaviour of the application regarding the TestFX robot’s interactions we can make various assertions. TestFX simplifies this process by allowing the use of special asserts, aimed to help simplify testing JavaFX applications, called FxAsserts. These assertions can be used in combination with a so-called matcher. A matcher contains various testable properties of relevant elements. An example would be the WindowMatcher’s isShowing property, which checks whether the window set in the assert is showing up as expected.
We have written these sort of tests for all controllers responsible for FXML sheets. The tests can found in the controller package under ‘*client/src/test/java/nl.tudelft.oopp.qubo/*’.

##How to configure
**Our tests can be configured to run in headless or headful mode**. Headless mode ‘hides’ the visible user interface of the application and automatically performs all the testing without affecting the user of the machine in any noticeable way. This also allows the tests to be run on machines without video output as well. The only noticeable part when running a test in headless mode on a machine with graphical output is seeing the test output in the IDE of choice in which the tests are run. The headful mode will make the application start and run regularly, making use of AWT and the Glass Windowing Toolkit normally.
By default, the tests run completely headless. This can be changed by changing the JVM arg headless to false. To do this, simply head over to line 70 of the global build.gradle file in your IDE of choice and change it to: ‘*jvmArgs "-Dheadless=false"*’. Changing this setting will affect a static initialiser block found in the TestFxBase class (same location as the test classes), which actually sets the proper headless/headful configuration.
It is also important to either comment out or remove line 65. This line is there to, by default, exclude the tests from Spring automatic testing, as the GitLab CI Pipeline used is not compatible. Removing this line will enable them for Spring, allowing local testing.

##How to run
Before running any tests, ensure that the server-side Spring Boot application is actually up and running. This can be done by navigating to ‘*Server/src/main/java/*’ and running the QuBo.java class. To check whether it’s already running (in IntelliJ), navigate to the ‘Services’ tab and verify.
**Tests for a single FXML sheet can all be run at once or individually.**
To **run all tests at once**, run the test at the class level. In IntelliJ, this can be done by pressing the green play button at the left of the class definition line or by right-clicking this line and selecting run in the context menu.
To **run tests individually**, run the test at the method level. In IntelliJ, this can be done by pressing the green play button at the left of a method definition line or by right-clicking this line and selecting run in the context menu.

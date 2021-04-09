# QuBo

![QuBo logo](/docs/img/cover.png "QuBo logo")

## Description of project
**QuBo** is an application aiming to improve interaction between students and lecturers to make the learning environment better and increase the quality of education.
Online education makes communication between students and lecturers more difficult. QuBo alleviates this problem by organising the questions asked by students in an easy-to-follow way so that no question is accidentally looked over. It also has other useful functionality such as live polls and a pace indicator for the lecturer. Its clean user interface and versatile features make it an essential tool for any lecture.

### Features

- Asking, editing, and deleting questions.
- Upvoting questions to have them show up first.
- Adding textual answers and marking questions as answered to keep the board relevant.
- Separate question boards for each lecture, secured with unique keys.
- Banning users who misbehave from interacting with a board, as well as the option to set up rate limiting.

- Real-time poll creation.
    - _To add another interactive element to lectures, there is the option to do a live poll in the board, which students can respond to. This will help lecturers in taking students’ opinions into account. This can also be used to test students’ knowledge._

- Anonymity mode
    - _The application can be set up to store questions in a completely anonymous way by unlinking them from users and IPs without compromising on functionality via single-use tokens. This could be used to make students feel more comfortable when expressing their opinions while giving feedback._

- Continuous visual pace indicator for the lecturer
    - _To make it easier for lecturers to know if they are going too fast or too slow, there is an indicator on the left side of the lecturer's screen, which moves up or down in accordance with students' opinions on the pace of the lecture. This is particularly useful in an online setting._

- Exporting all questions of a board to a file
    - _At the end of a lecture, lecturers can export all questions and answers to a neatly formatted file and upload them to Brightspace._

### Project structure
- **Back end**
    - The back end uses service-oriented architecture for separation of concerns. Common DTOs are shared between the client and the server to make communication easier. ModelMapper is used to convert entities to DTOs and vice versa. All repositories and services are fully tested through the use of unit tests. The majority of all endpoints, including the most important ones, as well as the parts of the back end they communicate with, are tested via integration tests.
- **Front end**
    - The front end adopts the basic structure of a JavaFX project such that most logic and views are separated into layers. FXML files abstract most user interface designs from the programming logic and view controllers contains the logic to control views. Server communication classes are used to exchange data between the server and the client, all communication methods are fully tested through the use of unit testing. CSS stylesheets are used to enable the separation of effect presentation and view content. The user interface can be automatically tested by using sophisticated robots which mimic real user interactions. Based on these interactions we can assert various expectations, which can easily be tested for.
## Group members

| 📸 | Name | Email |
|---|---|---|
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3532/avatar.png?width=400" width="50">  | Cassandra Visser | C.M.S.Visser@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3531/avatar.png?width=400" width="50"> | Dorothy Zhang | D.L.Zhang@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3096/avatar.png?width=400" width="50"/> | Gijs van de Linde | G.vandeLinde@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3534/avatar.png?width=400" width="50"/> | Jort van Leenen | J.P.vanLeenen@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3404/avatar.png?width=400" width="50"> | Martin Mladenov | M.Mladenov@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3533/avatar.png?width=400" width="50"/> | Yunhan Wang | Y.Wang-128@student.tudelft.nl |

## How to run it
- Run with terminal:

Navigate to the folder (main repository) that contains the .gradle folder.

- macOS/Linux: 
    1. Use the command `./gradlew server:bootRun` to start the server;
    2. Use the command `./gradlew client:run` to run the client application.

- Windows:
    1. Use the command "gradlew server:bootRun" to start the server;
    2. Use the command "gradlew client:run" to run the client application;

---
- Run with Intellij:
    1. Import the project;
    2. Run /server/src/main/java/nl/tudelft/oopp/qubo/QuBo.java (server application);
    3. Run /client/src/main/java/nl/tudelft/oopp/qubo/MainApp.java (client application);

(The gradle version of this application is 6.3)

## Documentation
![documentation](/docs/img/documentation.png "documentation")

## How to contribute to it

If you want to contribute, you should avoid failing builds, make commit messages clear and summarize the changes. You should follow our code style as stated in the checkstyle file and test your code properly before submitting. When you create merge requests, you should properly describe what this merge request is about and set correct labels, milestone, and sprint. We will approve your merge request if more than one member of our team believes that your code will not cause problems in our project and can improve the quality of our project to a certain extent.

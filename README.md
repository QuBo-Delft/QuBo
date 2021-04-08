## Description of project
Due to the restriction of remote education, interactions between lecturers and students have become extremely difficult. To enable smoother interactions during the lecture and to facilitate the learning environment, the TU Delft OOPP Group 5 (2020-2021) has decided to build a Question Board application named QuBo. This is an application that aims to improve the quality of lectures on both the lecturer side and the student side. Students can join the question board created by a lecturer and post questions during the lecture, the lecturer and teaching assistants (moderators) can answer these questions and manage the question board easily. Moreover, QuBo supports polling such that lecturers can recieve students' response during the lecture.

## Group members

| 📸 | Name | Email |
|---|---|---|
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3532/avatar.png?width=90" width="50">  | Cassandra Visser | C.M.S.Visser@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3531/avatar.png?width=90" width="50"> | Dorothy Zhang | D.L.Zhang@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3096/avatar.png?width=400" width="50px"/> | Gijs van de Linde | G.vandeLinde@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3534/avatar.png" width="50"/> | Jort van Leenen | J.P.vanLeenen@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3404/avatar.png?width=400" width="50"> | Martin Mladenov | M.Mladenov@student.tudelft.nl |
| <img src="https://gitlab.ewi.tudelft.nl/uploads/-/system/user/avatar/3533/avatar.png?width=400" width="50"/> | Yunhan Wang | Y.Wang-128@student.tudelft.nl |

## How to run it
- Run with terminal:

Navigate to the folder (main repository) that contains the .gradle folder.

For MacOS/Linux users: 
1. Use the command "./gradlew server:bootRun" for running the server application;
2. Use the command "./gradlew client:run" for running the client application;

For Window users:
1. Use the command "gradlew server:bootRun" for running the server application;
2. Use the command "gradlew client:run" for running the client application;

---
- Run with Intellij:
1. Import the project;
2. Run /server/src/main/java/nl/tudelft/oopp/qubo/QuBo.java (server application);
3. Run /client/src/main/java/nl/tudelft/oopp/qubo/MainApp.java (client application);

(The gradle version of this application is 6.3)

## How to contribute to it

If you want to contribute, you should avoid failing builds, make commit messages clear and summarize the changes. You should follow our code style and test your code properly. When you create merge requests, properly comment on what this merge request is about and set correct labels, milestone, and sprint. We will approve your merge request if more than two members of our team believe that your code will not cause problems in our project and can improve the quality of our project to a certain extent.

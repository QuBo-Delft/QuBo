
# Moderator View Tests

## Test Moderator View Functionality
---

### Test 1: Question Board Displayed Correctly
1.Join a question board as a moderator (described in the JoinQuBo tests).\
2.Join a question board as a student (described in the JoinQuBo tests). You should see no question displayed in the moderator view and a green circle on the upper left corner. You should see the title of the lecture entered by you and since what time this question board has opened.


### Test 2: Posted Question Displayed
1.Repeat steps 1, 2 from Test 1.\
2. Post a question as a student (click the “+” button for asking a question), question content should be: How to solve a function?. In the moderator view, you should see there is a question displayed in the unanswered questions section with the content “How to solve a function?” and 1 upvote.

### Test 3: Question Upvote Increased
1. Repeat steps 1, 2 from Test 2.\
2. In the moderator view, click the triangle in the question block, then you should see the the triangle is filled with orange color. In the student view, you should see the question’s upvote has been increased by one.

### Test 4: Question Upvote Decreased
1.Repeat steps 1, 2 from test 3.\
2.In the moderator view, click the orange triangle in the question block, you should see it turns back to a triangle with orange filled.



### Test 5: Question Edited
1.Repeat steps 1, 2 from test 2.\
2.In the moderator view, click the 3 dots button in the question block, you should see an option menu pops up with buttons named “edit”, “reply”, “mark as answered”, “ban”, and “delete”, respectively.\
3.In the moderator view, click “edit”, you should see a text area and two buttons called “cancel” and “update” in the question block, the text in the text area should be “How to solve a function?”.\
4.Change the text to “What is that?”, then click update. You should see the question content has changed to “What is that?” in both moderator view and student view.


### Test 6: Question Replied
1.Repeat steps 1, 2 from test 5.\
2.Click reply, you should see a text area and two buttons named “cancel” and “reply” in the question block.\
3.Enter “That is maths” in the text area then click reply. You should see there is a block appeared under the original question block with text content “That is maths”.\

### Test 7: Question Marked As Answered
1.Repeat steps 1, 2, 3 from test 6.\
2.Click “mark as answered”, you should see the original question block disappears.\
3.In both the moderator and student view, click the button at the upper right corner, you should see a side bar appears on the left of the window, click the hook-like button, you should see the previous question is displayed in the new popped up section (answered questions section).\

### Test 8: Question Deleted
1.Repeat steps 1, 2 from test 5.\
2.Click “delete”, you should see the question disappears in both the moderator view and the student view.\


### Test 9: User Banned 
1.Repeat steps 1, 2 from test 5.\
2.Click “ban”,  you should see a dialog pops up with the message “User IP Successfully Banned”.\
3.In the student view, try post a question, you should see an alert dialog pops up with the title “Failed to post your question”.\
4.In the student view, try click a pace button, you should see an alert dialog pops up with the title “Failed to add your pacevote”.\
5.In the student view, try delete the only one question, you should see an alert dialog pops up with the title “Failed to delete your question”.\
6.In the student view, try edit the content of the only one question, you should see an alert dialog pops up with the title “Failed to update your question”.\
7. In the student view, double click the triangle in the question block, you should see an alert dialog pops up with the title “Upvote failed”.\

### Test 10: Question Editing (text too short)
1. Repeat steps 1, 2 from test 5.\
2. In the moderator view, click “edit”, you should see a text area and two buttons called “cancel” and “update” in the question block, the text in the text area should be “How to solve a function?”.\
3. Change the text to “What?”, then click update. You should see the error message “Error: Question must be more than 8 characters long” in the question block.\

### Test 11: Board Details Displayed
1. Repeat steps 1, 2 from test 2.\
2. In the moderator view, click the button with the icon which has a rectangle and 3 bars inside. You should see the details of this question board displayed correctly in a new dialog.\



### Test 12: Documentation Displayed
1. Repeat steps 1, 2 from test 2.\
2. In the moderator view, click the button with a question mark icon. You should see a list of explanations of how to those buttons in a new dialog. \

### Test 13: Pace Changed
1. Repeat steps 1, 2 from test 2.\
2. In the student view, click pace button “too fast”. In the moderator view, you should see the pace indicator moves up.\
3. In the student view, click pace button “too slow”. In the moderator view, you should see the pace indicator moves down.\
4. In the student view, click pace button “all right”. In the moderator view, you should see the pace indicator moves to the middle of the pace bar.\

### Test 14: Export Questions
1.Repeat steps 1, 2 from Test 2.\
2.In the moderator view, open the side bar, you should see a button with the icon which has an arrow pointing up.\
3.Click that button, you should see a new dialog to ask for the file location. Pick a location on your computer.\
4.Find the file at the picked location, open it, you should see all questions of this lecture in text format.\

### Test 15: Close Question Board
1. Repeat steps 1, 2 from Test 2.\
2. In the moderator view, click the button at bottom right, you should see a new dialog to ask you to confirm the closing of this question board. Confirm it, you should the green circle turns to a circle filled with red.\

### Test 16: Leave Moderator View
1. Repeat steps 1, 2 from Test 2.\
2. In the moderator view, open the side bar, you should see a button with the icon which has an arrow pointing right.\
3. Click that button. You should see you are back to the JoinQuBo page.\

### Test 17: Schedule QuBo
1.Schedule to open a question board in the future.\
2. Join the question board as a moderator at the time before the question board opens. You should see a circle filled with yellow.\


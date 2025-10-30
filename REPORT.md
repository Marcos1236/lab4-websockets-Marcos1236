# Lab 4 WebSocket -- Project Report

## Description of Changes
In this lab assignment I have enabled and implemented the test 'onChat' in 'src/test/kotlin/websockets/ElizaServerTest.kt'.

To do so, initially I had to delete the annotation '@Disabled' of the 'onChat' test. This annotation prevents the test from being run, it was used in order for the tests to pass while the test wasn't implemented. 

Then, I added the logic in 'ComplexClient.onMessage' to send the text "I am feeling sad" when it detects the text "What's on your mind?", which is the initial text of the Doctor. Before this, I had tried to send the text when receiving the text "---", which is the message after "What's on your mind?". However, when doing the test, it sent the message many times. This happened because when the Doctor responds, he always ends with the text "---", which triggered the test message again.

Finally, I completed the 'onChat' test with two assertions. In the first one, I used assertTrue to check whether the number of messages sent are between 4 and 5. We use assertTrue to check an interval because the size of messages can vary due to latency. The second assertion checks that the first message sent by the Doctor is correct.

In order to accomplish all the tasks asked in the assignment, we also had to make sure the already existent CI workflow passes. As I am working in a forked repository, I had to enter to the Github Actions page of the repository in order to enable the workflow (as it wasn't implemented by me). Once this was done, the workflow run normally when new changes were pushed to the repository. However, there was still something else to do, as the workflow didn't pass and responded with an error. I found that there was a syntax error in the workflow (temuring instead of temurin) and after it was fixed, the workflow worked perfectly.

## Technical Decisions
In this assignment, we didn't have much room for decisions, however, there were some aspects of my code which I think could have been done differently. In the first place, I decided to use the string "What's on your mind?" as the condition to send the test message. I could have used the string "---" but, as I have explained before, it triggered the test message many times until the test ended. 

The next weren't exactly decisions, as the code contained hints that suggested this must be done, however, I will add it in this sections because it could also have been done differently. Firstly, the code hinted that the number of messages should not be checked with assertEquals. Therefore, I decided to use assertTrue to check if size is between an interval. I also decided that this interval was between 4 and 5 because I knew those were the only values ​​the size variable could be.

## Learning Outcomes
Thanks to this assignment, I gained a better understanding of the Websockets lifecycle and how to test it from the client side. It also helped me to practice writing asynchronous tests and handling concurrency and timing problems. This was specially useful because we have to do something similar in other subjects. 

Additionally, in the lab, the websockets explanation was really useful in order to fully understand the protocol and how Spring Boot handles it.

## AI Disclosure
### AI Tools Used
- ChatGPT

### AI-Assisted Work
- I used AI to fully understand the Eliza code and to check the syntax errors of this report. 
- 10% AI assisted, 90% original work.
- I didn't have to make any changes as I didn't use AI to generate code.

### Original Work
- I completed the test file ElizaServerTest.kt adding the missing lines of code and the requested comments. Besides, I fixed the CI workflow to make sure it passed. 
- I already had a basic understanding of the websocket protocol as I had worked with it in other subjects. That's why I could understand the code without too many problems. Besides, I could fully comprehend what I had to complete after the explanation in class.
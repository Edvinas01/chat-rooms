# Chat Rooms [![Build Status](https://travis-ci.org/Edvinas01/chat-rooms.svg?branch=master)](https://travis-ci.org/Edvinas01/chat-rooms) [![Coverage Status](https://coveralls.io/repos/github/Edvinas01/chat-rooms/badge.svg?branch=master)](https://coveralls.io/github/Edvinas01/chat-rooms?branch=master) [![Coverity Scan Build Status](https://scan.coverity.com/projects/10387/badge.svg)](https://scan.coverity.com/projects/edvinas01-chat-rooms) 
Spring Boot chat application which uses jwt's to authenticate users.

## Setup
Make sure Java 8 and MongoDB are installed and don't forget to set MongoDB connection string in the [application.yml](server/src/main/resources/application.yml) configuration file accordingly to your MongoDB setup.

## Back-end
First navigate to the [server](server) directory and run
```
./gradlew bootRun
```
This will build the application and run it on `localhost:10000`. 

If you're only interested in the back-end API and want to check how to use it, refer to controller integration tests for usage examples. You can find them [here](server/src/test/java/com/edd/chat/).

## Front-end
To run the front-end client first make sure you have installed [Node.js](https://nodejs.org/en/). Then navigate to [client](client) directory and run
```
npm start run
```

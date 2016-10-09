# Chat Rooms [![Build Status](https://travis-ci.org/Edvinas01/chat-rooms.svg?branch=master)](https://travis-ci.org/Edvinas01/chat-rooms) [![Coverage Status](https://coveralls.io/repos/github/Edvinas01/chat-rooms/badge.svg?branch=master)](https://coveralls.io/github/Edvinas01/chat-rooms?branch=master) [![Coverity Scan Build Status](https://scan.coverity.com/projects/10387/badge.svg)](https://scan.coverity.com/projects/edvinas01-chat-rooms) 
Spring Boot chat application which uses jwt's to authenticate users.

## Setup
Make sure Java 8 and MongoDB is installed and don't foget to set MongoDB connection string in the [applicaion.yml](server/src/main/resources/application.yml) configuration file accordingly to your MongoDB setup.

## Running back-end
Navigate to `server` directory and run `./gradlew bootRun`. This will build the Spring Boot application and start it on `10000` port.

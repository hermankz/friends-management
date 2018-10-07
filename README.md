#Friend Management API <br>
Build using Java 1.8, Spring Boot 2.0.4 with IntelliJ IDEA 

##Steps to setup:
- install Java (jre1.8.0_152), Maven (apache-maven-3.5.4), Docker.
- assume Docker is installed, run `sudo docker-compose up` in the same path as `docker-compose.yml` file located, to initiate PostgreSQL.
- assume Maven is installed, run `mvn package` to build the app.
- assume Java is installed, run `java -jar target/friends-management-1.0.0.jar`

###Using the API:
1. API to create user:<br>
POST http://localhost:8080/api/user/ <br>
Payload: {"email": "test@example.com"}

1. API to create friend connection between two email addresses:<br>
POST http://localhost:8080/api/userConnection/create

1. API to retrieve the friends list for an email address:<br>
POST http://localhost:8080/api/userConnection/friends

1. API to retrieve the common friends list between two email addresses:<br>
POST http://localhost:8080/api/userConnection/commonFriends

1. API to subscribe to updates from an email address:<br>
POST http://localhost:8080/api/userConnection/subscribeUpdates

1. API to block updates from an email address:<br>
POST http://localhost:8080/api/userConnection/blockUpdates

1. API to retrieve all email addresses that can receive updates from an email address:<br>
POST http://localhost:8080/api/userConnection/listCanReceiveUpdates

###Database (PostgreSQL 9.6) tables:
- user_account
  - id (bigint)
  - email (character)
- user_connection
  - id (bigint)
  - block_updates (boolean)
  - connected (boolean)
  - receive_updates (boolean)
  - friend_id (bigint)
  - user_id (bigint)

###TODO List: 
- unit tests
- init DB script for users

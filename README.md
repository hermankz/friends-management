<h2>Friend Management API </h2>
Build using Java 1.8, Spring Boot 2.0.4 with IntelliJ IDEA 

<h3>Steps to setup:</h3>

1. install Java (jre1.8.0_152), Docker.

1. assume Docker is installed, run `sudo docker-compose up` in the same path as `docker-compose.yml` file located, to initiate PostgreSQL.

1. assume Java is installed, run `java -jar release/friends-management-1.0.0.jar`

<h3>Using the API:</h3>

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

<h3>Database (PostgreSQL 9.6) tables:</h3>
- user_account<br>
  - id (bigint)<br>
  - email (character)<br><br>
- user_connection<br>
  - id (bigint)<br>
  - block_updates (boolean)<br>
  - connected (boolean)<br>
  - receive_updates (boolean)<br>
  - friend_id (bigint)<br>
  - user_id (bigint)<br>

<h3>TODO List: </h3>
- unit tests<br>
- init DB script for users

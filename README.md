Summary
------------------
This project involves creating a microservice for storing chat message data in a database table. The system supports asynchronous data saving, which can be implemented in a separate micro-service. 
The micro-service will support both saving and retrieval of chat messages.

I used JPA/Hibernate instead of Spring Data JPA for data persistence. The design should be data store agnostic, meaning that the data will initially be stored in PostgreSQL, but can later be migrated to MongoDB. 
The save method should be designed to support saving data to either PostgreSQL or MongoDB.

Future work
---------------
The system should also include a configurable DB connection pool. The micro-service will be HTTP-based, and the save chat message API can be called by AJKafkaRouter.

Key Features
---------------------
Asynchronous data saving
Separate micro-service for data persistence
Supports both saving and retrieval of chat messages
Uses JPA/Hibernate for data persistence
Data store agnostic design
Configurable DB connection pool
HTTP-based micro-service
Save chat message API callable by AJKafkaRouter


Technologies Used
----------------------
JPA/Hibernate
PostgreSQL
MongoDB
Micro-services
HTTP
Asynchronous processing
DB connection pooling
AJKafkaRouter

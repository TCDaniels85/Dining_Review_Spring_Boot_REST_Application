# Dining_Review_Spring_Boot_REST_Application
 Dining review application using Spring boot RESTful API

This objective of this project was to build a RESTful web API using Spring and Spring data JPA. Spring Initializr was used to generate the initial java project on which the final application was built upon. The MVC design pattern has been implemented to structure the project.

The embedded H2 database was used for the project to store the data and this was prepopulated with some data for testing purposes contained in the data.sql file. The H2 database was implemented in memory for testing purposes in the project as it is. This was also tested with persistent data stored in a file using the ‘spring.datasource.url = jdbc:h2:file:./data’ application property. 

This project also included the implementation of the Lombok library to reduce the amount of boilerplate code required when implementing accessor and mutator, as well as the constructors for the data models. The ResponseStatusException class and @ResponseStatus annotation were implemented to access more information about the errors in the Spring Framework.

The project has been tested using CURL, details of the CURL command tests can be found in the testing.txt document.

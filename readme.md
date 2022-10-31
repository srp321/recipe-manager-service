# Recipe Manager Service
Recipe Web Service is designed and implemented as part of and assessment.
It has basic CRUD operations related to RecipeData Entity and additional extended search capabilities.
It is a microservice based with REST API's assessment their interface.
The application is segregated into different components:
- Controller/API Layer
- Service/Business Layer
- Data Access Layer
- Persistence Layer

Currently, an in-memory database (H2-db) is used for the service which is for local development and testing purposes

### Architectural Choice
- Spring boot was decided as the framework of choice as it helps in rapid development of the application.
- The request flow from controller to service to entity (and to the persistence defined).

### Prerequisites
- JDK 1.11
- Apache Maven

### Steps to build the Service
- Download or clone project from GitHub
- Build the application (including running the tests) by executing below command
   ```
        mvn clean install
   ```

### Steps to execute the Service
- Run the application by executing command
   ```
        java -jar target/recipe-manager-service-0.0.1-SNAPSHOT.jar --logging.level.root=INFO
   ```

### Sources for API Documentation
- The documentation for the APIs as a swagger-ui html page APIs available at
http://localhost:8080/swagger-ui.html
- The documentation for the APIs in OpenAPI json format APIs available at
http://localhost:8080/v3/api-docs

### Steps for production deployment the service
- A relational database such as PostgreSQL or MySQL needs to be provisioned for the service
- Adding a jenkins pipeline job to the service, including stages for build, test, deploy and have additional features for security and artifacts management
- Based on scale, if needed, the service could be containerized and deployed on any public cloud, or it can also be deployed on-prem
- Different profiles could be created in application to support variables and parameters specific to different environments

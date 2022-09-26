# eCommerceService
Java + Spring boot backend service to manage products into inventory and allow customers can add/remove into their cart, get the ordered products and total money, checkout to persist orders.
# Development

## Prerequisite setup
1. Install Java version >= 11
2. Install Maven
3. Add the environment varables for Java & Maven
    - java -version
    - mvn -version
4. Database:
- Install MySQL server and create schema: shopping, create user: shopping/Shopping@123
- Or comment this block in application.properties file (not use mysql) to use H2-database:
  spring.datasource.type=com.zaxxer.hikari.HikariDataSource\
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\
  spring.datasource.hikari.driverClassName=com.mysql.cj.jdbc.Driver\
  spring.datasource.url=jdbc:mysql://localhost:3306/shopping\
  spring.datasource.username=shopping\
  spring.datasource.password=Shopping@123
## Architecture
![Alt text](collection/Architecture.png?raw=true)
- Assume that system will have 2 GUI components: 1 for customer can manipulate, another one for the operation team
- APIs from GUI will call to our backend service via an API Gateway. API Gateway will have responsibility to call IAM system to check authentication & authorization before routing to backend service
- All the apis and business logic are implemented into backend service
- We can extend the logic by separating the asynchronous flow to call the Message Broker (For example: When customer do checking out)
- System Architecture diagram already clarified the Main, the Assumption and TODO services 

### Database Schema
![Alt text](collection/DB-schema.png?raw=true)
## How to run
- Change config into application.properties, to insert records to database (data.sql): spring.sql.init.mode=always
- run: mvn spring-boot:run
- Stop application and run again with: mvn spring-boot:run to load products to productList (PostConstruct)
- and run the apis from the Postman collection: [here](https://www.getpostman.com/collections/79153b97e8917ed20345)
- List of APIS: [here](http://localhost:8080/swagger-ui/) or [here](https://www.getpostman.com/collections/79153b97e8917ed20345)
### Unit Test Coverage: 
- 100% coverage for Controller & Service layers
- run: mvn clean install
- Check the coverage report from Jacoco at: \target\site\jacoco\index.html
### Documentation
We use Swagger for API documentation. The Swagger documentation can be found at http://localhost:8080/swagger-ui/
### Health Checking
Call actuator apis from address: http://localhost:8191
### Security
Already added the Spring Security, configure into **SecurityConfig** class and mock token details. If application need to call IAM, change the code into **JWTAuthenticationManager** class
### Transactional
- Make the transactional mechanism when Creating/Updating/Deleting product. They will persist to database and update the dto to productList
- Make the transactional request when customers do checking out products from their carts. All products into cart will be change quantity and update to productList. 
## TODO:
- After checking out and persisting to database, call to message broker to separate the asynchronous flow (notification email, sms, update loyalty, reconciliation,...)
## Discussion: auto-scaling
- In this service, we do use the session scope to manage the customer cart. All the manipulation from customer will reflect and manage by a map: productList that stores the remaining quantity of products.
- With the mechanism of loading all products and store to map: productList, it helps to check & manage quantity rapidly.
- But we also have the trade-off: It needs more RAM memory to store product, and difficult auto-scaling.
- Think about scaling: We can have more than 1 instance of service by hashing product information and divide equally to each instance (configure in load balancer). But, in the situation of very large workload, we need add more instance, we need change the hash function and restart system.
- For the resilience problem, we can apply the master/slave architecture. Whenever the master is down, we can swith to slave instance.
# Admin-on-Rest Demo + Java Spring Boot/MySQL REST Backend

This is a Spring-based demo of the [admin-on-rest](https://marmelab.com/admin-on-rest) library for React.js. It creates a working administration for a fake poster shop named Posters Galore.

Admin-on-rest requires a REST server which is provided in this bundle in the /backend folder.

## Front-End 

In the project directory /src, you can run:

`npm start`

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

## Back-End

This backend implementation allows the use of admin-on-rest as front-end replacing FakeRest (JS) with Java Spring backend/MySQL.

## Configuration

Create a database called demo in MySQL. The credentials are being configured in application.properties. Run with mvn spring-boot:run or from jar.

### Features

- Automatic Generation of database tables according to the Java classes annotated with `@Entity`
- Automatic filling of data from https://raw.githubusercontent.com/zifnab87/react-admin-demo-java-rest/master/backend/src/main/webapp/WEB-INF/uploaded/data.json
- Rest API based on admin-on-rest conventions (e.g resource names and calling signatures: https://marmelab.com/admin-on-rest/RestClients.html)
- Built-in User Authentication (followed this implementation: https://auth0.com/blog/securing-spring-boot-with-jwts/)
- Easily expandable by adding a new `@Entity` class, extending `BaseRepository<T>`, extending `BaseController<T>` both provided by https://github.com/zifnab87/react-admin-java-rest
- Paging and Sorting behind the scenes support using `PagingAndSortingRepository` provided by Java Spring-Data
- Text Search among all text fields using q parameter 
- Exact Match filtering among the rest of the fields of a resource
- All query building is happening behind the scenes using Specifications and Criteria API provided by Java Spring-Data
- Ability to support snake_case or camelCase variables by setting (`react-admin-api.use-snake-case = true` (default = false)) in application.properties
- Automatic wrapping of responses in "content" using `@ControllerAdvice` provided by https://github.com/zifnab87/react-admin-java-rest/blob/master/src/main/java/reactAdmin/rest/controllerAdvices/WrapperAdvice.java
- Automatic calculation total number of results returned and addition of that number in `X-Total-Count` response header provided as `@ControllerAdvice` by https://github.com/zifnab87/react-admin-java-rest/blob/master/src/main/java/reactAdmin/rest/controllerAdvices/ResourceSizeAdvice.java
- Automatic deserialization of entities by their ids only during POST/PUT, using `@JsonCreator` annotations over constructors of Entities - see here for explanation: https://stackoverflow.com/questions/46603075/single-custom-deserializer-for-all-objects-as-their-ids-or-embedded-whole-object


### Future work

- ~~Make the project runnable through Maven - currently it is a IntelliJ Idea Maven project~~ **DONE**
- ~~Be able to combine results from Text Search and Exact Match filtering~~ **DONE**
- Indexes that might be missing currently
- Swagger-UI needs to be excluded properly from authentication


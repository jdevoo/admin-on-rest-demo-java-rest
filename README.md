# Admin-on-rest Demo + Java Spring Boot/MySQL REST Backend

This is a demo of the [admin-on-rest](https://github.com/marmelab/admin-on-rest) library for React.js. It creates a working administration for a fake poster shop named Posters Galore. You can test it online at http://marmelab.com/admin-on-rest-demo.

[![admin-on-rest-demo](https://marmelab.com/admin-on-rest/img/admin-on-rest-demo-still.png)](https://vimeo.com/205118063)

Admin-on-rest requires a REST server which is provided in this bundle in the /backend folder

To explore the source code, start with [src/index.js](https://github.com/marmelab/admin-on-rest-demo/blob/master/src/index.js).

**Note**: This project was bootstrapped with [Create React App](https://github.com/facebookincubator/create-react-app).

## Front-End 

In the project directory /src, you can run:

### `npm start`

Runs the app in the development mode.<br>
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br>
See the section about [running tests](#running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

### `npm run deploy`

Deploy the build to GitHub gh-pages.

## Back-End

This backend implementation is the result of considerable effort to use admin-on-rest as front-end but migrate away from Headless Drupal backend (PHP) to Java Spring backend/MySQL. The main reasons including: lack of versioning for backend changes (we had to take data dumps and keep a txt with Drupal changes), time consuming configuration of Views involving many entities and fields, in need of many (some times non-existing) Plugins to do common things, not native REST implementation, queries involving a ton of tables due to Drupal field reusability among different nodes, difficulty combining drupal tables with flat tables for big-data and analytics, etc..

## Configuration

You need a database called demo. The credentials are being configured in application.properties. Open the project using existing resources and select Maven from IntelliJ Idea menu.

### Features

- Automatic Generation of database tables according to the Java classes annotated with `@Entity`
- Rest API based on admin-on-rest conventions (e.g resource names and calling signatures: https://marmelab.com/admin-on-rest/RestClients.html)
- Built-in User Authentication (followed this implementation: https://auth0.com/blog/securing-spring-boot-with-jwts/)
- Paging and Sorting support using `PagingAndSortingRepository` provided by Java Spring-Data
- Text Search among all text fields using q parameter 
- Exact Match filtering among the rest of the fields of a resource
- All query building is happening behind the scenes using Specifications and Criteria API 
- Easily expandable by adding a new `@Entity` class, extending `GenericRepository<T>` and making a similar Controller to the existing ones 

### Future work

https://marmelab.com/admin-on-rest/RestClients.html
- Make the project runnable through Maven - currently it is a IntelliJ Idea Maven project
- Be able to combine results from Text Search and Exact Match filtering
- Indexes that might be missing currently
- some fixes for embedded lists for some of resources




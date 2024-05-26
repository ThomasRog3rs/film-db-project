
# Film Project

The Film Project is a web service that interacts with a database to serve the data to clients
who request data. This project was created by Thomas Rogers for MUU web services assignment. On this assignment, I achieved a 1st Class Distinction

## Deployment - Azure
1.  Create a resource group for the project
1.  Create a MySQL database with that resource group assigned
1.  Migrate database to the MySQL database
1.  Create an App Service with your same resource group
1.  Get connection string for Azure DB and add to the DAO
1.  Install Azure tools for eclipse
1.  Login
1.  Right click on the application -> azure -> Publish as azure web application
1.  Select your App Service and deploy.
1.  Check it out at https://{appServiceName}.azurewebsites.net

### My live application

[https://filmwebservice.azurewebsites.net](https://filmwebservice.azurewebsites.net/)

### API Endpoints
#### GET
URL/Films - returns all fimls
URL/Films?FilmID=10001 - returns just one film or an error message

#### POST
URL/Films - takes in a body of a film object and returns a success or error message (no ID in film object)

#### PUT
URL/Films - takes in a body of a film object and returns a success or error message (Requires ID in film object)

#### DELETE
URL/Films?FilmID=10001 - returns success or error message

### Project Critical Analysis
[Report](https://1drv.ms/w/s!Apuzvs_WyaRmhqwnuJ15fu_rahuwDg?e=SlVbTL)

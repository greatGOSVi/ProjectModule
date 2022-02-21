# Project Module with oAuth2.0
A module to manage company projects info, like their names, description, personnel involved, and cost.

## How to run:

### 1.- Install Docker
It is optional to install Docker Desktop; I recommend to do so

### 2.- Run these commands in terminal as administrator:
netsh int ip add addr 1 10.5.0.4/32 st=ac sk=tr

netsh int ip add addr 1 10.5.0.5/32 st=ac sk=tr

netsh int ip add addr 1 10.5.0.6/32 st=ac sk=tr

netsh int ip add addr 1 10.5.0.7/32 st=ac sk=tr

### 3.- In the root folder of the project, execute in terminal:
docker-compose up -d

### 4.- Populate the databases:
Once your docker containers are up, open your Docker Desktop app and 
enter the terminal of the authorization-database container and execute all queries in auth-db-queries.sql;
then, open the terminal of the projectmodule-database container and execute all queries in projectmodule-db-queries.sql

### 5.- Log in:
Login in the redirect login form found in http://localhost:8080. The aviable users are:

Username: julio.vargas@theksquaregroup.com, Password: user (ROLE_USER)

Username: guillermo.ceme@theksquaregroup.com, Password: manager (ROLE_MANAGER)

Username: carlos.reyes@theksquaregroup.com, Password: admin (ROLE_ADMIN)

## Test it!
Now, everything ready to test the endpoints.

The aviable endpoints are:

(ROLE_ADMIN) Get: /projects

(ROLE_ADMIN) Get: /projects/{projectId}

(ROLE_ADMIN, ROLE_MANAGER, ROLE_USER) Get: /projects/my_projects

(ROLE_ADMIN) Post: /projects

(ROLE_ADMIN, ROLE_MANAGER) Patch: /projects/{projectId}

(ROLE_ADMIN, ROLE_MANAGER) Patch: /projects/close/{projectId}

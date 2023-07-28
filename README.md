# Spring-Boot-Employee

## Install Dependencies

`mvn clean install`

## Run

### Description

Configure Following Environment Variables 

- `MYSQL_DATABASE`
- `MYSQL_HOST`
- `MYSQL_PASSWORD`
- `MYSQL_PORT`
- `MYSQL_USER`

`mvn spring-boot:run`

## Test 

`mvn clean test`

`mvn site -DgenerateReports=false`

## Coverage

![EmployeeApplication](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/a4d8be29-3099-4301-a35e-0e209813b875)

![EmployeeController](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/64bcc7f4-08c5-4ea1-ad7b-08020fc41815)

![EmployeeService](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/ba2032ed-8e33-4f0c-9721-02f1d2194f13)

## Docker Run

### Install Docker Compose 

#### LINUX OR MAC

`sudo curl -SL https://github.com/docker/compose/releases/download/v2.20.2/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose`

`sudo chmod +x /usr/local/bin/docker-compose`

`docker-compose --version`

#### Other OS

https://docs.docker.com/compose/install/

### Description

Runs the application along with MySQL Server

`docker-compose up --detach`

`docker-compose down`

## Tech Stack Used

- Maven
- Docker
- JUnit (Unit Testing)
- Jacoco (Code Coverage)
- Mockito (Mock Database)
- Surefire (Test Reports)
- MySQL
- AWS EC2 (Database Deployment)
- AWS ECS (Application Deployment)
- Github Actions (CICD)



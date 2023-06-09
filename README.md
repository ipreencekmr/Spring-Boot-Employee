# Spring-Boot-Employee

## Install Dependencies

`mvn clean install`

## Run

`mvn spring-boot:run`

## Test 

`mvn clean test`

`mvn site -DgenerateReports=false`

## Coverage

![EmployeeApplication](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/a4d8be29-3099-4301-a35e-0e209813b875)

![EmployeeController](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/64bcc7f4-08c5-4ea1-ad7b-08020fc41815)

![EmployeeService](https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/ba2032ed-8e33-4f0c-9721-02f1d2194f13)

## Docker Run

`docker build -t demo:latest`

`docker run -p 8080:8080 demo:latest`


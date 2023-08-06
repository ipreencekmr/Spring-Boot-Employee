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

`docker-compose up --force-recreate -V --detach`

`docker-compose down`

### Java CI Workflow

<img width="1468" alt="JavaCI-Success" src="https://github.com/ipreencekmr/Spring-Boot-Employee/assets/3636918/7b3c6235-4864-4765-a1bc-495076838191">

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


## Available Services 

1. Create Qualification

``` 
URL: http://localhost:8080/qualifications
METHOD: POST
BODY:

{
    "value":"MCA"
}

RESPONSE:

{
    "id": 1,
    "value": "MCA"
}
```

2. Create Department

``` 
URL: http://localhost:8080/departments
METHOD: POST
BODY:

{
    "value":"Quality Analyst"
}

RESPONSE:

{
    "id": 1,
    "value": "Quality Analyst"
}
```

3. Create Employee

``` 
URL: http://localhost:8080/employees
METHOD: POST
BODY:

{
    "firstName":"Prince",
    "lastName":"Sharma",
    "emailId":"prince.sharma@gmail.com",
    "age":30,
    "gender":"MALE",
    "department":{
        "id": 1,
        "value": "Quality Analyst"
    },
    "qualification":{
        "id": 1,
        "value": "MCA"
    },
    "address":{
        "addressLine1":"Tower 9",
        "addressLine2":"CPR",
        "aptSuite":"705",
        "society":"AVL36",
        "city":"Gurgaon",
        "state":"Haryana",
        "country":"India",
        "zipCode":"122004"
    }
}

RESPONSE:

Status 201 Created

```

4. Get Qualifications
```
URL: http://localhost:8080/qualifications
METHOD: GET
RESPONSE:

[
    {
        "id": 1,
        "value": "MCA"
    }
]

```

5. Get Qualification By ID

```
URL: http://localhost:8080/qualifications/1
METHOD: GET
RESPONSE:

{
    "id": 1,
    "value": "MCA"
}
```

6. Get Departments

``` 
URL: http://localhost:8080/departments
METHOD: GET
RESPONSE:

[
    {
        "id": 1,
        "value": "Quality Analyst"
    }
]
```

7. Get Department By ID

``` 
URL: http://localhost:8080/departments/1
METHOD: GET
RESPONSE:

{
    "id": 1,
    "value": "Quality Analyst"
}
```

8. Get Employees 

``` 
URL: http://localhost:8080/employees
METHOD: GET
RESPONSE:

[
    {
        "id": 1,
        "firstName": "Prince",
        "lastName": "Sharma",
        "emailId": "prince.sharma@gmail.com",
        "age": 30,
        "gender": "MALE",
        "status": "ACTIVE",
        "department": {
            "id": 1,
            "value": "Quality Analyst"
        },
        "address": {
            "id": 1,
            "zipCode": 122004,
            "addressLine1": "Tower 9",
            "addressLine2": "CPR",
            "aptSuite": "705",
            "society": "AVL36",
            "city": "Gurgaon",
            "state": "Haryana",
            "country": "India"
        },
        "qualification": {
            "id": 1,
            "value": "MCA"
        }
    }
]

```

9. Get Employee By ID

``` 
URL: http://localhost:8080/employees/1
METHOD: GET
RESPONSE:

{
    "id": 1,
    "firstName": "Prince",
    "lastName": "Sharma",
    "emailId": "ipreencekmr@outlook.com",
    "age": 31,
    "gender": "MALE",
    "status": "ACTIVE",
    "department": null,
    "address": {
        "id": 2,
        "zipCode": 122004,
        "addressLine1": "Tower 9",
        "addressLine2": "CPR",
        "aptSuite": "705",
        "society": "AVL36",
        "city": "Gurgaon",
        "state": "Haryana",
        "country": "India"
    },
    "qualification": null
}

```

10. Update Qualification
``` 
URL: http://localhost:8080/qualifications/1
METHOD: PUT
BODY:

{
    "value":"M.C.A."
}

RESPONSE:

{
    "id": 1,
    "value": "M.C.A."
}
```

11. Update Department

``` 
URL: http://localhost:8080/departments/1
METHOD: PUT
BODY:

{
    "value":"Quality Assurance Engineer"
}

RESPONSE:

{
    "id": 1,
    "value": "Quality Assurance Engineer"
}
```

12. Update an Employee

``` 
URL: http://localhost:8080/employees
METHOD: PUT
BODY:

{
    "firstName":"Prince",
    "lastName":"Sharma",
    "emailId":"ipreencekmr@outlook.com",
    "age":31,
    "gender":"MALE",
    "address":{
        "addressLine1":"Tower 9",
        "addressLine2":"CPR",
        "aptSuite":"705",
        "society":"AVL36",
        "city":"Gurgaon",
        "state":"Haryana",
        "country":"India",
        "zipCode":"122004"
    }
}

RESPONSE:

{
    "id": 1,
    "firstName": "Prince",
    "lastName": "Sharma",
    "emailId": "ipreencekmr@outlook.com",
    "age": 31,
    "gender": "MALE",
    "status": "ACTIVE",
    "department": null,
    "address": {
        "id": 2,
        "zipCode": 122004,
        "addressLine1": "Tower 9",
        "addressLine2": "CPR",
        "aptSuite": "705",
        "society": "AVL36",
        "city": "Gurgaon",
        "state": "Haryana",
        "country": "India"
    },
    "qualification": null
}

```


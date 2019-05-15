# Fare Flight Search

## Tools used
    - Spring boot 2.1.2.RELEASE
    - spring-boot-starter-test
    - spring-boot-starter-data-redis
    - Junit 5
    - Flyway
    - Mysql 8.0
    - JDK 1.8
    
## How to set up
    Mysql Database
    * Create database on mysql with name "meta_search"
    * Username: root
    * Password: mysql
    * Port: 3306
    
    Redis
    * Install and start redis server as PORT 6379

## Building JAR File
    
    Run mvn command:
    mvn clean install
    
## Run application
     
    Run command: 
    java -jar target java -jar target\metasearch-0.1.0-SNAPSHOT.jar  
    
    By default port: 8080
    
    * All necessary tables and data should be generated
    
## Sample request and response
    
    1. http://localhost:8080/api/flights   
    
    Request
    
    Body as JSON: 
        {
            "departureCode": "AAR",
            "arrivalCode": "ADB",
            "localDate": "2019-09-09"
        }
        
    Response     
        {
            "generatedId": "5e5a5f25-180b-43c1-9c44-7a9ed60a4c42",
            "offset": 0,
            "scheduleResponses": null
        }
    
    2. http://localhost:8080/api/flights/5e5a5f25-180b-43c1-9c44-7a9ed60a4c42?offset=7
    
    Request
    
        Path param: 5e5a5f25-180b-43c1-9c44-7a9ed60a4c42
        Query param: 0
        
    Response
        {
            "generatedId": "5e5a5f25-180b-43c1-9c44-7a9ed60a4c42",
            "offset": 7,
            "scheduleResponses": [
                {
                    "id": 4,
                    "departAirportCode": "AAR",
                    "arrivalAirportCode": "ADB",
                    "providerCode": "aircanada.com",
                    "basePrice": 431.3048106547333
                }
            ]
        }

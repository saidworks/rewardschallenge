# Rewards API 
> This API allows users to add and spend rewards as well as view the total balance of points per payer. An additional feature allows user to view all reward transactions. 

## Installation 
To run this app, you will need:   
-  Java Development Kit 11 (JDK 11) or higher (click on the links below for installation instructions by Operating System): 
        -  [macOS](https://docs.aws.amazon.com/corretto/latest/corretto-18-ug/macos-install.html)
        - [Windows](https://docs.aws.amazon.com/corretto/latest/corretto-18-ug/windows-10-install.html) 
        - [Linux](https://docs.aws.amazon.com/corretto/latest/corretto-18-ug/generic-linux-install.html)
- MAVEN (the Java dependency manager) - [installation instructions](https://maven.apache.org/install.html).
           

After setting up your dev environment, run the server from the root directory of the project by entering the following commands in the your terminal:

```sh
cd CodingExercise
mvn spring-boot:run
```

## Endpoints
After running the app, the following endpoints are available:
- You can add a reward with a POST request to:
    ```sh
    localhost:8080/add
    ```
    and with a JSON object in the body containing the payer name, number of points and the timestamp for the transaction, e.g: 
     ```sh 
    { "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }
    ```

- You can get all of the transactions you made so far with a GET to:
    ```sh
    localhost:8080/transactions
    ```
- You can get your balance with a GET request to:
    ```sh
    localhost:8080/balance
    ```
- You can spend points from your balance with a POST request to:
    ```sh
    localhost:8080/spend
    ```
    and with a JSON object in the body containing number of points to spend, e.g:
    ```sh 
    { "points": 5000}
    ```

## Tech Stack
- Java
- Maven as a dependecy manager
- Spring boot framework using the following dependencies:
    - Spring Data JPA
    - H2 In-memory Database
    - Spring Web 


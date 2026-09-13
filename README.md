# Parking System

An object-oriented Java application designed to manage parking customers, vehicles, permits, addresses, and parking transactions.

This project was developed as part of the University of Denver software development coursework and focuses on applying object-oriented design, Java, Maven, and automated unit testing.

## Project Overview

The Parking System is intended to support a university parking operation where customers can register with the parking office, associate multiple vehicles with their accounts, and obtain permits for those vehicles. Parking transactions are associated with customer accounts and may be subject to different rates depending on the parking lot and vehicle type.

The system is designed using separate classes that represent the major entities and behaviors of the parking operation.

## Technologies

* **Java**
* **Maven**
* **JUnit 5**
* **Git**
* **Visual Studio Code**

Java and JUnit versions are documented in the project's `pom.xml` file.

## Project Structure

The project follows a standard Maven structure:

```text
du_parking_app/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── example/
    │               └── parkingsystem/
    └── test/
        └── java/
            └── com/
                └── example/
                    └── parkingsystem/
```

### Main Classes

The application uses classes to represent the primary objects in the parking system. These include customers, addresses, vehicles, permits, parking lots, and parking transactions, along with any supporting classes implemented for the application.

Each class is responsible for representing its own data and behavior rather than placing all functionality into a single class.

## Testing

The project uses **JUnit 5** for automated unit testing.

Current unit tests include tests for functionality such as:

### Customer

`CustomerTest` verifies that a customer's name is correctly generated from the first and last name.

Example:

```text
Input:
First Name: John
Last Name: Smith

Expected:
John Smith
```

### Address

`AddressTest` verifies that the address information is correctly formatted from the individual address fields.

Example:

```text
Input:
123 Main St
Apt 4
Springfield, IL 62701

Expected:
123 Main St, Apt 4, Springfield, IL 62701
```

Additional tests can be added as new functionality is implemented.

## Running the Tests

Maven is used to compile the project and execute the JUnit test suite.

From the project root, run:

```bash
mvn test
```

A successful test run should end with Maven reporting:

```text
BUILD SUCCESS
```

The Maven command also compiles the application and test classes before executing the tests.

## Design Approach

The project uses object-oriented principles by representing real-world parking system entities as individual Java classes.

For example, a `Customer` contains information about a customer and maintains an association with an `Address`. Other classes represent vehicles, permits, parking lots, and parking transactions.

This approach allows individual classes to have clearly defined responsibilities and makes the system easier to test and maintain.

## Development Notes

The project is being developed incrementally. Classes and functionality are implemented and tested as the design develops.

Unit tests are used to verify individual methods and behaviors before relying on them as part of larger system functionality. Maven provides a consistent way to compile the project and execute the automated tests.

## Author

Developed as part of University of Denver software development coursework.

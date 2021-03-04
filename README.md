# Ledger Bank
## Project Description
This is a online banking portal using a Java/PostgreSQL backend with the Jackson and Jersey frameworks connected to a RESTful frontend built with React.

## Technologies Used
Java - version 1.8
Jersey - version 2.31
Jackson - version 2.11
PostgreSQL - version 13
Maven - 3.2.4
React - version 17
Tomcat - version 9

## Features

* Responsive, simplified frontend using React to render JSON passed from API calls
* Secure login into system using PGcrypto to securely hash passwords into database 
* Validates the availability of funds with attempting withdrawals and accepting transactions

## To-do list:

* Implement quick setup in main class to initialize PostgreSQL database from Java code
* Implement authorization controls on all transactions

## Getting Started

Install Java 1.8 or higher
Install PostgreSQL 13 or higher
Install Tomcat 9 or higher
    git clone https://github.com/LittleKingsguard/RevatureAssignments.git
Build Maven project to install dependencies
Execute all scripts in Postgres init script.txt
Set the Tomcat server to deploy the Maven build artifact of the project after compilation

## Usage

  Initialize the Tomcat server after setting up the runtime environment for deployment
  After Tomcat startup, project should be available at localhost:8080/Project1

## License
This project uses the following license: Copyleft GPLv3

# Rabobank_BE
FileProcessor
The software module, built using Spring Boot, consists of REST service that processess input files.

What's the purpose of this software Module?
This module processess the file(.csv/xml) which contains the transaction information of a customer and validates it for the uniqueness of the transaction reference number and the transaction amount at the end of each transaction.

Any discrepencies found during the validation will be sent as an output to the validator.

Getting Started
The following software are necessary and can be downloaded from the links given below.

JDK 1.8.
Gradle 4.8.1.
Intellj.
Gradle plugin for Eclipse.
After downloading JDK and Gradle, set up the environment variable in your system.

Setup the project
Open the Eclipse IDE and clone the project from github link to a local folder in your system.
Import the cloned project in to eclipse as a Gradle project in to Eclipse IDE. 3.Perform a Gradle refresh to download the dependencies.
How to run the project
Option 1: Running as Gradle Spring Boot application
Open command prompt on the project location.
Run the following command,
gradle clean build bootrun
Option 2: Running as Spring Boot fom the Eclipse IDE
As the project is imported as a Gradle project, you can run it from the IDE as a Spring Boot App.
That's it! The application can be accessed at http://localhost:9091

If the port 9091 is being used by any other application, then make the following entry in application.properties and use the port that is free.

server.port=9091

Testing the API:
Use postman or any other tool that can interact with HTTP APIs.
Attach the 'records.csv' or 'records.xml'(The sample files are available in 'src/main/resources' folder) file along with the request and hit the URL it.
Response will be the list of transaction references that are not valid.
How to test using POSTMAN:
Create a new request in postman.
Ensure that the type of request is POST.
Key in the REST URI, 'http://localhost:9091/processFile'
In the Authorization tab, add the authentication credentials as shown in the image below and update the request. Username is, "rabobank" Password is, "rabobank"
Adding Authentication

In the Body tab, add a key named 'file' and choose the file that needs to be processed a shown in the image below.
Adding file

Send the request and the resulting JSON will be returned as shown in the below folder.
Adding file

https://github.com/venkataramansb/Rabobank_BE/blob/master/postman

Coverage Information available in belwo folder
https://github.com/venkataramansb/Rabobank_BE/blob/master/coverage


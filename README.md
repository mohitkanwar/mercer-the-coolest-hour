# mercer-the-coolest-hour

## User Story
As a user running the application    
I can view tomorrow’s predicted temperatures for a given zip-code in the United States  
So that I know which will be the coolest hour of the day.  

## Architecture
Spring-Boot MVC Architecture

## Executable Jar
Executable Jar can be downloaded from https://github.com/mohitkanwar/mercer-the-coolest-hour/blob/master/Release/coolest-hour-1.0-RELEASE.jar

## Release Notes
Release Notes and instructions to use are available at https://github.com/mohitkanwar/mercer-the-coolest-hour/blob/master/Release/Release%20Notes.docx?raw=true

## Execution Instructions
Minimum Java Version 8 is required. This application has been tested on
  * Ubuntu 18.04 , Java 8
  * Windows 10, Java 10

1) Download the executable jar (shared above).
2) execute the jar and provide the zipcode (only U.S) for which temperatures for tomorrow are expected as the first argument.
e.g. : `java -jar coolest-hour-1.0-RELEASE.jar 10001` returns the temperature report for tomorrow in New York.
The report highlights the coolest hour

Please refer to release notes for detailed instructions.

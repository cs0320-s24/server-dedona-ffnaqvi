> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
Sprint 2: Server  
Team members and contributions:         
My team partner and I did the main framework of the project side by side, but the division of work was as following:       
dedona: implemented searchCSV/loadCSV/viewCSV, CensusHandler, CSV endpoint testing, error checking, commenting, debugging, and original CSV parser       
ffnaqvi: implemented searchCSV/loadCSV/viewCSV, Census endpoint testing, error checking, commenting, debugging, and caching framework       
[(https://github.com/cs0320-s24/server-dedona-ffnaqvi)](https://github.com/cs0320-s24/server-dedona-ffnaqvi.git)            
This project probably took us ~20 hours         

# Design Choices
The program has the following packages:
* datasource, which contains csv files for use
* CSVParser, which contains the CSVParser<T> class, LoadCSVHandler, ViewCSVHandler, and SearchCSVHandler classes
  * the CSVParser<T> parses the inputted data based on the inputted CreatorFromRow<T>'s specifications
  * the LoadCSVHandler class servers as the endpoint for loading a csv and takes in a filename to parse; returns status code to indicate success/failure
  * the ViewCSVHandler class servers as the endpoint for viewing a csv; only works upon CSV load succcess
  * the SearchCSVHandler class servers as the endpoint for loading a csv and takes in a searchValue, columnIdentifier, and boolean for headers; only works upon loadCSV success
* Server, which contains the Server class
  *  the Server class initializes the localhost connection and sets up the endpoints of the API
* Exceptions, which contains the FactoryFailureException class
  *  the FactoryFailureExceptionClass class is a custom error for failure of parsing CSVs
* Census, which contains the Census, CensusAPIUtilities, and CensusHandler classes
  * s
* Caching, which contains the ACSDatasource interface, CachedCensusHandler, and MockedAPIDatasource classes
  *  V
* Search, which contains the Search class
  * the Search class is a class designed for users specifically searching Strings from .csv files
* Creators, which contains the CreatorFromRow<T> interface, IntegerCreator, ListStringCreator, and StringCreator classes
  * the ListString class implements the CreatorFromRow<T> interface for List<String>
  * the IntegerCreator class implements the CreatorFromRow<T> interface for Integers
  * the String class implements the CreatorFromRow<T> interface for String
  * the CreatorFromRow<T> interface allows for customization of creation of rows
       
We made the following design choices:
* Handle errors by printing a descriptive message to the user endpoint
* Create the ACSDatasouce interface to create classes that had the ability to send requests
* Create a MockedAPIDatasource to reduce the calls to the ACS API when testing
* Create separate classes for each of the endpoints to handle the specific parameters and errors for each endpoint
* Create a caching class with parameters that can be changed for maximum space efficiency and customizability
* Have a shared state (variables public to Server) between different CSVEndpoints to connect loadStatus
(The following are the design choices for CSVParsing)
* CSVParser is a generic type class
* CSVParser has one constructor for ease of use
* CSVParser's constructor takes in highly malleable parameters
* Search is highly specific for user use
* Collect all of the information for other classes in Main for high class specificity
* Created CreatorFromRow<T> interface to allow for parsing of custom classes
* Abstracted the FactoryFailureException in main so that the developer has a clearer concept of the issue
# Errors/Bugs
This isn't a bug but I have a setup issue I wasn't able to get fixed at conceptual hours/Ed in which I can't import the checkstyle format. See ed post #12 for details. I will attempt to get this fixed at the next conceptual hours as well. 
# Tests
TODO
I tested for the following cases using JUnit tests:
* CSV data with and without column headers through varying input of the CSVParser
  * this was done in the withHeaders() and withoutHeaders() tests 
* CSV data in different Reader types (e.g., StringReader and FileReader) through varied input of the type of reader
  * this was done in the differentReaders() test
* CSV data with inconsistent column count through testing the FactoryFailureException from CSVParser<T>
  *  this was done in the testMalformedData() test
* Searching for values that are, and aren’t, present in the CSV through a combination of CSVParser<T> and Search
  * this was done in testBasicSearch() by checking the number of rows
* Searching for values that are present, but are in the wrong column through CSVParser<T input
  * this was done in testSearchByCol() tests
* Searching for values by index, by column name, and without a column identifier
  * this was done in testSearchByCol() and testSearchByRow() tests 
* Using multiple CreatorFromRow classes to extract CSV data in different formats.
  * this was done in the flowerTest() test through the use of the Flower object and CreatorFromRow<T> implementation in FlowerRowCreator 
* CSV data that lies outside the protected directory
  * tested this in testFileAccess() to verify an invalid file will not be searched

I also tested the user interface manually through a variety of different inputs.
# How to...
This is a CSVParser Library that has functionality for both an end-user and developers. The following classes/tools are intended for users:
* the REPL() method in Main, which prompts the User in the terminal for relevant information before searching a specified file
* the Search Class, which takes in specific types for file searching
* the RowCreator class, which preserves the List<String> data structure

The following classes/tools are intended for developers and users:
* The CSVParser Class, which takes in generic types in the parameter to ensure versatility
* the CreatorFromRow<T> Interface, which creates a generic type from a specified input to ensure versatility
* the README, which details everything in the project!

To use this library as a user, simply run main and follow the prompts in the terminal. The user interface assumes parsing through a file of strings.  
    
To use this library as a developer, implement the CreatorFromRow<T> interface and use the CSVParser<T> class to customize the way you would like your Object to be parsed. See the Flower package for an example of parsing an object.
 

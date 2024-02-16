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
  * the Census class is a class with the state, county, and percentage of broadband fields that is deserialized using Moshi
  * CensusAPIUtilities defines the utilities for the Census class, including deserialization
  * the CensusHandler class servers as the broadband endpoint and handles sending requests to the ACS API
* Caching, which contains the ACSDatasource interface, CachedCensusHandler, and MockedAPIDatasource classes
  *  the ACSDatasource interface contains a method for sendingRequests
  *  the CachedCensusHandler sets up a LoadingCache and fills it according to the users preference
  *  MockedAPIDatasource uses a constant class to test the program without spamming calls to the ACS API
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
* A caching proxy class that serves as an intermediary for caching    
* Use Moshi for Json deserialization
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
We had the following testing suites:       
* TestCachedCensus, which uses mocking to test the ACS API caching
  * X tests
* TestCensusAPIUtilities, which tests the deserialization of the Census data through backend testing
  * X tests
* TestCensusHandler, which uses integration testing to ensure the API endpoint is being reached correctly
  *  X tests
* LoadCSVHandlerTests, which tests the functionality of the loadcsv endpoint through integration and backend testing
  * X tests
* ViewCSVParserTests, which tests the functionality of the viewcsv endpoint through integration and backend testing
  * X tests
* SearchCSVHandlerTests, which tests the functionality of the searchcsv endpoint through integration and backend testing
  * X tests

We also tested the user interface manually through a variety of different inputs in the browser.
# How to...
This is a WebAPI tool that can connect to local CSV files and the ACS API and has functionality for both an end-user and developers. The following classes/tools are intended for users:
* the REPL() method in Main, which prompts the User in the terminal for relevant information before searching a specified file
* the Search Class, which takes in specific types for file searching
* the RowCreator class, which preserves the List<String> data structure

The following classes/tools are intended for developers and users:
* The CSVParser Class, which takes in generic types in the parameter to ensure versatility
* the CreatorFromRow<T> Interface, which creates a generic type from a specified input to ensure versatility
* the README, which details everything in the project!

To use this library as a user, simply run main and follow the prompts in the terminal. The user interface assumes parsing through a file of strings.  
    
To use this library as a developer, implement the CreatorFromRow<T> interface and use the CSVParser<T> class to customize the way you would like your Object to be parsed. See the Flower package for an example of parsing an object.

To use this project as a front-end developer making requests on CSVs, you can interact with the web API to load, view, or search the contents of a CSV file by calling the loadcsv, viewcsv, or searchcsv endpoints. The loadcsv endpoint requires a file path parameter, and only one CSV dataset can be loaded at a time. Attempting to use viewcsv or searchcsv without a loaded CSV will produce an error response without halting the server. The search functionality allows searching by column index, column header, or across all columns. The server can be started via a command-line entry point (./run in the terminal), or by using the IntelliJ run button. The interaction with the server's functionalities primarily occurs through the API endpoints and is accomplished through the Server, LoadCSVHandler, ViewCSVHandler, SearchCSVHandler, CSVParser, and Search classes.       

To use this project as a front-end developer making requests to the American Census API, you can retrieve the percentage of households with broadband access for a specified state and county by calling the web API with the state and county names as parameters. The API response includes the date and time of data retrieval from the ACS API, along with the received state and county names. To search the counties for an entire state, simply do not enter a county value. Arbitrary state and county values will send an error message without halting the server. The server can be started via a command-line entry point (./run in the terminal), or by using the IntelliJ run button. This is accomplished through the Server, CensusHandler, Census, and CensusAPIUtilities classes.       

To use this project as a backend developer concerned with caching, you have control over caching ACS request-responses when using the provided classes in the server program. The caching allows for avoiding excessive network requests, with configurable eviction policies to manage stale entries. The strategy pattern can be employed to allow developers to select or customize caching strategies by passing in any parameters to the CachedCensusHandler in Server.java. To remove caching, simply set the time the item is in the cache to 0.

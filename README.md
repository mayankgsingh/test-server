# Quick Introduction
A spring boot based stand alone server to test/support automation HTTP based API calls REST & SOAP. 

## Getting Started
* Refer to ```application.properties``` to change the post of your choice. Currently it is set to 8081.

### REST Interface [```RestController.mockCall()``` ]
* URI pattern: ```/{entity}/{id}```
* Supported Methods: GET, POST, DELETE, PATCH, PUT

#### What it accepts?
* URI: As mentioned call needs to be "/{entity}/{id}" format.
* HTTP Header(optional): Along with that, an optional HTTP header can be set "x-requested-response". This represents the expected return response code. If not passed, then by default application will send 200 HTTP status response code along with reponse body.
* Payload(optional): You can pass in desired payload in JSON format. 

  
#### What it returns?
* HTTP Status Code: As indicated by "x-requested-response" header in request. If not present 200 will be used by default.
* Response - A JSON will be return. It will consist of two parts. 
  * Response: application will look for JSON file at given class path. "<HTTP_METHOD>/<ENTITY>.<HTTP_REQUESTED_STATUS_CODE>.json" Sample, response files are present under "src/main/resources". This information will be wrapped under "response" TAG.
  * Original Request payload: "payload" will indicate, what was passed as request for processing. It includes body, http headers, http method & URL. 

### SOAP Interface 
This implementation relies on Spring SOAP "touch-first" approach. That means design XSD and then generate Java objects. Refer to ```country.xsd``` under ```src/main/resources```.

Any changes to XSD, we have to regenerate the Java object and adjust endpoints implementation if necessary. Once XSD is updated run ```mvn compile``` to generate new/updated resources. 

As of now soap interface is designed to work on Country data. Get all country list or getCountryByName(String iso2code). If SOAP call needs to be extended for a different data set, more development needs to be done to design endpoints.

#### What it accepts?
 * Two sample request XML's are included "requestcountry.xml" & "requestallcountry.xml".
 * URL: http://localhost:8081/ws

## How to run
### Command Line
```
mvn test
```

### IDE (Eclipse/Other)
Look for ```TestServerApplication.java``` and run.

### Rest Call
```
 http://localhost:8081/rest/country/1
 http://localhost:8081/rest/country/iso2=IN
```

Post Call Example with 400 response code
```
curl -X POST --header "content-type: application/json" --header "x-requested-response: 200" http://localhost:8081/rest/employee/1

curl -X POST --header "content-type: application/json" --header "x-requested-response: 400" http://localhost:8081/rest/employee/1
```

### SOAP Call
```
 curl --header "content-type: text/xml" -d @request.xml http://localhost:8081/ws
 curl --header "content-type: text/xml" -d @requestallcountry.xml http://localhost:8081/ws
```

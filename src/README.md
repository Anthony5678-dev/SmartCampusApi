# Smart Campus Sensor and Room Management API

A JAX-RS RESTful API built with Jersey 2.x and an embedded Grizzly HTTP server. No database, no Spring Boot — pure JAX-RS as required.

---

## Setup and Installation

### Prerequisites

* Java 11 or higher
* Apache Tomcat 9
* NetBeans (or any IDE with Maven support)

---

### Run the Project (from GitHub)

1. Clone the repository:


git clone https://Anthony5678-dev/SmartCampusApi.git
cd SmartCampusApi


2. Open in NetBeans:

* File → Open Project → select the folder

3. Build:


mvn clean install


4. Run:

* Right-click project → Run (deploys to Tomcat)

---

### Access the API

```
http://localhost:8080/SmartCampusApi/api/v1/
```

Test:

```
http://localhost:8080/SmartCampusApi/api/v1/rooms
```

---

## Sample Curl Commands



### 1. Get all rooms

curl.exe http://localhost:8080/SmartCampusApi/api/v1/rooms

### Outcome

[{"id":"COMP-C107","name":"Computer Science lab","capacity":35,"sensorIDs":[]},{"id":"DES-002","name":"Dsign Studio","capacity":20,"sensorIDs":[]},{"id":"LAW-B35","name":"Law Lecture Hall","capacity":200,"sensorIDs":[]},{"id":"R1","name":"Library","capacity":50,"sensorIDs":["S1"]}]

### 2. Create a Room

curl.exe --% -X POST http://localhost:8080/SmartCampusApi/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LIB-301\",\"name\":\"Library\",\"capacity\":10}"

### Outcome

{"id":"LIB-301","name":"Library","capacity":10,"sensorIDs":[]}

### 3. Create a Sensor

curl.exe --% -X POST http://localhost:8080/SmartCampusApi/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0,\"roomId\":\"LIB-301\"}"

### Outcome

{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0.0,"roomId":"LIB-301"}

### 4. Get All Sensors

curl.exe http://localhost:8080/SmartCampusApi/api/v1/sensors

### Outcome

[{"id":"TEMP-23","type":"Temperature","status":"ACTIVE","currentValue":21.7,"roomId":"LAW-B35"},{"id":"C02-102","type":"C02","status":"ACTIVE","currentValue":420.5,"roomId":"COMP-C107"},{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":0.0,"roomId":"LIB-301"}]

### 5. Filter Sensors by Type

curl.exe "http://localhost:8080/SmartCampusApi/api/v1/sensors?type=Temperature"

### Outcome

[{"id":"TEMP-23","type":"Temperature","status":"ACTIVE","currentValue":21.7,"roomId":"LAW-B35"},{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":22.5,"roomId":"LIB-301"}]

---

## Conceptual Report

### Part 1.1
JAX-RS uses a request-scoped lifecycle, meaning a new instance of each resource class is created for every HTTP request. Because of this, instance variables cannot be used to store data, as they are lost after each request.If each resource used its own HashMap, it would be reset every time, so no data would persist. To solve this, shared static HashMap collections are used so all requests can access the same data.Since HashMap is not thread-safe, multiple requests modifying it at the same time could cause issues

---

### Part 1.2

HATEOAS (Hypermedia as the Engine of Application State) involves including links in API responses so clients can see what actions are available next, instead of relying completely on external documentation.

For instance, a request to GET /api/v1 might return links to /api/v1/rooms and /api/v1/sensors. This means developers don’t need to hardcode URLs or remember the full API structure, because the server guides them to the next steps.This approach also makes the API more adaptable. If endpoints change in the future, clients that follow the provided links will continue to work without needing updates. For this reason, HATEOAS is seen as a key feature of advanced RESTful design, as it makes the API self-describing and easier to navigate.

---

### Part 2.1

Returning only IDs in a list response reduces the amount of data sent, making it more efficient in terms of bandwidth. However, it means the client would need to make additional requests to retrieve the full details of each room, increasing the number of API calls and slowing down the overall process.

Returning full room objects sends more data in a single response, but it allows the client to access all the required information immediately without making extra requests. In this API, returning full objects is more appropriate because it simplifies client-side processing and avoids unnecessary additional calls.

---

### Part 2.2

The DELETE operation is idempotent because it has the same effect on the server state even if it is called multiple times. After the first successful request, the room is removed from the system. If the same DELETE request is sent again, the room no longer exists, so no further changes are made.

The response may be different for each request. The first request may return 200 OK, while later requests may return 404 Not Found because the room is already deleted. However, idempotency is concerned with the final state of the system, not the response code. In all cases, the room remains deleted, so the operation is idempotent.

---

### Part 3.1

The @Consumes(MediaType.APPLICATION_JSON) annotation means the POST endpoint only accepts requests with a Content-Type: application/json header. If a client sends data in a different format, such as text/plain or application/xml, JAX-RS will automatically reject the request.

In this case, the request is blocked before it reaches the method, and the server returns a 415 Unsupported Media Type response. This happens because JAX-RS checks the content type and only allows formats that match the @Consumes annotation. This removes the need for manual checking and keeps the resource methods focused on the main logic.

---

### Part 3.2

Using @QueryParam for filtering (e.g. GET /api/v1/sensors?type=CO2) is more appropriate than placing the filter in the URL path (e.g. /api/v1/sensors/type/CO2) because it better represents how filtering works on a collection.

Path parameters are meant to identify a specific resource, such as a sensor by its ID, while query parameters are used to apply optional conditions to a collection. In this case, filtering sensors by type is not identifying a single resource, but narrowing down a list.

Query parameters are also more flexible. If no parameter is provided, the endpoint simply returns all sensors, which fits how the current API works. They also allow multiple filters to be combined easily, such as ?type=CO2&status=ACTIVE, without needing extra endpoints.

Overall, using query parameters follows standard REST practices, keeps the API simple, and makes it easier to extend and use, especially in tools like Postman.

---

### Part 4.1

The Sub-Resource Locator pattern allows a nested path to be handled by a separate class instead of putting all logic in one place. In this API, the sensors resource delegates the /sensors/{id}/readings path to a dedicated readings resource class, which then handles the actual requests.

This improves the structure of the application by keeping each class focused on one responsibility. Instead of having one large controller with all endpoints for rooms, sensors, and readings, the logic is split into smaller, more manageable classes.

This makes the code easier to read, test, and maintain. It also allows the API to grow more easily, because new features can be added to specific classes without affecting the rest of the system.

---

### Part 5.2

HTTP 422 is more accurate in this case because the request itself is valid, but the data inside it is incorrect.

When a client sends a POST request to /api/v1/sensors, the endpoint exists and the server understands the request, so a 404 Not Found would be misleading. The issue is not the URL, but the fact that the JSON payload contains a reference (such as roomId) that does not exist.

HTTP 422 Unprocessable Entity is more appropriate because it indicates that the request was understood and correctly formatted, but could not be processed due to invalid data. This makes the error clearer for the client compared to a 404, which would suggest the endpoint itself is missing

---

### Part 5.4

Exposing Java stack traces in API responses is a security risk because it reveals internal details about how the system works.

Stack traces can show information such as class names, method names, package structure, and even line numbers. This allows an attacker to understand how the application is built and how different parts are connected. It can also reveal the technologies and libraries being used, which could help an attacker find known vulnerabilities.

In addition, the sequence of method calls in the trace exposes parts of the application’s logic, making it easier to identify weak points and craft targeted attacks.

For this reason, stack traces should not be shown to users. Instead, they should be logged internally for developers, while the API returns a simple error message such as a 500 Internal Server Error.

---

### Part 5.5

That’s a very solid answer — here’s a cleaner version in your style while keeping the key points:

---

Using a JAX-RS filter for logging is better than adding `Logger.info()` in every resource method because it keeps the code cleaner and more consistent.

A filter applies logging to all requests automatically, so there is no need to repeat the same logging code in every method. This avoids duplication and reduces the risk of forgetting to log certain endpoints.

It also keeps resource classes focused on their main purpose, which is handling business logic, while logging is handled separately. This improves readability and organisation.

Finally, filters are easier to maintain. If the logging format or behaviour needs to change, it can be updated in one place instead of modifying multiple classes.





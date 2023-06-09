# Web API

The webapi is a restful API that provides access to the IoT device's data.

The API is hosted in Google Cloud and powered by Spring Boot. The API returns the following error codes on all
endpoints:

## All Endpoints Status Codes

The following status codes are configured for all endpoints:

- **Status Code: 404**: Not Found, the requested URL doesn't correspond to any endpoint
- **Status Code: 400**: Bad Request, the request exists, but something about it is invalid. If the error is known,
  details are returned in the response body
- **Status Code: 500**: Server Error, the server encountered an error while processing the request, the error is
  returned in the response body

## PUT Endpoints Status Codes

Put methods need to be able to report on the status of the operation, so they return the following status codes:

- **Status Code: 200**: OK, the operation was successfully completed as intended
- **Status Code: 202**: Accepted, the operation was accepted, but it didn't affect the state of the system
- **Status Code: 500**: Server Error, the server did NOT acknowledge the operation

## Sensor Readings Endpoint

### **GET** /readings?requestDate={date(yyyy-mm-dd)}

This endpoint returns the sensor readings for the given date in an array.
If the query doesn't include the `requestDate` parameter, the endpoint returns the readings for the current date.
The individual readings are provided in the following JSON format (order not-guaranteed)

```json
[
  {
    "id": "ID",
    "code": 11,
    "temperature": 2.2,
    "co2": 33,
    "humidity": 44,
    "light": 55,
    "sound": 66,
    "pir": false,
    "time": "7777-88-99 11:22:33.444",
    "comment": "A comment"
  }
  //..
]
```

It can return the following status codes:

- **Status Code: 200**: OK, the readings are returned in the response body

### **PUT** /comment

This endpoint updates the comment for the given date. The comment needs to be provided in the following JSON format as
the request body

```json
{
  "id": "A reading ID",
  "comment": "A comment for that reading"
}
```

## Sensor Limits Endpoint

### **GET** /limits

This endpoint returns the sensor limits. The limits are provided in the following JSON format

```json
{
  "maxTemperature": 11,
  "minTemperature": 22,
  "maxHumidity": 33,
  "minHumidity": 55,
  "maxCo2": 44
}
```

It can return the following status codes:

- **Status Code: 200**: OK, the limits are returned in the response body

### **PUT** /limits

This endpoint updates the sensor limits. The limits need to be provided in the following JSON format as the request body

```json
{
  "maxTemperature": 11,
  "minTemperature": 22,
  "maxHumidity": 33,
  "minHumidity": 55,
  "maxCo2": 44
}
```

## Sensor Status Endpoint

### **GET** /state

This endpoint returns the sensor status. The status is provided in the following JSON format

```json
{
  "isOn": false
}
```

It can return the following status codes:

- **Status Code: 200**: OK, the status is returned in the response body

### **PUT** /state

This endpoint updates the sensor status. The status needs to be provided in the following JSON format as the
request body

```json
{
  "isOn": false
}
```

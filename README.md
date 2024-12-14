## Requirements
Docker

## Installation
After cloning the repository, go to the root directory folder and run the following command:
`docker compose up`

## Swagger
You can find the swagger dashboard at: `http://localhost:8080/swagger-ui/index.html`

## How to Use
`POST /api/payments`  

Creates a new payment. Required fields:  
* firstName
* lastName
* zipCode - It's following the Australian format and should contain 4 digits
* cardNumber - Can have between 15 and 19 digits

`POST /api/payments/webhooks`  

Registers a new webhook. Required fields:  
* url - Must be a valid URL

After creating a new payment the API will send the payment info to all the webhooks currently registered

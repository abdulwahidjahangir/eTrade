# eTrade Backend API Documentation

## Overview

The **eTrade** backend API provides all necessary features for managing an e-commerce platform. This includes handling users, products, orders, categories, and authentication using JWT (JSON Web Tokens) for secure access. This API is built with Spring Boot and follows RESTful principles.


## Key Features of eTrade API

- 🚀 **Seamless Authentication**: Secure user login with JWT tokens for safe and scalable authentication.
- 🛒 **Product Management**: Full CRUD (Create, Read, Update, Delete) operations for products, including detailed information, pricing, and availability.
- 🏷️ **Category Management**: Allows admins to create and manage product categories for easy organization of the catalog.
- 🛍️ **Cart Management**: Guest users can create a cart and place orders without needing to register, offering a seamless shopping experience.
- 📦 **Order Processing**: Enables users to place orders, including managing shipping information and order details.
- 🔐 **Role-Based Access Control**: Differentiated access for admins and regular users, ensuring secure and appropriate permissions.
- 💳 **Dynamic Pricing & Discounts**: Flexible pricing, including handling product discounts and sale prices.
- 📊 **Inventory Management**: Real-time tracking of product stock levels to ensure proper inventory control.
- 💬 **Rich User Feedback**: Handles user registration, email verification, and account status (blocked or verified).
- 🌍 **CORS Enabled**: Configured to support cross-origin requests, allowing secure integration with different domains.
- ⚙️ **Custom Error Handling**: Detailed and descriptive error messages to help with debugging and smooth API consumption.
- 🔄 **Fully RESTful API**: Designed following REST principles for simple and predictable interaction with clients.
- 🔒 **Security-First Design**: Ensures all sensitive data is secured and that users are blocked or restricted as needed.

---

## Authentication

### POST `/api/auth/login`

- **Description**: Logs in a user and returns a JWT token.
- **Request Body**:
    ```json
    {
        "email": "user@example.com",
        "password": "password123"
    }
    ```
- **Response**:
    - **200 OK**:
      ```json
      {
          "accessToken": "jwt_token_here",
          "tokenType": "Bearer"
      }
      ```
    - **401 Unauthorized**: Invalid credentials.

---

### POST `/api/auth/register`

- **Description**: Registers a new user in the system.
- **Request Body**:
    ```json
    {
        "email": "user@example.com",
        "password": "password123",
        "firstName": "John",
        "lastName": "Doe",
        "phone": "1234567890"
    }
    ```
- **Response**:
    - **201 Created**:
      ```json
      {
          "message": "User registered successfully"
      }
      ```
    - **400 Bad Request**: Missing or malformed fields.

---

## Product Endpoints

### GET `/api/products`

- **Description**: Retrieves a list of all products.
- **Query Parameters**:
    - **page** (Optional): The page number (default: `1`).
    - **size** (Optional): Number of products per page (default: `10`).
- **Response**:
    - **200 OK**:
      ```json
      [
        {
            "id": 1,
            "title": "Product 1",
            "description": "Description of product 1",
            "price": 99.99,
            "category": "Electronics",
            "availableStock": 10
        },
        {
            "id": 2,
            "title": "Product 2",
            "description": "Description of product 2",
            "price": 49.99,
            "category": "Clothing",
            "availableStock": 50
        }
      ]
      ```

### POST `/api/products`

- **Description**: Adds a new product to the catalog. Admin users only.
- **Request Body**:
    ```json
    {
        "title": "New Product",
        "description": "Detailed description of the new product",
        "price": 120.00,
        "categoryId": 1,
        "minQuantityToBuy": 1
    }
    ```
- **Response**:
    - **201 Created**:
      ```json
      {
          "message": "Product created successfully",
          "productId": 3
      }
      ```

### GET `/api/products/{productId}`

- **Description**: Fetches a specific product by its ID.
- **Response**:
    - **200 OK**:
      ```json
      {
          "id": 1,
          "title": "Product 1",
          "description": "Description of product 1",
          "price": 99.99,
          "category": "Electronics",
          "availableStock": 10
      }
      ```
    - **404 Not Found**: Product not found.

---

## Category Endpoints

### GET `/api/categories`

- **Description**: Fetches a list of all categories.
- **Response**:
    - **200 OK**:
      ```json
      [
        {
            "id": 1,
            "name": "Electronics",
            "description": "Electronic items"
        },
        {
            "id": 2,
            "name": "Clothing",
            "description": "Men and women clothing"
        }
      ]
      ```

### POST `/api/categories`

- **Description**: Creates a new category. Admin only.
- **Request Body**:
    ```json
    {
        "name": "New Category",
        "description": "Category description"
    }
    ```
- **Response**:
    - **201 Created**:
      ```json
      {
          "message": "Category created successfully",
          "categoryId": 3
      }
      ```

---

## Order Endpoints

### POST `/api/orders`

- **Description**: Creates a new order with a list of products, shipping information, and order details.
- **Request Body**:
    ```json
    {
        "userId": 1,
        "products": [
            {
                "productId": 1,
                "quantity": 2
            }
        ],
        "shippingInfo": {
            "firstName": "John",
            "lastName": "Doe",
            "email": "john@example.com",
            "phone": "1234567890",
            "street1": "123 Main St",
            "street2": "Apt 101",
            "city": "New York",
            "cap": "10001",
            "country": "USA"
        }
    }
    ```
- **Response**:
    - **201 Created**:
      ```json
      {
          "message": "Order created successfully",
          "orderId": 1234
      }
      ```

---

## Admin Endpoints

### GET `/api/admin/users`

- **Description**: Fetches a list of all users. Admin only.
- **Response**:
    - **200 OK**:
      ```json
      [
        {
            "id": 1,
            "email": "user1@example.com",
            "isVerified": true,
            "isBlocked": false
        },
        {
            "id": 2,
            "email": "user2@example.com",
            "isVerified": false,
            "isBlocked": false
        }
      ]
      ```

### PUT `/api/admin/users/{userId}/block`

- **Description**: Blocks a specific user. Admin only.
- **Request Body**:
    ```json
    {
        "isBlocked": true
    }
    ```
- **Response**:
    - **200 OK**:
      ```json
      {
          "message": "User has been blocked successfully"
      }
      ```

---

## Security

### JWT Authentication

JWT tokens are used for authenticating users. To access secured endpoints, include a valid JWT token in the `Authorization` header.

#### Example Login Request:
```json
{
    "email": "user@example.com",
    "password": "password123"
}

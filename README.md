# eTrade Backend API Documentation

## Overview

The **eTrade** backend API provides all necessary features for managing an e-commerce platform. This includes handling users, products, orders, categories, and authentication using JWT (JSON Web Tokens) for secure access. This API is built with Spring Boot and follows RESTful principles.


## Key Features of eTrade API

- 🚀 **Seamless Authentication**:  
  Secure user login with JWT tokens for safe and scalable authentication.

- 🛒 **Product Management**:  
  Manage products with full CRUD (Create, Read, Update, Delete) capabilities, allowing for detailed information on each product, including its variants. Each variant can have unique attributes such as color, size, and pricing, with availability determined by the specific variant.

- 🏷️ **Category Management**:  
  Product categories for easy organization of the catalog.

- 🛍️ **Cart Management**:  
  Guest users can create a cart and place orders without needing to register, offering a seamless shopping experience.

- 📦 **Order Processing**:  
  Enables users to place orders, including managing shipping information and order details.

- 🔐 **Role-Based Access Control**:  
  Differentiated access for admins and regular users, ensuring secure and appropriate permissions.

- 💳 **Dynamic Pricing & Discounts**:  
  Flexible pricing, including handling product discounts and sale prices.

- 💬 **Rich User Feedback**:  
  Handles user registration, email verification, and account status (blocked or verified).

- ⚙️ **Custom Error Handling**:  
  Detailed and descriptive error messages to aid with debugging and ensure smooth API consumption.

- 🔄 **Fully RESTful API**:  
  Designed following REST principles for simple and predictable interaction with clients.

- 🔒 **Security-First Design**:  
  Ensures all sensitive data is secured, and that users are blocked or restricted as needed.

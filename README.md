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
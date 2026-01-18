# Payment Gateway System

A Dockerized payment gateway system with order management, payment processing,
webhooks, retry logic, and a real-time dashboard.

Built using:
- Spring Boot (Backend API)
- React (Dashboard + Checkout)
- PostgreSQL
- Redis
- Docker & Docker Compose
## Setup Instructions

### Prerequisites
- Docker
- Docker Compose

### Start the project

docker-compose up -d

## Access Services

1.Dashboard: http://localhost:3000

2.Backend API: http://localhost:8000

3.Health Check: http://localhost:8000/health

All services run inside Docker. No local database or backend setup is required.

## Architecture Overview

The system is composed of multiple Docker containers:

- **API Service (Spring Boot)**
  - Handles orders, payments, merchants, and webhooks
  - Secured using API Key & Secret
  - Async payment processing using background workers

- **Dashboard (React + Nginx)**
  - Displays system health
  - Shows orders and payments
  - Provides checkout flow

- **PostgreSQL**
  - Stores merchants, orders, payments, webhook logs

- **Redis**
  - Used for job queues and payment processing state

All services communicate over a Docker network.

## API Endpoints

### Health
- GET /health

### Orders
- POST /api/v1/orders
- GET /api/v1/orders
- GET /api/v1/orders/{order_id}

### Payments
- POST /api/v1/payments
- GET /api/v1/payments
- GET /api/v1/payments/{payment_id}

### Public Checkout APIs
- GET /api/v1/orders/{order_id}
- POST /api/v1/payments

### Webhooks
- POST /api/v1/webhooks/payment

All private APIs require:
- X-Api-Key
- X-Api-Secret

## Database Schema

### merchants
- id
- name
- email
- api_key
- api_secret
- webhook_url
- created_at

### orders
- id
- merchant_id
- amount
- currency
- status
- receipt
- created_at
- updated_at

### payments
- id
- order_id
- method (upi/card)
- status (created, processing, success, failed)
- amount
- created_at
- updated_at

### webhook_events
- id
- payment_id
- payload
- status
- retry_count
- created_at

Database schema is auto-created by Hibernate inside the PostgreSQL Docker container.
No manual SQL setup is required.

## Payment Flow

1. Merchant creates an order
2. User opens checkout page
3. User selects payment method
4. Payment is processed asynchronously
5. Status is updated via polling
6. Webhook is delivered with retry logic

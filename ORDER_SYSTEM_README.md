# Order System - Magic East Backend

## Overview
Successfully created a complete Order management system following the MVC pattern for your TCG online store.

## Created Files

### 1. **Models (Entities)**
- `Order.kt` - Main order entity
  - Links to `Usuario` (user who placed the order)
  - Contains list of `OrderItem`s
  - Tracks order status, total, creation date, and shipping address
  
- `OrderItem.kt` - Individual product items in an order
  - Links to `Order` and `Producto`
  - Stores quantity, unit price, and subtotal
  - Captures price at time of purchase (important for historical records)

### 2. **Repository**
- `OrderRepository.kt` - Data access layer
  - `findByUsuarioId()` - Get all orders for a specific user
  - `findByEstado()` - Get all orders with a specific status
  - `findByUsuarioIdAndEstado()` - Combined filter

### 3. **DTOs**
- `OrderDTOs.kt` - Request/Response objects
  - `CreateOrderRequest` - For creating new orders
  - `OrderItemRequest` - Product + quantity in order
  - `OrderResponse` - Order details for API responses
  - `OrderItemResponse` - Item details in responses

### 4. **Service**
- `OrderService.kt` - Business logic layer
  - `crearOrden()` - Creates order, validates stock, reduces inventory
  - `obtenerOrden()` - Get order by ID
  - `listarTodasLasOrdenes()` - Get all orders
  - `listarOrdenesPorUsuario()` - Get user's orders
  - `listarOrdenesPorEstado()` - Filter by status
  - `actualizarEstado()` - Update order status
  - `cancelarOrden()` - Cancel an order

### 5. **Controller**
- `OrderController.kt` - REST API endpoints
  - `POST /api/ordenes` - Create new order
  - `GET /api/ordenes/{id}` - Get order by ID
  - `GET /api/ordenes` - List all orders
  - `GET /api/ordenes/usuario/{usuarioId}` - List user's orders
  - `GET /api/ordenes/estado/{estado}` - Filter by status
  - `PATCH /api/ordenes/{id}/estado` - Update order status
  - `POST /api/ordenes/{id}/cancelar` - Cancel order

## Database Schema

### ORDERS Table
```
ID (PK)
USUARIO_ID (FK -> Usuarios)
FECHA_CREACION
TOTAL
ESTADO
DIRECCION_ENVIO
```

### ORDER_ITEMS Table
```
ID (PK)
ORDER_ID (FK -> ORDERS)
PRODUCTO_ID (FK -> PRODUCTO)
CANTIDAD
PRECIO_UNITARIO
SUBTOTAL
```

## Order States
- `PENDIENTE` - Order created, awaiting confirmation
- `CONFIRMADA` - Order confirmed
- `ENVIADA` - Order shipped
- `ENTREGADA` - Order delivered
- `CANCELADA` - Order cancelled

## Key Features

### ✅ Stock Management
- Automatically validates stock availability before creating order
- Reduces product stock when order is created
- Prevents overselling

### ✅ Price Tracking
- Captures product price at time of purchase
- Maintains historical pricing data
- Protects against price changes affecting past orders

### ✅ Order Validation
- Validates user exists
- Validates all products exist
- Validates quantities are positive
- Validates sufficient stock

### ✅ Automatic Calculations
- Calculates item subtotals (quantity × price)
- Calculates order total (sum of all subtotals)

### ✅ Complete API Documentation
- All endpoints documented with Swagger/OpenAPI
- Available at: http://localhost:8080/swagger-ui.html

## Example Usage

### Creating an Order
```json
POST /api/ordenes
{
  "usuarioId": 1,
  "direccionEnvio": "Av. Siempre Viva 742, Valparaíso",
  "items": [
    {
      "productoId": 5,
      "cantidad": 2
    },
    {
      "productoId": 8,
      "cantidad": 1
    }
  ]
}
```

### Response
```json
{
  "id": 1,
  "usuarioId": 1,
  "usuarioNombre": "Agustín Morales",
  "items": [
    {
      "id": 1,
      "productoId": 5,
      "productoNombre": "Mazo Intro Azul",
      "cantidad": 2,
      "precioUnitario": 25990,
      "subtotal": 51980
    },
    {
      "id": 2,
      "productoId": 8,
      "productoNombre": "Booster Pack",
      "cantidad": 1,
      "precioUnitario": 8990,
      "subtotal": 8990
    }
  ],
  "fechaCreacion": "2025-12-14T22:47:00",
  "total": 60970,
  "estado": "PENDIENTE",
  "direccionEnvio": "Av. Siempre Viva 742, Valparaíso"
}
```

## Testing

### Access Swagger UI
Visit: http://localhost:8080/swagger-ui.html

Look for the "Órdenes" section to test all order endpoints.

### Test Flow
1. Create a user (if not exists)
2. Create products (if not exists)
3. Create an order with the user ID and product IDs
4. View the order
5. Update order status (PENDIENTE → CONFIRMADA → ENVIADA → ENTREGADA)
6. View user's order history

## Notes

- The system uses JPA relationships (`@ManyToOne`, `@OneToMany`)
- Cascade operations ensure order items are saved/deleted with orders
- Transactions ensure data consistency
- Stock is reduced atomically during order creation
- All endpoints follow REST conventions
- Full Swagger documentation included

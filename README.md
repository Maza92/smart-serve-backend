# Smart Serve Backend - Restaurant Management System

## 📋 Overview

Smart Serve Backend is a comprehensive restaurant management system built with **Spring Boot 3** and **Java 21**. It provides a complete point-of-sale (POS) solution with integrated inventory management, real-time kitchen operations, financial reporting, and staff performance analytics.

## 🚀 Key Features

### 🏪 Point of Sale (POS)

- **Complete order management** with table assignment and status tracking
- **Real-time kitchen integration** with WebSocket updates
- **Order workflow** from draft to completion with state validation

### 💰 Cash Register Management

- **Session-based cash control** with open/close operations
- **Movement tracking** for all cash transactions
- **Single active register** validation to prevent conflicts
- **Comprehensive audit trail** for financial reconciliation

### 📦 Advanced Inventory System

- **Recipe-based stock management** with automatic deduction
- **Unit conversion system** for different measurement scales
- **Real-time dashboard** with caching and event-driven updates
- **Movement tracking** for purchases, usage, adjustments, and waste
- **Low stock alerts** and expiration date monitoring

### 👨‍🍳 Kitchen Operations

- **Real-time order display** with WebSocket integration
- **Status progression** (Sent → In Preparation → Ready → Served)
- **Cook assignment** and preparation time tracking
- **Automatic inventory updates** when orders are marked ready

### 📊 Business Intelligence & Reports

- **Sales analytics** with temporal trends and performance metrics
- **Staff performance tracking** with revenue and efficiency metrics
- **Product analysis** with profitability and popularity insights
- **Financial reporting** with payment method distribution
- **Real-time dashboards** with cached data and live updates

### 🔐 Security & Access Control

- **JWT-based authentication** integration with AuthService microservice
- **Role-based access control** (Admin, Cashier, Waiter, Cook, Baker)
- **Endpoint protection** with granular permissions
- **Multi-language support** (English/Spanish) with i18n

### 🔄 Real-time Features

- **WebSocket integration** for kitchen updates (`/topic/kitchen/order-updates`)
- **Dashboard live updates** (`/topic/dashboard/inventory`)
- **Notification system** for staff alerts and updates

## 🏗️ Architecture

### Project Structure

```
src/main/java/com/example/demo/
├── config/              # Spring configurations & WebSocket setup
├── controller/          # REST API endpoints by domain
├── dto/                 # Data Transfer Objects with validation
├── entity/              # JPA entities with relationships
├── enums/               # Business logic enumerations
├── exception/           # Custom exceptions with global handler
├── handler/             # Global exception and event handlers
├── repository/          # JPA repositories with custom queries
├── security/            # Security configuration & JWT integration
├── service/             # Business logic implementation
├── specification/       # Complex filtering with JPA Criteria API
├── mappers/             # Entity-DTO mapping with MapStruct
└── utils/               # Utility classes and helpers
```

### Database Design

The system uses a sophisticated PostgreSQL schema with key entities:

- **Users & Roles**: Authentication and authorization
- **Orders & Order Details**: Complete order lifecycle management
- **Dishes & Recipes**: Menu items with ingredient relationships
- **Inventory Items & Movements**: Stock control with audit trail
- **Cash Registers & Movements**: Financial transaction tracking
- **Restaurant Tables**: Table management and status control
- **Transactions**: Payment processing and reconciliation

## 🛠️ Technology Stack

### Core Framework

- **Spring Boot 3.x** - Main application framework
- **Spring Security 6** - Authentication and authorization
- **Spring Data JPA** - Database access and ORM
- **Spring WebSocket** - Real-time bidirectional communication

### Database & Persistence

- **PostgreSQL** - Primary database with complex relationships
- **HikariCP** - High-performance connection pooling
- **Hibernate** - JPA implementation with custom queries

### Integration & Communication

- **JWT Authentication** - Stateless security with AuthService integration
- **REST APIs** - RESTful endpoints with OpenAPI documentation
- **WebSocket (STOMP)** - Real-time updates for kitchen and dashboards

### Development & Documentation

- **SpringDoc OpenAPI 3** - Automatic API documentation generation
- **Swagger UI** - Interactive API testing interface
- **Spring Boot Actuator** - Application monitoring and health checks

### Business Logic Features

- **JPA Specifications** - Dynamic complex filtering
- **Custom Exceptions** - Centralized error handling with i18n
- **Event-Driven Updates** - Real-time cache invalidation
- **Caching Strategy** - Performance optimization for dashboards
- **Validation Framework** - Input validation with custom validators

## 🔐 Security & Roles

### Authentication Flow

1. **Frontend** authenticates with **AuthService** microservice
2. **JWT tokens** obtained from AuthService are validated by main backend
3. **Role-based access** control enforced on all endpoints
4. **Security headers** and CORS configuration for web clients

### Role Permissions

| Role        | Permissions                                            |
| ----------- | ------------------------------------------------------ |
| **ADMIN**   | Full system access, user management, financial reports |
| **CASHIER** | Cash register operations, payments, order completion   |
| **WAITER**  | Order creation, table management, customer service     |
| **COOK**    | Kitchen operations, order preparation, inventory usage |
| **BAKER**   | Specialized cooking operations, recipe management      |
| **USER**    | Basic access, limited functionality                    |

## 📋 Main API Endpoints

### 🍽️ Order Management

```http
POST   /api/v1/order/draft                    # Create draft order
PUT    /api/v1/order/{id}/send-to-kitchen     # Send order to kitchen
PUT    /api/v1/order/{id}/claim-to-cook       # Claim order for preparation
PUT    /api/v1/order/{id}/mark-as-ready       # Mark order ready to serve
PUT    /api/v1/order/{id}/serve               # Serve order to customer
GET    /api/v1/order/{id}/account             # Get order invoice
PUT    /api/v1/order/{id}/pay                 # Process payment
GET    /api/v1/order/kitchen                  # Get kitchen dashboard orders
```

### 💰 Cash Register Operations

```http
POST   /api/v1/cash/create                    # Create new cash register
PUT    /api/v1/cash/open/{id}                 # Open register with initial amount
PUT    /api/v1/cash/close/{id}                # Close register with final count
GET    /api/v1/cash/status                    # Get current register status
GET    /api/v1/cash/current                   # Get active register details
POST   /api/v1/cash-movements                 # Record cash movement
```

### 📦 Inventory Management

```http
GET    /api/v1/inventory/dashboard            # Get inventory dashboard
POST   /api/v1/inventory/refresh              # Refresh dashboard cache
POST   /api/v1/inventory/update-stock         # Manual stock adjustment
POST   /api/v1/inventory/update-stocks-batch  # Batch stock updates
GET    /api/v1/inventory-items                # Get inventory items
POST   /api/v1/inventory-items                # Create inventory item
```

### 📊 Reports & Analytics

```http
GET    /api/v1/report/dashboard               # Business dashboard data
GET    /api/v1/report/sales-summary           # Sales performance summary
GET    /api/v1/report/waiter-performance-report # Staff performance analytics
GET    /api/v1/report/product-sales-report    # Product performance analysis
GET    /api/v1/report/payment-method-distribution # Payment analytics
```

### 🍕 Menu & Recipe Management

```http
GET    /api/v1/dishes                         # Get menu items
POST   /api/v1/dishes                         # Create dish with recipe
GET    /api/v1/dishes/{id}/ingredients        # Get dish ingredients
GET    /api/v1/recipe                         # Get recipes
POST   /api/v1/recipe                         # Create recipe entry
```

## 🔄 Business Workflows

### Order Lifecycle

```
Table Selection → Draft Order → Add Items → Send to Kitchen
     ↓
Kitchen Claim → In Preparation → Mark Ready → Serve to Customer
     ↓
Generate Invoice → Process Payment → Table Cleanup → Complete
```

### Cash Register Session

```
Create Register → Open with Initial Amount → Process Transactions
     ↓
Record Movements → Close with Final Count → Generate Report → Archive
```

### Inventory Flow

```
Purchase Items → Update Stock → Create Recipes → Process Orders
     ↓
Auto Deduction → Monitor Levels → Reorder Alerts → Audit Trail
```

## 🌐 Real-time Features

### WebSocket Endpoints

- **Kitchen Updates**: `/topic/kitchen/order-updates`

  - Real-time order status changes
  - New orders sent to kitchen
  - Preparation progress updates

- **Inventory Dashboard**: `/topic/dashboard/inventory`

  - Stock level changes
  - Recent activity updates
  - Metric recalculations

### Event-Driven Updates

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handleInventoryChangedEvent(InventoryChangedEvent event) {
    cacheService.clearInventoryDashboardCache();
    DashboardUpdateDto update = DashboardUpdateDto.builder()
        .updateType(event.getUpdateType())
        .data(event.getData())
        .timestamp(Instant.now())
        .build();
    broadcastUpdate(update);
}
```

## 📊 Performance & Caching

### Caching Strategy

- **Dashboard Data**: Cached with event-driven invalidation
- **Inventory Metrics**: Real-time calculation with cache optimization
- **Recent Activities**: Cached with automatic refresh
- **Cache Invalidation**: Event-based clearing for data consistency

### Performance Features

- **JPA Specifications** for efficient complex queries
- **Async Processing** for non-blocking operations
- **Batch Operations** for bulk inventory updates

## 🛠️ Installation & Setup

### Prerequisites

- **Java 21** or higher
- **PostgreSQL 12+** database server
- **AuthService** microservice running
- **Gradle 8+** for building

### Environment Configuration

```env
# Database Configuration
POSTGRES_HOST=localhost
POSTGRES_DB=smartserve
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password

```

### Database Setup

```sql
CREATE DATABASE smartserve;
CREATE USER smartserve_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE smartserve TO smartserve_user;
```

### Running the Application

```bash
# Clone the repository
git clone https://github.com/your-username/smart-serve-backend.git
cd smart-serve-backend

# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Or run with Docker
docker-compose up -d
```

### API Documentation

Once running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api/v1/v3/api-docs

## 🤝 Integration

### AuthService Integration

The backend integrates seamlessly with the AuthService microservice:

- **JWT Token Validation**: Stateless authentication with shared secret
- **User Information**: Role and permission synchronization
- **Session Management**: Coordinated with auth service sessions

### Frontend Integration

Designed to work with modern frontend frameworks:

- **RESTful APIs**: Standard HTTP methods and status codes
- **WebSocket Support**: Real-time updates for dynamic UIs
- **CORS Configuration**: Cross-origin request support
- **API Documentation**: OpenAPI 3.0 specification for code generation

## 📚 Additional Resources

- **API Documentation**: Available at `/swagger-ui.html` when running
- **AuthService**: Separate microservice for authentication
- **Frontend Repository**: [Angular System](https:///github.com/Maza92/smart-serve)

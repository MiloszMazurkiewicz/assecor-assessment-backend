# Assecor Assessment Backend

##  Overview

REST API for managing persons and their favorite colors

##  Key Features

- **RESTful API** with complete CRUD operations for persons and colors
- **Database Persistence** using H2 with JPA/Hibernate
- **Normalized Database Design** with separate colors table
- **Real-time Metrics Dashboard** with endpoint latency tracking
- **Comprehensive Monitoring** using Spring Boot Actuator and Micrometer
- **Dependency Injection** with Spring's IoC container
- **Comprehensive Testing** with JUnit 5
- **API Documentation** with Swagger/OpenAPI
- **Modern UI** with responsive metrics dashboard

##  Technology Stack

- **Java 25**
- **Spring Boot 3.5.0**
- **H2 Database** (in-memory)
- **JUnit 5** for testing
- **Micrometer** for metrics collection
- **SpringDoc OpenAPI** for API documentation
- **Lombok** for boilerplate reduction


##  API Endpoints

http://localhost:8080/swagger-ui/index.html

## 📈 Metrics & Monitoring

### Real-time Metrics Dashboard
Access the comprehensive metrics dashboard at:
```
http://localhost:8080/metrics-dashboard.html
```

### Actuator Endpoints

- **Health Check**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`

##  Getting Started

### Prerequisites

- **Java 25** or higher
- **Maven 3.9** or higher


### Running the Application:
   ```bash
   mvn spring-boot:run
   ```

### Running the Tests

```bash
mvn test
```

##  Acceptance Criteria Fulfillment

✅ **REST Interface**: All required endpoints implemented  
✅ **Dependency Injection**: Spring handles all dependencies  
✅ **Unit Tests**: Comprehensive test coverage  
✅ **Database Integration**: JPA with H2 database  
✅ **Metrics Monitoring**: Real-time performance tracking  
✅ **API Documentation**: Swagger/OpenAPI integration  
✅ **Error Handling**: Global exception management  
✅ **Data Validation**: Input validation with Bean Validation  

##  Bonus Features Implemented

✅ **Database Normalization**: Separate colors table 
✅ **CRUD Operations**: Complete person and color management 
✅ **Metrics Dashboard**: Real-time monitoring UI 
✅ **Latency Tracking**: Endpoint performance monitoring 
✅ **API Documentation**: Swagger UI integration 
✅ **Modern UI**: Responsive metrics dashboard 
✅ **Comprehensive Logging**: Structured logging with SLF4J 

## Future Enhancements

- Caching
- OAuth2 Security
- Integration with company's metrics system + dashboard

## 📝 License

This project is part of the Assecor assessment and is for evaluation purposes only.


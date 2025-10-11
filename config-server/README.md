# **Config Server**

A centralized configuration server for the microservices system.

## Current Status

- [x] **Module Initialized**: `config-server` has been created as a Spring Boot module.
- [x] **Dependencies Installed**: Added `spring-cloud-config-server` and `eureka-client`.
- [x] **Feature Activated**: Enabled with `@EnableConfigServer`.
- [ ] **Connect to Git Repository**: Not yet configured to point to a Git repository.
- [ ] **Client Integration**: Other microservices have not been configured to use this server.

## Next Steps

### 1\. Configure the Server

Update the `application.properties` file in `config-server`:

```properties
server.port=8888
spring.cloud.config.server.git.uri=https://github.com/your-username/your-config-repo.git
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

### 2\. Configure the Clients (Other services)

For each microservice (e.g., `order-service`):

- **Add Dependency** to `pom.xml`:

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
  </dependency>
  ```

- **Create `bootstrap.properties`** file in `src/main/resources`:

  ```properties
  spring.application.name=order-service
  spring.cloud.config.uri=http://localhost:8888
  ```

- **Clean up** the local `application.properties` file.
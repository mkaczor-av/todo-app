## Simple TODO manager app 


### Stack:
- PostgrSQL
- Spring Boot 3 (JDK 21)
- Angular 17

### Requirements:
- docker, docker-compose
- JDK 21
- nodeJS, Angular CLI

### Running the app
Run the database
```
cd todo-app-devops
docker-compose up -d
```

Run the backend
```
cd todo-app-backend
./gradlew build
./gradlew bootRun
```

Run the frontend:
```
cd todo-app-frontend
npm install
ng serve
```

---
Backend is running on <http://localhost:8080> \
The API definition can be viewed at <http://localhost:8080/swagger-ui/index.html> 

Frontend is running on <http://localhost:4200>

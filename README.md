# Language Learning Application

## How to Build and Run

### Frontend App
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on: http://localhost:5173

### Backend App
```bash
cd project
mvn spring-boot:run
```
Backend runs on: http://localhost:8080

## Database Console

**H2 Console URL:** http://localhost:8080/h2-console

**Connection Settings:**
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:project`
- Username: `sa`
- Password: *(leave empty)*

## User Login Credentials

| Name | Email | Password | Role |
|------|-------|----------|------|
| Sarah Johnson | sarah.johnson@example.com | password123 | Teacher |
| Michael Brown | michael.brown@example.com | password456 | Teacher |
| Emma Davis | emma.davis@example.com | password789 | Teacher & Student |
| James Wilson | james.wilson@example.com | password321 | Student |
| Olivia Miller | olivia.miller@example.com | password654 | Student |
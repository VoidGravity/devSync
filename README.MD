Data flow:
HTTP Request -> Servlet -> Service -> DAO -> Database-> DAO -> Service -> Servlet -> HTTP Response


Trello link : https://trello.com/b/vLKKodCh
# DevSync - Task Management Application

## Project Overview

DevSync is an innovative task management platform developed to address the limitations of existing task management tools. Built with Jakarta EE, this robust application aims to streamline task management for individuals, team leaders, and managers in dynamic work environments.

### Version 1.0.0 Features
- User CRUD operations




## Technologies Used

- Jakarta EE
- Hibernate
- JPA (Java Persistence API)
- Servlets
- JSP (JavaServer Pages)
- Maven
- Tomcat (Application Server)
- PostgreSQL (Database)

## Project Setup

### Prerequisites
- JDK 11 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Tomcat 9 or higher

### Database Setup
1. Create a PostgreSQL database named `devsync_db`.
2. Run the SQL scripts located in `src/main/resources/sql/` to create and initialize the database schema.

### Application Setup
1. Clone the repository:
   ```
   git clone https://github.com/your-username/devsync.git
   ```
2. Navigate to the project directory:
   ```
   cd devsync
   ```
3. Build the project:
   ```
   mvn clean install
   ```
4. Deploy the generated WAR file to your Tomcat server.

## Usage

After deployment, access the application at `http://localhost:8080/devsync`.

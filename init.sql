CREATE TYPE role AS ENUM ( 'NOMRMAL_USER', 'MANAGER');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role role NOT NULL DEFAULT 'EMPLOYEE'
);

INSERT INTO users (username, password, first_name, last_name, email, role)
VALUES
    ('MANAGER1', 'MANAGER', 'John', 'Doe', 'john.doe@example.com', 'MANAGER'),
    ('MANAGER2', 'MANAGER', 'Jane', 'Smith', 'jane.smith@example.com', 'NOMRMAL_USER');


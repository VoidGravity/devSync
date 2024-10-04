CREATE TYPE manager_role AS ENUM ('EMPLOYEE', 'TEAM_LEAD', 'MANAGER', 'DIRECTOR');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       email VARCHAR(100) UNIQUE NOT NULL,
                       manager_role manager_role NOT NULL DEFAULT 'EMPLOYEE'
);

INSERT INTO users (username, password, first_name, last_name, email, manager_role)
VALUES
    ('john_doe', 'hashed_password', 'John', 'Doe', 'john.doe@example.com', 'EMPLOYEE'),
    ('jane_smith', 'hashed_password', 'Jane', 'Smith', 'jane.smith@example.com', 'MANAGER');

-- UPDATE users
-- SET manager_role = 'TEAM_LEAD'
-- WHERE username = 'john_doe';
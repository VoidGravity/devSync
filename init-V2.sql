CREATE TYPE role AS ENUM ('NORMAL_USER', 'MANAGER');
CREATE TYPE task_status AS ENUM ('Pending', 'In Progress', 'Completed', 'Not Completed');
CREATE TYPE change_request_status AS ENUM ('Pending', 'Approved', 'Rejected');
CREATE TYPE token_type AS ENUM ('Modification', 'Deletion');

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       email VARCHAR(100) UNIQUE NOT NULL,
                       role role NOT NULL DEFAULT 'NORMAL_USER',
                       modification_tokens INT DEFAULT 2,
                       deletion_tokens INT DEFAULT 1,
                       last_token_reset_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
                       task_id SERIAL PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       description TEXT,
                       due_date TIMESTAMP NOT NULL,
                       created_by INT NOT NULL,
                       assigned_to INT NOT NULL,
                       creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       status task_status DEFAULT 'Pending',
                       completed_at TIMESTAMP,
                       is_manager_assigned BOOLEAN DEFAULT FALSE,
                       last_status_update TIMESTAMP,
                       original_assigned_to INT,
                       FOREIGN KEY (created_by) REFERENCES users(id),
                       FOREIGN KEY (assigned_to) REFERENCES users(id),
                       FOREIGN KEY (original_assigned_to) REFERENCES users(id)
);

CREATE TABLE task_tags (
                           task_id INT,
                           tag VARCHAR(50),
                           PRIMARY KEY (task_id, tag),
                           FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);

CREATE TABLE token_usage_history (
                                     id SERIAL PRIMARY KEY,
                                     user_id INT NOT NULL,
                                     task_id INT NOT NULL,
                                     token_type token_type NOT NULL,
                                     used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(id),
                                     FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);

CREATE TABLE change_requests (
                                 id SERIAL PRIMARY KEY,
                                 user_id INT NOT NULL,
                                 task_id INT NOT NULL,
                                 requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 resolved_at TIMESTAMP,
                                 status change_request_status DEFAULT 'Pending',
                                 FOREIGN KEY (user_id) REFERENCES users(id),
                                 FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);



INSERT INTO users (username, password, first_name, last_name, email, role)
VALUES
    ('manager1', 'hashedpassword1', 'John', 'Doe', 'john.doe@example.com', 'MANAGER'),
    ('user1', 'hashedpassword2', 'Jane', 'Smith', 'jane.smith@example.com', 'NORMAL_USER'),
    ('user2', 'hashedpassword3', 'Bob', 'Johnson', 'bob.johnson@example.com', 'NORMAL_USER');

INSERT INTO tasks (title, description, due_date, created_by, assigned_to, creation_date, status, original_assigned_to)
VALUES
    ('Complete Project Proposal', 'Draft and finalize the project proposal for the new client',
     '2024-10-31 17:00:00', 1, 2, CURRENT_TIMESTAMP, 'In Progress', 2);

INSERT INTO task_tags (task_id, tag)
VALUES
    (1, 'Project'),
    (1, 'Proposal'),
    (1, 'Client');
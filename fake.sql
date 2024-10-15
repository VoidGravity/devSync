INSERT INTO users (username, password, first_name, last_name, email, role, modification_tokens, deletion_tokens, last_token_reset)
VALUES
    ('MANAGER', 'MANAGER', 'John', 'Doe', 'john.doe@example.com', 'MANAGER', 2, 1, CURRENT_TIMESTAMP),
    ('NORMAL', 'NORMAL', 'Jane', 'Smith', 'jane.smith@example.com', 'NORMAL_USER', 2, 1, CURRENT_TIMESTAMP);

INSERT INTO tags (name)
VALUES
    ('Urgent'),
    ('Important'),
    ('Low Priority'),
    ('Bug'),
    ('Feature'),
    ('Documentation')
    ON CONFLICT (name) DO NOTHING;
INSERT INTO tasks (title, description, due_date, created_by, assigned_to, creation_date, completed, modifiable, replacedByManager)
VALUES
    ('Implement login system', 'Create a secure login system for the application', CURRENT_DATE + INTERVAL '7 days', 1, 2, CURRENT_TIMESTAMP, false, true, false),
    ('Fix homepage layout', 'Adjust the CSS for better responsive design', CURRENT_DATE + INTERVAL '3 days', 2, 2, CURRENT_TIMESTAMP, false, true, false),
    ('Write user documentation', 'Create a comprehensive user guide', CURRENT_DATE + INTERVAL '14 days', 1, 1, CURRENT_TIMESTAMP, false, true, false),
    ('Optimize database queries', 'Improve performance of slow queries', CURRENT_DATE + INTERVAL '10 days', 1, 2, CURRENT_TIMESTAMP, false, true, false);
INSERT INTO task_tags (task_id, tag_id)
SELECT t.task_id, tg.id
FROM
    (VALUES
         (1, 'Urgent'), (1, 'Feature'),
         (2, 'Low Priority'), (2, 'Bug'),
         (3, 'Low Priority'), (3, 'Documentation'),
         (4, 'Important'), (4, 'Feature')
    ) AS t(task_id, tag_name)
        JOIN tasks ON tasks.id = t.task_id
        JOIN tags tg ON tg.name = t.tag_name;
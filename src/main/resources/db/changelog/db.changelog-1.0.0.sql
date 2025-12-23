-- changeset aleksLiss:1
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(30) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);

-- changeset aleksLiss:2
CREATE TABLE IF NOT EXISTS locations (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(83) NOT NULL,
                           user_id INT REFERENCES users(id),
                           latitude DECIMAL(9,6) NOT NULL,
                           longitude DECIMAL(9,6) NOT NULL
);

-- changeset aleksLiss:3
CREATE TABLE IF NOT EXISTS sessions (
                          id UUID PRIMARY KEY,
                          user_id INT REFERENCES users(id),
                          expires_at TIMESTAMP NOT NULL
);
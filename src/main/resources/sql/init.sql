CREATE TABLE IF NOT EXISTS users (
                                     id INT PRIMARY KEY,
                                     login VARCHAR(30) NOT NULL UNIQUE,
                                     password VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
                                         id INT PRIMARY KEY,
                                         name VARCHAR(83) NOT NULL,
                                         user_id INT REFERENCES users(id),
                                         latitude DECIMAL(9,6) NOT NULL,
                                         longitude DECIMAL(9,6) NOT NULL,
                                         unique (user_id, latitude, longitude)
);

CREATE TABLE IF NOT EXISTS sessions (
                                        id UUID PRIMARY KEY,
                                        user_id INT REFERENCES users(id),
                                        expires_at TIMESTAMP NOT NULL
);
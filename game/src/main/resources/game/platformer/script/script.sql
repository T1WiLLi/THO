-- Script for database integration
DROP SCHEMA IF EXISTS tho CASCADE;

CREATE SCHEMA tho;

SET search_path TO tho;

-- Hold users
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL 
);

-- Hold different levels
CREATE TABLE level (
    id SERIAL PRIMARY KEY,
    level_name VARCHAR NOT NULL
);

-- Hold user timestamp for different levels
CREATE TABLE score (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    level_id INT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (level_id) REFERENCES level(id)
);

-- Trigger
CREATE OR REPLACE FUNCTION update_score()
RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM score
    WHERE user_id = NEW.user_id
    AND level_id = NEW.level_id
    AND NEW.timestamp < timestamp;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_insert_score
BEFORE INSERT ON score
FOR EACH ROW EXECUTE FUNCTION update_score();

-- Insert a test user into the app_user table
INSERT INTO app_user (username, password) VALUES ('test', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'); -- Password is 123, this is an HASH of it using SHA-256
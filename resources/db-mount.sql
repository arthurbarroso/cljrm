-- Drop tables
drop table if exists users;

-- Create schema
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    email VARCHAR ( 50 ) UNIQUE NOT NULL,
    password VARCHAR ( 50 ) NOT NULL ,
    created_at TIMESTAMP NOT NULL
);

CREATE table jobs(
    id SERIAL PRIMARY KEY,
    title VARCHAR( 60 ) NOT NULL,
    description VARCHAR ( 400 ) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    posted_by INT NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(posted_by)
            REFERENCES users(id)
);
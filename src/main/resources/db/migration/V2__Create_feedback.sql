CREATE TABLE feedback (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          email VARCHAR(1000) NOT NULL,
                          comment VARCHAR(1000) NOT NULL,
                          created_at TIMESTAMP NOT NULL
);

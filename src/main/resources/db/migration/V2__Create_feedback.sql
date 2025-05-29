CREATE TABLE feedback (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                          comment VARCHAR(1000) NOT NULL,
                          created_at TIMESTAMP NOT NULL
);

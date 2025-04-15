CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE tags (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE news (
                      id BIGSERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      content TEXT NOT NULL,
                      news_type VARCHAR(20) NOT NULL,
                      image_url VARCHAR(255),
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP NOT NULL
);

-- Связь новостей и авторов
CREATE TABLE news_authors (
                              news_id BIGINT REFERENCES news(id),
                              author_id BIGINT REFERENCES authors(id),
                              PRIMARY KEY (news_id, author_id)
);

-- Связь новостей и тегов
CREATE TABLE news_tags (
                           news_id BIGINT REFERENCES news(id),
                           tag_id BIGINT REFERENCES tags(id),
                           PRIMARY KEY (news_id, tag_id)
);
CREATE TABLE IF NOT EXISTS members (
    id          BIGINT NOT NULL,
    name        TEXT NOT NULL,
    disabled    BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS posts (
    id          BIGINT NOT NULL,
    content     TEXT NOT NULL,
    member_id   BIGINT NOT NULL,
    ts          TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES members (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS comments (
    id          BIGINT NOT NULL,
    content     TEXT NOT NULL,
    post_id     BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    ts          TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id),
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_comment_member FOREIGN KEY (member_id) REFERENCES members (id) ON UPDATE CASCADE ON DELETE RESTRICT
);

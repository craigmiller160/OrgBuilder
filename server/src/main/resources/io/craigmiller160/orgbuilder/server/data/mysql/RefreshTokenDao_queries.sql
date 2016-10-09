-- The queries used by the RefreshTokenDao

-- QUERY=INSERT
INSERT INTO tokens (token_id, user_id, token_hash, expiration)
VALUES (?,?,?,?);

-- QUERY=UPDATE
UPDATE tokens
SET token_id = ?, user_id = ?, token_hash = ?, expiration = ?
WHERE token_id = ?;

-- QUERY=DELETE
DELETE FROM tokens
WHERE token_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM tokens
WHERE token_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS token_count
FROM tokens;

-- QUERY=GET_ALL
SELECT *
FROM tokens
ORDER BY token_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM tokens
ORDER BY token_id ASC
LIMIT ?,?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO tokens (token_id, user_id, token_hash, expiration)
VALUES (?,?,?,?)
ON DUPLICATE KEY UPDATE user_id = VALUES (user_id), token_hash = VALUES (token_hash),
  expiration = VALUES (expiration);

-- QUERY=GET_WITH_HASH
SELECT *
FROM tokens
WHERE token_hash = ?;
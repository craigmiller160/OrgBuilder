-- The queries used by the RefreshTokenDao

-- QUERY=INSERT
INSERT INTO tokens (token_id, user_id, org_id, token_hash, expiration)
VALUES (?,?,?,?,?);

-- QUERY=UPDATE
UPDATE tokens
SET token_id = ?, user_id = ?, org_id = ?, token_hash = ?, expiration = ?
WHERE token_id = ?;

-- QUERY=DELETE
DELETE FROM tokens
WHERE token_id = ?;

-- QUERY=GET_BY_ID
SELECT t.*, u.user_email, o.org_name
FROM tokens t
  LEFT JOIN old.users u ON t.user_id = u.user_id
  LEFT JOIN orgs o ON t.org_id = o.org_id
WHERE t.token_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS token_count
FROM tokens;

-- QUERY=GET_ALL
SELECT t.*, u.user_email, o.org_name
FROM tokens t
  LEFT JOIN old.users u ON t.user_id = u.user_id
  LEFT JOIN orgs o ON t.org_id = o.org_id
ORDER BY token_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT t.*, u.user_email, o.org_name
FROM tokens t
  LEFT JOIN old.users u ON t.user_id = u.user_id
  LEFT JOIN orgs o ON t.org_id = o.org_id
ORDER BY token_id ASC
LIMIT ?,?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO tokens (token_id, user_id, org_id, token_hash, expiration)
VALUES (?,?,?,?,?)
ON DUPLICATE KEY UPDATE user_id = VALUES (user_id), token_hash = VALUES (token_hash),
  expiration = VALUES (expiration);

-- QUERY=GET_WITH_HASH
SELECT t.*, u.user_email, o.org_name
FROM tokens t
  LEFT JOIN old.users u ON t.user_id = u.user_id
  LEFT JOIN orgs o ON t.org_id = o.org_id
WHERE token_hash = ?;

-- QUERY=DELETE_BY_ORG
DELETE FROM tokens
WHERE org_id = ?;

-- QUERY=DELETE_BY_USER
DELETE FROM tokens
WHERE user_id = ?;
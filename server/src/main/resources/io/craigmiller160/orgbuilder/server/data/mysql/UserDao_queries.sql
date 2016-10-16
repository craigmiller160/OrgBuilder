-- The queries used by the UserDao

-- QUERY=INSERT
INSERT INTO users (user_id, user_email, passwd, role, org_id)
VALUES (?,?,?,?,?);

-- QUERY=UPDATE
UPDATE users
SET user_id = ?, user_email = ?, passwd = ?, role = ?, org_id = ?
WHERE user_id = ?;

-- QUERY=DELETE
DELETE FROM users
WHERE user_id = ?;

-- QUERY=GET_BY_ID
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
WHERE u.user_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS user_count
FROM users;

-- QUERY=GET_ALL
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
ORDER BY u.user_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
ORDER BY u.user_id ASC
LIMIT ?,?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO users (user_id, user_email, passwd, role, org_id)
VALUES (?,?,?,?,?)
ON DUPLICATE KEY UPDATE user_email = VALUES (user_email),
  passwd = VALUES (passwd), role = VALUES (role), org_id = VALUES (org_id);

-- QUERY=GET_WITH_NAME
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
WHERE u.user_email = ?;

-- QUERY=GET_BY_ID_AND_ORG
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
WHERE u.user_id = ?
AND u.org_id = ?;

-- QUERY=COUNT_BY_ORG
SELECT COUNT(*) AS user_count
FROM users
WHERE org_id = ?;

-- QUERY=GET_ALL_BY_ORG
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
WHERE u.org_id = ?
ORDER BY u.user_id ASC;

-- QUERY=GET_ALL_LIMIT_BY_ORG
SELECT u.*, o.org_name
FROM users u
  LEFT JOIN orgs o ON u.org_id = o.org_id
WHERE u.org_id = ?
ORDER BY u.user_id ASC
LIMIT ?,?;

-- QUERY=DELETE_BY_ORG
DELETE FROM users
WHERE org_id = ?;
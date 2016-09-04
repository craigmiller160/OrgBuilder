-- The queries used by the UserDao

-- QUERY=INSERT
INSERT INTO users (user_id, user_name, user_email, passwd, role, org_id)
VALUES (?,?,?,?,?,?);

-- QUERY=UPDATE
UPDATE users
SET user_id = ?, user_name = ?, user_email = ?, passwd = ?, role = ?, org_id = ?
WHERE user_id = ?;

-- QUERY=DELETE
DELETE FROM users
WHERE user_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM users
WHERE user_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS user_count
FROM users;

-- QUERY=GET_ALL
SELECT *
FROM users
ORDER BY user_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM users
ORDER BY user_id ASC
LIMIT ?,?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO users (user_id, user_name, user_email, passwd, role, org_id)
VALUES (?,?,?,?,?,?)
ON DUPLICATE KEY UPDATE user_name = VALUES (user_name), user_email = VALUES (user_email),
  passwd = VALUES (passwd), role = VALUES (role), org_id = VALUES (org_id);
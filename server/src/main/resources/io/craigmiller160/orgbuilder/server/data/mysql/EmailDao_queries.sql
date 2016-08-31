-- The queries used by the EmailDao

-- QUERY=INSERT
INSERT INTO emails (email_id, email_type, email_address, preferred_email, email_member_id)
VALUES (?,?,?,?,?);

-- QUERY=UPDATE
UPDATE emails
SET email_id = ?, email_type = ?, email_address = ?, preferred_email = ?, email_member_id = ?
WHERE email_id = ?;

-- QUERY=DELETE
DELETE FROM emails
WHERE email_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM emails
WHERE email_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS email_count
FROM emails;

-- QUERY=GET_ALL
SELECT *
FROM emails
ORDER BY email_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM emails
ORDER BY email_id ASC
LIMIT ?,?;

-- QUERY=GET_ALL_BY_MEMBER
SELECT *
FROM emails
WHERE email_member_id = ?
ORDER BY email_id ASC;

-- QUERY=GET_ALL_BY_MEMBER_LIMIT
SELECT *
FROM emails
WHERE email_member_id = ?
ORDER BY email_id ASC
LIMIT ?,?;

-- QUERY=COUNT_BY_MEMBER
SELECT COUNT(*) AS email_count_by_member
FROM emails
WHERE email_member_id = ?;

-- QUERY=CLEAR_PREFERRED
UPDATE emails
SET preferred_email = FALSE
WHERE email_member_id = ?
AND email_id <> ?;

-- QUERY=GET_PREFERRED_FOR_MEMBER
SELECT *
FROM emails
WHERE email_member_id = ?
AND preferred_email = TRUE;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO emails (email_id, email_type, email_address, preferred_email, email_member_id)
VALUES (?,?,?,?,?)
ON DUPLICATE KEY UPDATE email_type = VALUES (email_type), email_address = VALUES (email_address),
  preferred_email = VALUES (preferred_email), email_member_id = VALUES (email_member_id);
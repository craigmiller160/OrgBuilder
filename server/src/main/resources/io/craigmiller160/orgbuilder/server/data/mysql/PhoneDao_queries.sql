-- The queries used by the PhoneDao

-- QUERY=INSERT
INSERT INTO phones (phone_id, phone_type, area_code, prefix, line_number, extension, preferred_phone, phone_member_id)
VALUES (?,?,?,?,?,?,?,?);

-- QUERY=UPDATE
UPDATE phones
SET phone_id = ?, phone_type = ?, area_code = ?, prefix = ?, line_number = ?, extension = ?, preferred_phone = ?, phone_member_id = ?
WHERE phone_id = ?;

-- QUERY=DELETE
DELETE FROM phones
WHERE phone_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM phones
WHERE phone_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS phone_count
FROM phones;

-- QUERY=GET_ALL
SELECT *
FROM phones
ORDER BY phone_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM phones
ORDER BY phone_id ASC
LIMIT ?,?;

-- QUERY=GET_ALL_BY_MEMBER
SELECT *
FROM phones
WHERE phone_member_id = ?
ORDER BY phone_id ASC;

-- QUERY=GET_ALL_BY_MEMBER_LIMIT
SELECT *
FROM phones
WHERE phone_member_id = ?
ORDER BY phone_id ASC
LIMIT ?,?;

-- QUERY=COUNT_BY_MEMBER
SELECT COUNT(*) AS phone_by_member_count
FROM phones
WHERE phone_member_id = ?;

-- QUERY=CLEAR_PREFERRED
UPDATE phones
SET preferred_phone = FALSE
WHERE phone_member_id = ?
AND phone_id <> ?;

-- QUERY=GET_PREFERRED_FOR_MEMBER
SELECT *
FROM phones
WHERE phone_member_id = ?
AND preferred_phone = TRUE;

-- QUERY=DELETE_BY_MEMBER
DELETE FROM phones
WHERE phone_member_id=?;

-- QUERY=GET_BY_ID_AND_MEMBER
SELECT *
FROM phones
WHERE phone_id = ?
AND phone_member_id = ?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO phones (phone_id, phone_type, area_code, prefix, line_number, extension, preferred_phone, phone_member_id)
VALUES (?,?,?,?,?,?,?,?)
ON DUPLICATE KEY UPDATE phone_type = VALUES (phone_type), area_code = VALUES (area_code),
  prefix = VALUES (prefix), line_number = VALUES (line_number), extension = VALUES (extension),
  preferred_phone = VALUES (preferred_phone), phone_member_id = VALUES (phone_member_id);
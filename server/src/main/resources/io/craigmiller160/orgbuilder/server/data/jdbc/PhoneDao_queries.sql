-- The queries used by the PhoneDao

-- QUERY=INSERT
INSERT INTO phones (phone_type, area_code, prefix, line_number, extension, preferred_phone, phone_member_id)
VALUES (?,?,?,?,?,?,?);

-- QUERY=UPDATE
UPDATE phones
SET phone_type = ?, area_code = ?, prefix = ?, line_number = ?, extension = ?, preferred_phone = ?, phone_member_id = ?
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
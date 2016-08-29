-- The queries used by the AddressDao

-- QUERY=INSERT
INSERT INTO addresses (address_type, address, unit, city, state, zip_code, preferred_address, address_member_id)
VALUES (?,?,?,?,?,?,?,?);

-- QUERY=UPDATE
UPDATE addresses
SET address_type = ?, address = ?, unit = ?, city = ?, state = ?, zip_code = ?, preferred_address = ?, address_member_id = ?
WHERE address_id = ?;

-- QUERY=DELETE
DELETE FROM addresses
WHERE address_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM addresses
WHERE address_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS address_count
FROM addresses;

-- QUERY=GET_ALL
SELECT *
FROM addresses
ORDER BY address_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM addresses
ORDER BY address_id ASC
LIMIT ?,?;

-- QUERY=GET_ALL_BY_MEMBER
SELECT *
FROM addresses
WHERE address_member_id = ?
ORDER BY address_id ASC;

-- QUERY=GET_ALL_BY_MEMBER_LIMIT
SELECT *
FROM addresses
WHERE address_member_id = ?
ORDER BY address_id ASC
LIMIT ?,?;

-- QUERY=COUNT_BY_MEMBER
SELECT COUNT(*) AS address_by_member_count
FROM addresses
WHERE address_member_id = ?;

-- QUERY=CLEAR_PREFERRED
UPDATE addresses
SET preferred_address = FALSE
WHERE address_member_id = ?
AND address_id <> ?;

-- QUERY=GET_PREFERRED_FOR_MEMBER
SELECT *
FROM addresses
WHERE address_member_id = ?
AND preferred_address = ?;
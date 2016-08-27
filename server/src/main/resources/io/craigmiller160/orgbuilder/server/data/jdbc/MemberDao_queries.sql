-- The queries used by the MemberDao

-- QUERY=INSERT
INSERT INTO members (first_name, middle_name, last_name, date_of_birth, gender)
VALUES (?,?,?,?,?);

-- QUERY=UPDATE
UPDATE members
SET first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, gender = ?
WHERE member_id = ?;

-- QUERY=DELETE
DELETE FROM members
WHERE member_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM members
WHERE member_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS member_count
FROM members;

-- QUERY=GET_ALL
SELECT *
FROM members
ORDER BY member_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM members
ORDER BY member_id ASC
LIMIT ?,?;
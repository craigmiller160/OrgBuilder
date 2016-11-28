-- The queries used by the MemberDao

-- QUERY=INSERT
INSERT INTO members (member_id, first_name, middle_name, last_name, date_of_birth, sex)
VALUES (?,?,?,?,?,?);

-- QUERY=UPDATE
UPDATE members
SET member_id = ?, first_name = ?, middle_name = ?, last_name = ?, date_of_birth = ?, sex = ?
WHERE member_id = ?;

-- QUERY=DELETE
DELETE FROM members
WHERE member_id = ?;

-- QUERY=GET_BY_ID
SELECT m.*, a.*, p.*, e.*
FROM members m
  LEFT JOIN addresses a ON m.member_id = a.address_member_id AND a.preferred_address = TRUE
  LEFT JOIN phones p ON m.member_id = p.phone_member_id AND p.preferred_phone = TRUE
  LEFT JOIN emails e ON m.member_id = e.email_member_id AND e.preferred_email = TRUE
WHERE m.member_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS member_count
FROM members;

-- QUERY=GET_ALL
SELECT m.*, a.*, p.*, e.*
FROM members m
  LEFT JOIN addresses a ON m.member_id = a.address_member_id AND a.preferred_address = TRUE
  LEFT JOIN phones p ON m.member_id = p.phone_member_id AND p.preferred_phone = TRUE
  LEFT JOIN emails e ON m.member_id = e.email_member_id AND e.preferred_email = TRUE
ORDER BY member_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT m.*, a.*, p.*, e.*
FROM members m
  LEFT JOIN addresses a ON m.member_id = a.address_member_id AND a.preferred_address = TRUE
  LEFT JOIN phones p ON m.member_id = p.phone_member_id AND p.preferred_phone = TRUE
  LEFT JOIN emails e ON m.member_id = e.email_member_id AND e.preferred_email = TRUE
ORDER BY member_id ASC
LIMIT ?,?;

-- QUERY=INSERT_OR_UPDATE
INSERT INTO members (member_id, first_name, middle_name, last_name, date_of_birth, sex)
VALUES (?,?,?,?,?,?)
ON DUPLICATE KEY UPDATE first_name = VALUES (first_name), middle_name = VALUES (middle_name),
  last_name = VALUES (last_name), date_of_birth = VALUES (date_of_birth), sex = VALUES (sex);

-- QUERY=SEARCH_BASE
SELECT m.*, a.*, p.*, e.*
FROM members m
  LEFT JOIN addresses a ON m.member_id = a.address_member_id AND a.preferred_address = TRUE
  LEFT JOIN phones p ON m.member_id = p.phone_member_id AND p.preferred_phone = TRUE
  LEFT JOIN emails e ON m.member_id = e.email_member_id AND e.preferred_email = TRUE;
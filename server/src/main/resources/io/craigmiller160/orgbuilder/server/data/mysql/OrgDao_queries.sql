-- The queries used by the OrgDao

-- QUERY=INSERT
INSERT INTO orgs (org_name)
VALUES (?);

-- QUERY=UPDATE
UPDATE orgs
SET org_name = ?
WHERE org_id = ?;

-- QUERY=DELETE
DELETE FROM orgs
WHERE org_id = ?;

-- QUERY=GET_BY_ID
SELECT *
FROM orgs
WHERE org_id = ?;

-- QUERY=COUNT
SELECT COUNT(*) AS org_count
FROM orgs;

-- QUERY=GET_ALL
SELECT *
FROM orgs
ORDER BY org_id ASC;

-- QUERY=GET_ALL_LIMIT
SELECT *
FROM orgs
ORDER BY org_id ASC
LIMIT ?,?;
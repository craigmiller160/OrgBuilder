-- Creates the schema and table layout for the org_app schema, which contains global values that are needed for every operation (orgs, auth, etc)

CREATE TABLE IF NOT EXISTS orgs (
  org_id BIGINT NOT NULL AUTO_INCREMENT,
  org_name VARCHAR(255) NOT NULL,
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  org_schema_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (org_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  user_email VARCHAR(255) NOT NULL UNIQUE,
  passwd VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  org_id BIGINT NOT NULL,
  PRIMARY KEY (user_id),
  FOREIGN KEY (org_id) REFERENCES orgs (org_id)
);

CREATE TABLE IF NOT EXISTS tokens (
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  token_hash VARCHAR(255) NOT NULL UNIQUE,
  expiration TIMESTAMP NOT NULL,
  PRIMARY KEY (token_id),
  FOREIGN KEY (user_id) REFERENCES users (user_id)
);

DROP TRIGGER IF EXISTS orgs_before_schema_name_trigger;

DELIMITER ;;

CREATE TRIGGER orgs_before_schema_name_trigger
BEFORE INSERT ON orgs FOR EACH ROW
  BEGIN
    DECLARE next_index INT;
    SET next_index =
    (SELECT auto_increment
     FROM information_schema.tables
     WHERE table_schema = 'org_app'
           AND table_name = 'orgs');

    IF next_index IS NULL THEN
      SET next_index = 0;
    END IF;

    SET NEW.org_schema_name = CONCAT(next_index, SUBSTRING(NEW.org_name,1,5));
  END ;;
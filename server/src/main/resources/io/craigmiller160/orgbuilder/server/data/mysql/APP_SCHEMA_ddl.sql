-- Creates the schema and table layout for the org_app schema, which contains global values that are needed for every operation (orgs, auth, etc)

CREATE TABLE IF NOT EXISTS orgs (
  org_id BIGINT NOT NULL AUTO_INCREMENT,
  org_name VARCHAR(255) NOT NULL,
  org_description VARCHAR(255),
  created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  org_schema_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (org_id)
);

CREATE TABLE IF NOT EXISTS old.users (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  user_email VARCHAR(255) NOT NULL UNIQUE,
  passwd VARCHAR(255) NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  role VARCHAR(255),
  org_id BIGINT,
  PRIMARY KEY (user_id),
  FOREIGN KEY (org_id) REFERENCES orgs (org_id)
);

CREATE TABLE IF NOT EXISTS tokens (
  token_id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  org_id BIGINT,
  token_hash VARCHAR(255) NOT NULL UNIQUE,
  expiration TIMESTAMP NOT NULL,
  PRIMARY KEY (token_id),
  FOREIGN KEY (user_id) REFERENCES old.users (user_id),
  FOREIGN KEY (org_id) REFERENCES orgs (org_id)
);

DROP TRIGGER IF EXISTS orgs_before_schema_name_trigger;
DROP PROCEDURE IF EXISTS restrict_master_orgid_proc;
DROP PROCEDURE IF EXISTS restrict_master_role_proc;
DROP TRIGGER IF EXISTS users_before_insert_trigger;
DROP TRIGGER IF EXISTS users_before_update_trigger;

DELIMITER ;;

CREATE TRIGGER orgs_before_schema_name_trigger
BEFORE INSERT ON orgs FOR EACH ROW
  BEGIN
    DECLARE next_index INT;
    DECLARE temp_schema_name VARCHAR(50);

    SET next_index =
    (SELECT auto_increment
     FROM information_schema.tables
     WHERE table_schema = DATABASE()
           AND table_name = 'orgs');

    IF next_index IS NULL THEN
      SET next_index = 0;
    END IF;

    SET temp_schema_name = CONCAT(next_index, SUBSTRING(NEW.org_name,1,5));
    SET NEW.org_schema_name = REPLACE(temp_schema_name, ' ','_');
  END ;;

CREATE PROCEDURE restrict_master_orgid_proc (IN role VARCHAR(255), IN org_id BIGINT)
  BEGIN
    IF role = 'MASTER' AND org_id IS NOT NULL THEN
      SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A user with a role of MASTER cannot be assigned an org_id';
    END IF;

    IF role <> 'MASTER' AND org_id IS NULL THEN
      SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'A standard user must be assigned an org_id';
    END IF;
  END ;;

CREATE PROCEDURE restrict_master_role_proc (IN role VARCHAR(255))
  BEGIN
    IF role LIKE '%MASTER%' AND role <> 'MASTER' THEN
      SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Users with MASTER role cannot have other roles';
    END IF;
  END ;;

CREATE TRIGGER users_before_insert_trigger
BEFORE INSERT ON old.users FOR EACH ROW
  BEGIN
    CALL restrict_master_role_proc(NEW.role);
    CALL restrict_master_orgid_proc(NEW.role, NEW.org_id);
  END ;;

CREATE TRIGGER users_before_update_trigger
BEFORE UPDATE ON old.users FOR EACH ROW
  BEGIN
    CALL restrict_master_role_proc(NEW.role);
    CALL restrict_master_orgid_proc(NEW.role, NEW.org_id);
  END ;;
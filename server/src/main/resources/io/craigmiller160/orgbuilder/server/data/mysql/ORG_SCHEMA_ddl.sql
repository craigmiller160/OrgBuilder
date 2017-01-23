-- Creates the table layout for each organization's schema in the database of this application

CREATE TABLE members (
  member_id BIGINT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255),
  middle_name VARCHAR(255),
  last_name VARCHAR(255),
  date_of_birth DATE,
  sex VARCHAR(20),
  PRIMARY KEY (member_id)
);

ALTER TABLE members AUTO_INCREMENT = 1000;

CREATE TABLE addresses (
  address_id BIGINT NOT NULL AUTO_INCREMENT,
  address_type VARCHAR(20) NOT NULL DEFAULT 'HOME',
  address1 VARCHAR(255),
  address2 VARCHAR(255),
  city VARCHAR(255),
  state CHAR(2),
  zip_code CHAR(6),
  preferred_address BOOLEAN NOT NULL DEFAULT FALSE,
  address_member_id BIGINT NOT NULL,
  PRIMARY KEY (address_id),
  FOREIGN KEY (address_member_id) REFERENCES members (member_id)
);

CREATE TABLE phones (
  phone_id BIGINT NOT NULL AUTO_INCREMENT,
  phone_type VARCHAR(20) NOT NULL DEFAULT 'HOME',
  area_code CHAR(3),
  prefix CHAR(3),
  line_number CHAR(4),
  extension VARCHAR(20),
  preferred_phone BOOLEAN NOT NULL DEFAULT FALSE,
  phone_member_id BIGINT NOT NULL,
  PRIMARY KEY (phone_id),
  FOREIGN KEY (phone_member_id) REFERENCES members (member_id)
);

CREATE TABLE emails (
  email_id BIGINT NOT NULL AUTO_INCREMENT,
  email_type VARCHAR(20) NOT NULL DEFAULT 'PERSONAL',
  email_address VARCHAR(255),
  preferred_email BOOLEAN NOT NULL DEFAULT FALSE,
  email_member_id BIGINT NOT NULL,
  PRIMARY KEY (email_id),
  FOREIGN KEY (email_member_id) REFERENCES members (member_id)
);
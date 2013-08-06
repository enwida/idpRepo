--drop all tables	
DROP SCHEMA IF EXISTS users CASCADE;

CREATE SCHEMA users;


CREATE TABLE users.users
(
  user_id serial NOT NULL,
  user_name character varying(45) NOT NULL,
  user_password character varying(45) NOT NULL,
  first_name character varying(45) NOT NULL,
  last_name character varying(45) NOT NULL,
  enabled boolean NOT NULL,
  joining_date date NOT NULL,
  telephone character varying(20),
  company_name character varying(45) NOT NULL,
  company_logo character varying(200),
  activation_id character varying(45),
  username character varying(45),
  CONSTRAINT users_pkey PRIMARY KEY (user_id)
)
;


CREATE TABLE users.groups (
    group_id SERIAL PRIMARY KEY,
    group_name character varying(45) NOT NULL,
    auto_pass boolean NOT NULL
);


CREATE TABLE users.roles (
    role_id SERIAL PRIMARY KEY,
    role_name character varying(45),
    description character varying(45)
);


CREATE TABLE users.rights (
    right_id SERIAL PRIMARY KEY,
    role_id integer,
    tso integer,
    product integer,
    resolution character(50),
    time1 date,
    time2 date,
    aspect_id integer,
    enabled boolean NOT NULL,
	CONSTRAINT "FK_roles_rights" FOREIGN KEY (role_id) REFERENCES users.roles(role_id)  ON DELETE CASCADE
);





CREATE TABLE users.user_group (
	id SERIAL PRIMARY KEY,
    user_id integer NOT NULL,
    group_id integer NOT NULL,
	CONSTRAINT "FK_user_usergroup" FOREIGN KEY (user_id) REFERENCES users.users(user_id) ON DELETE CASCADE,
	CONSTRAINT "FK_group_usergroup" FOREIGN KEY (group_id) REFERENCES users.groups(group_id) ON DELETE CASCADE
);


CREATE TABLE users.group_role (
	id SERIAL PRIMARY KEY,
    group_id integer NOT NULL,
    role_id integer,
	CONSTRAINT "FK_group_group" FOREIGN KEY (group_id) REFERENCES users.groups(group_id)  ON DELETE CASCADE,
	CONSTRAINT "FK_roles_rights" FOREIGN KEY (role_id) REFERENCES users.roles(role_id)  ON DELETE CASCADE
	);
	
-- Before inserting any data in any of the tables make sure all the DDL commands are executed	
INSERT INTO users.users(
		user_name, user_password, first_name, last_name, enabled, 
		joining_date, telephone, company_name)
VALUES ('micha', '123', 'Michael', 'Steck', true, '2013-07-02', '0049 89 1234567','enwida');


INSERT INTO users.roles( role_name, description)
    VALUES ('admin','adminstrator');

INSERT INTO users.roles( role_name, description)
    VALUES ('anonymous','all anonymous users');
	
INSERT INTO users.groups(group_name, auto_pass)
VALUES ('adminGroup', TRUE);


INSERT INTO users.group_role(
            group_id, role_id)
    VALUES (1, 1);

INSERT INTO users.user_group(
            user_id, group_id)
    VALUES (1, 1);
    
CREATE OR REPLACE FUNCTION getAllRights() RETURNS SETOF users.rights AS
$BODY$
DECLARE
    r users.rights%rowtype;
    DECLARE id INT=0;
BEGIN
    LOOP
	id=id+1;
        INSERT INTO users.rights(
            right_id, role_id, tso, product, resolution, time1, time2, aspect_id,enabled)
	VALUES (id, 1, 1, 1, 1, '2012.1.1.', '2012.1.1.', 1, true);
	EXIT WHEN id> 100;
    END LOOP;
    RETURN;
END
$BODY$
LANGUAGE 'plpgsql' ;

delete from users.rights;

SELECT  getAllRights();
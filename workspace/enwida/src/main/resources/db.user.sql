--drop all tables	
DROP SCHEMA IF EXISTS users CASCADE;CREATE SCHEMA users;


CREATE TABLE users.user
(
  user_id serial NOT NULL,
  name character varying NOT NULL,
  user_password character varying(45) NOT NULL,
  first_name character varying(45) NOT NULL,
  last_name character varying(45) NOT NULL,
  enabled boolean NOT NULL,
  joining_date date NOT NULL,
  telephone character varying(20),
  company_name character varying(45) NOT NULL,
  company_logo character varying(200),
  activation_id character varying(45),
  CONSTRAINT users_pkey PRIMARY KEY (user_id)
)
;


CREATE TABLE users.groups (
    group_id SERIAL PRIMARY KEY,
    name character varying(45) NOT NULL,
    auto_pass boolean NOT NULL
);


CREATE TABLE users.roles (
    role_id SERIAL PRIMARY KEY,
    name character varying(45),
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
	CONSTRAINT "FK_user_usergroup" FOREIGN KEY (user_id) REFERENCES users.user(user_id) ON DELETE CASCADE,
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
INSERT INTO users.users(user_id,
		name, user_password, first_name, last_name, enabled, 
		joining_date, telephone, company_name,1)
VALUES (1,'test', 'q12wq12w', 'test', 'test', true, '2013-07-02', '0049 89 1234567','enwida','login_count');



INSERT INTO users.roles(role_id,name, description)
    VALUES (1,'admin','adminstrator');

    
INSERT INTO users.roles(role_id,name, description)
    VALUES (2,'chart','This user can see all charts');

INSERT INTO users.roles(role_id,name, description)
    VALUES (3,'anonymous','all anonymous users');
	
INSERT INTO users.groups(group_id,name, auto_pass)
VALUES (1,'adminGroup', TRUE);

INSERT INTO users.groups(group_id,name, auto_pass)
VALUES (2,'anonymous', TRUE);



INSERT INTO users.group_role(group_id, role_id)
    VALUES (1, 1);

INSERT INTO users.user_group(user_id, group_id)
    VALUES (1, 1);

INSERT INTO users.group_role(group_id,role_id)
	values (1,2);

--Assign anonymous group to anonymous role
INSERT INTO users.group_role(group_id,role_id)
values (2,3);


--First delete previous rights
delete from users.rights ;
--Create rights

DO
$BODY$
DECLARE
--MAX values
    role_id   INT:=2;
    tso INT=99;
    productMAX INT=300;
    resolution varchar[]:= '{"HOURLY","MONTHLY", "DAILY", "WEEKLY", "QUATER_HOURLY", "YEARLY"}';
    aspectMax INT=20;
--Counters
    pCounter INT=0;
    rCounter INT=0;
    aCounter INT=0;
BEGIN

	   FOR pCounter IN 200 .. productMAX  LOOP
		FOR rCounter IN  array_lower(resolution, 1) .. array_upper(resolution, 1)  LOOP
  			FOR aCounter IN 0 .. aspectMax  LOOP
		            INSERT INTO users.rights (role_id,tso,product,resolution,time1,time2,aspect_id,enabled)
				VALUES (role_id,tso,pCounter,resolution[rCounter],'1970-01-01','2500-01-01',aCounter,true);
		        END LOOP;
		END LOOP;
	    END LOOP;
END;
$BODY$ language plpgsql;

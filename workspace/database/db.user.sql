
CREATE SCHEMA users;


CREATE TABLE users.users (
    user_id SERIAL PRIMARY KEY,
    user_name character varying(45) NOT NULL,
    user_password character varying(45) NOT NULL,
    first_name character varying(45) NOT NULL,
    last_name character varying(45) NOT NULL,
    enabled boolean NOT NULL,
    joining_date date NOT NULL,
    telephone character varying(20),
    company_name character varying(45) NOT NULL
);


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
    time1 character(50),
    time2 character(50),
    aspect_id integer,
    enabled boolean NOT NULL,
	CONSTRAINT "FK_roles_rights" FOREIGN KEY (role_id) REFERENCES roles(role_id)
);





CREATE TABLE users.user_group (
    user_id integer NOT NULL,
    group_id integer NOT NULL,
	CONSTRAINT "FK_user_usergroup" FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT "FK_group_usergroup" FOREIGN KEY (group_id) REFERENCES groups(group_id),
	PRIMARY KEY(user_id,group_id)
);


CREATE TABLE users.group_role (
    group_id integer NOT NULL,
    role_id integer,
	CONSTRAINT "FK_group_usergroup" FOREIGN KEY (group_id) REFERENCES groups(group_id),
	CONSTRAINT "FK_roles_rights" FOREIGN KEY (role_id) REFERENCES roles(role_id),
	PRIMARY KEY(group_id,role_id)
	);
	
	
--drop all tables	
drop table users.group_role;
drop table users.user_group;
drop table users.rights;
drop table users.roles;
drop table users.groups;
drop table users.users;

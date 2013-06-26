
SET client_encoding = 'UTF8';

CREATE SCHEMA users;



ALTER SCHEMA users OWNER TO postgres;

SET search_path = users, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;




CREATE TABLE group_role (
    group_id integer NOT NULL,
    role_id integer
);


ALTER TABLE users.group_role OWNER TO enwida;


CREATE TABLE groups (
    group_id integer NOT NULL,
    group_name character varying(45) NOT NULL,
    auto_pass boolean NOT NULL
);


ALTER TABLE users.groups OWNER TO enwida;



CREATE SEQUENCE groups_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.groups_group_id_seq OWNER TO enwida;

ALTER SEQUENCE groups_group_id_seq OWNED BY groups.group_id;


CREATE TABLE rights (
    right_id integer NOT NULL,
    role_id integer,
    tso integer,
    product integer,
    resolution character(50),
    time1 character(50),
    time2 character(50),
    aspect_id integer,
    enabled boolean NOT NULL
);


ALTER TABLE users.rights OWNER TO enwida;



CREATE SEQUENCE rights_right_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.rights_right_id_seq OWNER TO enwida;

ALTER SEQUENCE rights_right_id_seq OWNED BY rights.right_id;


CREATE TABLE roles (
    role_id integer NOT NULL,
    role_name character varying(45),
    description character varying(45)
);


ALTER TABLE users.roles OWNER TO enwida;



CREATE SEQUENCE roles_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.roles_role_id_seq OWNER TO enwida;

ALTER SEQUENCE roles_role_id_seq OWNED BY roles.role_id;

CREATE TABLE user_group (
    user_id integer NOT NULL,
    group_id integer NOT NULL
);


ALTER TABLE users.user_group OWNER TO enwida;


CREATE TABLE users (
    user_id integer NOT NULL,
    user_name character varying(45) NOT NULL,
    user_password character varying(45) NOT NULL,
    first_name character varying(45) NOT NULL,
    last_name character varying(45) NOT NULL,
    enabled boolean NOT NULL,
    joining_date date NOT NULL,
    telephone character varying(20),
    company_name character varying(45) NOT NULL
);


ALTER TABLE users.users OWNER TO enwida;


CREATE SEQUENCE users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.users_user_id_seq OWNER TO enwida;


ALTER SEQUENCE users_user_id_seq OWNED BY users.user_id;

ALTER TABLE ONLY groups ALTER COLUMN group_id SET DEFAULT nextval('groups_group_id_seq'::regclass);

ALTER TABLE ONLY rights ALTER COLUMN right_id SET DEFAULT nextval('rights_right_id_seq'::regclass);
ALTER TABLE ONLY roles ALTER COLUMN role_id SET DEFAULT nextval('roles_role_id_seq'::regclass);
ALTER TABLE ONLY users ALTER COLUMN user_id SET DEFAULT nextval('users_user_id_seq'::regclass);

ALTER TABLE ONLY groups
    ADD CONSTRAINT group_pkey PRIMARY KEY (group_id);

  
ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_group_name_key UNIQUE (group_name);
  ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);

    ALTER TABLE ONLY user_group
    ADD CONSTRAINT user_group_pkey PRIMARY KEY (user_id, group_id);


ALTER TABLE ONLY users
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);

ALTER TABLE ONLY users
    ADD CONSTRAINT username_unique UNIQUE (user_name);


CREATE INDEX "FKI_group_grouprole" ON group_role USING btree (group_id);


CREATE INDEX "FKI_group_usergroup" ON user_group USING btree (group_id);
CREATE INDEX "FKI_role_grouprole" ON group_role USING btree (role_id);

CREATE INDEX "FKI_roles_rights" ON rights USING btree (role_id);

    
ALTER TABLE ONLY group_role
    ADD CONSTRAINT "FK_group_grouprole" FOREIGN KEY (group_id) REFERENCES groups(group_id);



ALTER TABLE ONLY user_group
    ADD CONSTRAINT "FK_group_usergroup" FOREIGN KEY (group_id) REFERENCES groups(group_id);


ALTER TABLE ONLY group_role
    ADD CONSTRAINT "FK_role_grouprole" FOREIGN KEY (role_id) REFERENCES roles(role_id);



ALTER TABLE ONLY rights
    ADD CONSTRAINT "FK_roles_rights" FOREIGN KEY (role_id) REFERENCES roles(role_id);



ALTER TABLE ONLY user_group
    ADD CONSTRAINT "FK_user_usergroup" FOREIGN KEY (user_id) REFERENCES users(user_id);




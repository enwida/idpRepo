-- Clean start
TRUNCATE users.users CASCADE;
TRUNCATE users.groups CASCADE;
TRUNCATE users.roles CASCADE;
TRUNCATE users.rights CASCADE;

DROP SEQUENCE IF EXISTS users.uploaded_file_sequence;

CREATE SEQUENCE users.uploaded_file_sequence
   INCREMENT 1
   START 1;
ALTER SEQUENCE users.uploaded_file_sequence
  OWNER TO enwida;

INSERT INTO users.users (name,email,user_password,first_name,last_name,company_name,login_count,joining_date,enabled) VALUES ('admin','admin@enwida.de','5ebe2294ecd0e0f08eab7690d2a6ee69','Admin','User','enwida.de',0,NOW(),true);

INSERT INTO users.groups (name,auto_pass) VALUES ('admin',false);
INSERT INTO users.groups (name,auto_pass) VALUES ('anonymous',false);
INSERT INTO users.roles (name,description) VALUES ('admin','Admin role');
INSERT INTO users.roles (name,description) VALUES ('chart','Chart role');
INSERT INTO users.roles (name,description) VALUES ('anonymous','Anonymous role');

INSERT INTO users.user_group (user_id,group_id) VALUES ((SELECT user_id FROM users.users WHERE name = 'admin'),(SELECT group_id FROM users.groups WHERE name = 'admin'));

INSERT INTO users.group_role (group_id,role_id) VALUES ((SELECT group_id FROM users.groups WHERE name = 'admin'),(SELECT role_id FROM users.roles WHERE name = 'admin'));



DO
$BODY$
DECLARE
-- values
    role_id   INT:=(SELECT role_id FROM users.roles WHERE name = 'admin');
    tso INT[]:= '{431,99}';
    productMAX INT=1;
    productMIN INT=1;
    resolution varchar[]:= '{"HOURLY","MONTHLY", "DAILY", "WEEKLY", "QUATER_HOURLY", "YEARLY"}';
    aspect varchar[]:= '{"CR_VOL_ACTIVATION","CR_VOL_ACTIVATION_CP","CR_DEGREE_OF_ACTIVATION","CR_ACTIVATION_FREQUENCY","CR_VOL_ACCEPTED","CR_VOL_OFFERED","CR_POWERPRICE_ACCEPTED","CR_POWERPRICE_REJECTED","CR_WORKPRICE_ACCEPTED","CR_WORKPRICE_REJECTED","CR_POWERPRICE_MIN","CR_POWERPRICE_MID","CR_POWERPRICE_MAX","CR_WORKPRICE_ACC_MIN","CR_WORKPRICE_ACC_MID","CR_WORKPRICE_ACC_MAX","CR_WORKPRICE_ACC_MAX","CR_WORKPRICE_MARG_MID","CR_WORKPRICE_MARG_MAX"}';
    aspectMax INT=20;
--Counters
    pCounter INT=0;
    rCounter INT=0;
    aCounter INT=0;
    tCounter INT=0;
BEGIN
	FOR tCounter IN array_lower(tso, 1) .. array_upper(tso, 1)  LOOP
	   FOR pCounter IN productMIN .. productMAX  LOOP
		FOR rCounter IN  array_lower(resolution, 1) .. array_upper(resolution, 1)  LOOP
			FOR aCounter IN  array_lower(aspect, 1) .. array_upper(aspect, 1)  LOOP
		            INSERT INTO users.rights (tso,product,resolution,start_date,end_date,aspect,enabled)
				VALUES (tso[tCounter],pCounter,resolution[rCounter],'1970-01-01','2500-01-01',aspect[aCounter],true);
		        END LOOP;
		END LOOP;
	    END LOOP;
	END LOOP;
END;
$BODY$ language plpgsql;
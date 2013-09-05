DO
$BODY$
DECLARE
-- values
    role_id   INT:=(SELECT role_id FROM users.roles WHERE name = 'admin');
    tso INT=0;
    productMAX INT=1;
    productMIN INT=1;
    resolution varchar[]:= '{"HOURLY","MONTHLY", "DAILY", "WEEKLY", "QUATER_HOURLY", "YEARLY"}';
    aspect varchar[]:= '{"UL_TH_LOAD_PROFILE"}';
    aspectMax INT=20;
--Counters
    pCounter INT=0;
    rCounter INT=0;
    aCounter INT=0;
BEGIN

	   FOR pCounter IN productMIN .. productMAX  LOOP
		FOR rCounter IN  array_lower(resolution, 1) .. array_upper(resolution, 1)  LOOP
			FOR aCounter IN  array_lower(aspect, 1) .. array_upper(aspect, 1)  LOOP
		            INSERT INTO users.rights (role_id,tso,product,resolution,start_date,end_date,aspect,enabled)
				VALUES (role_id,tso,pCounter,resolution[rCounter],'1970-01-01','2500-01-01',aspect[aCounter],true);
		        END LOOP;
		END LOOP;
	    END LOOP;
END;
$BODY$ language plpgsql;
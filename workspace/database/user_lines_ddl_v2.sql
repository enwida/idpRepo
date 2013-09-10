-- Table: public.user_lines

DROP TABLE IF EXISTS public.user_lines;

CREATE TABLE public.user_lines
(
  "time" timestamp with time zone,
  value double precision,
  user_line_id bigint,
  CONSTRAINT user_lines_pkey PRIMARY KEY ("time",user_line_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_lines
  OWNER TO enwida;

DROP SCHEMA IF EXISTS users  cascade;
CREATE SCHEMA users authorization enwida;

DROP SEQUENCE IF EXISTS users.uploaded_file_sequence;

CREATE SEQUENCE users.uploaded_file_sequence
   INCREMENT 1
   START 1;
ALTER SEQUENCE users.uploaded_file_sequence
  OWNER TO enwida;
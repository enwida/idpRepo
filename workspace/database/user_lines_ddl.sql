-- Table: public.user_lines

DROP TABLE IF EXISTS public.user_lines;

CREATE TABLE public.user_lines
(
  id serial NOT NULL,
  "timestamp" timestamp with time zone,
  value double precision,
  metadata_id integer,
  CONSTRAINT user_lines_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_lines
  OWNER TO enwida;
-- Table: public.user_lines

DROP TABLE IF EXISTS public.user_lines;

CREATE TABLE public.user_lines
(
  "timestamp" timestamp with time zone,
  value double precision,
  user_line_id varchar(255),
  CONSTRAINT user_lines_pkey PRIMARY KEY ("timestamp",user_line_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.user_lines
  OWNER TO enwida;
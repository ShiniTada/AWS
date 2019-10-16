
-------------------------------------------------TABLES----------------------------------------------
CREATE TABLE image_metadata
(
   name varchar(255) NOT NULL,
   path varchar(255) NOT NULL,
   size bigint,
   creation_date date,
   CONSTRAINT image_metadata_pkey PRIMARY KEY(path)
);
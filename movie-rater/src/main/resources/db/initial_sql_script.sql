CREATE TABLE registration (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	email varchar(100) NOT NULL,
	token uuid NOT NULL,
	CONSTRAINT registration_pk PRIMARY KEY (id),
	constraint registration_uq UNIQUE (email)
);

CREATE TABLE "user" (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	first_name varchar(50) NOT NULL,
	last_name varchar(50) NOT NULL,
	username varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	constraint user_username_uq UNIQUE (username)
);

CREATE TABLE movie (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	imdb_id varchar(50) NOT NULL,
	image_url varchar(200) NOT NULL,
	title varchar(50) NOT NULL,
	description text NOT NULL,
	movie_year int8 NOT NULL,
	CONSTRAINT movie_pk PRIMARY KEY (id),
	CONSTRAINT movie_uq UNIQUE (imdb_id)
);

CREATE TABLE review (
	id int8 GENERATED ALWAYS AS IDENTITY NOT NULL,
	user_id int8 NOT NULL,
	movie_id int8 NOT NULL,
	created_at timestamp NOT NULL,
	rating int8 NULL,
	review text NULL,
	CONSTRAINT review_pk PRIMARY KEY (id),
	CONSTRAINT review_uq UNIQUE (user_id, movie_id),
	CONSTRAINT review_user_fk FOREIGN KEY (user_id) REFERENCES "user"(id),
	CONSTRAINT review_movie_fk FOREIGN KEY (movie_id) REFERENCES movie(id)
);
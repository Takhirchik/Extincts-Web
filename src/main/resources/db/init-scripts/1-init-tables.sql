create table if not exists roles (
	id serial primary key,
	name varchar(20) not null
);

create table if not exists users (
	id serial primary key,
	nickname varchar(100) not null,
	login varchar(100) not null unique,
	password varchar(100) not null,
	email varchar(100) not null,
	role_id int not null,
	foreign key (role_id) references roles(id)
);

create table if not exists stand_categories (
	id serial primary key,
	category_name varchar(50) not null unique
);

create table if not exists stands (
	id serial primary key,
	stand_name varchar(100) not null,
	description text,
	stand_category_id int not null,
	foreign key (stand_category_id) references stand_categories(id)
);

create table if not exists extincts (
	id serial primary key,
	extinct_name varchar(100) not null,
	description text,
	stand_id int not null,
	foreign key (stand_id) references stands(id)
);

create table if not exists extincts_images (
	id serial primary key,
	url_image text not null,
	extinct_id int not null,
	foreign key (extinct_id) references extincts(id)
);
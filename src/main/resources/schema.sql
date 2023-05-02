drop table if exists ingredient cascade
drop table if exists recipe cascade
drop table if exists recipe_ingredient cascade
drop table if exists unit cascade
create table ingredient (id uuid not null, name varchar(255), primary key (id))
create table recipe (id uuid not null, description varchar(255), name varchar(255), primary key (id))
create table recipe_ingredient (id uuid not null, quantity float(53), ingredient_id uuid, recipe_id uuid, unit_id uuid, primary key (id))
create table unit (id uuid not null, name varchar(255), primary key (id))
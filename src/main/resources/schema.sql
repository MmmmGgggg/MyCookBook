drop table if exists ingredient cascade
drop table if exists recipe cascade
drop table if exists recipe_ingredient cascade
drop table if exists unit cascade
drop table if exists recipe_search cascade
create table ingredient (id uuid not null, name varchar(255), primary key (id))
create table recipe (id uuid not null, description varchar(255), name varchar(255), processed varchar(255), primary key (id))
create table recipe_ingredient (id uuid not null, quantity float(53), ingredient_id uuid, recipe_id uuid, unit_id uuid, primary key (id))
create table unit (id uuid not null, name varchar(255), primary key (id))
create table recipe_search (id uuid not null, ingredients_combination varchar(255) not null, nr_combinations integer, recipe_id uuid not null, primary key (id))


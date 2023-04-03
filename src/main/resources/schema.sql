DROP TABLE IF EXISTS RECIPE;
create table recipe (id uuid not null, description varchar(255), name varchar(255), primary key (id))
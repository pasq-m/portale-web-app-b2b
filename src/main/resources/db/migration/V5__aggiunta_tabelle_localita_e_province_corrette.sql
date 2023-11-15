create table localita (id integer not null auto_increment, cap varchar(255), descrizione varchar(255), id_provincia integer, primary key (id)) engine=InnoDB;
create table province (id integer not null auto_increment, codice varchar(255), id_regione integer, primary key (id)) engine=InnoDB;
alter table localita add constraint FKod44itko6kxf9pib7eo5bonj9 foreign key (id_provincia) references province (id);
alter table province add constraint FKnyamklwn0wptshsw44wle2i71 foreign key (id_regione) references regioni (id);
create user config_user with password 'secret1';
create user ingestor_user with password 'secret2';

create database config owner config_user;
create database ingestor owner ingestor_user;


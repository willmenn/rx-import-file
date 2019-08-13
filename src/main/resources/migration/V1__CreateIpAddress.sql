CREATE TABLE IF NOT EXISTS IPADDRESS (
ip_address varchar(255),
country_code varchar(255),
country varchar(255),
city varchar(255),
latitude varchar(255),
longitude varchar(255),
mystery_value varchar(255),
CONSTRAINT ipaddress_pk PRIMARY KEY (ip_address)
)
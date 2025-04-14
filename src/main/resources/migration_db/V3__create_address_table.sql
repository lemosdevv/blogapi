CREATE TABLE address (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  street varchar(255),
  suite varchar(255),
  city varchar(255),
  zipcode varchar(255),
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by bigint(19) unsigned DEFAULT NULL,
  updated_by bigint(19) unsigned DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
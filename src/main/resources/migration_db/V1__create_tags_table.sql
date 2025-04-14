CREATE TABLE tags (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by bigint(19) unsigned NOT NULL,
  updated_by bigint(19) unsigned NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
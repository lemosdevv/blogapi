CREATE TABLE todos (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  completed boolean default false,
  user_id bigint(19) unsigned DEFAULT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by bigint(19) unsigned DEFAULT NULL,
  updated_by bigint(19) unsigned DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_user_todos (user_id),
  CONSTRAINT fk_user_todos FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
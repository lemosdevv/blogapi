CREATE TABLE user_role (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  user_id bigint(19) unsigned NOT NULL,
  role_id bigint(19) unsigned NOT NULL,
  PRIMARY KEY (id),
  KEY fk_security_user_id (user_id),
  KEY fk_security_role_id (role_id),
  CONSTRAINT fk_security_user_id FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_security_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
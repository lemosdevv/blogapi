CREATE TABLE comments (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  body text NOT NULL,
  post_id bigint(19) unsigned DEFAULT NULL,
  user_id bigint(19) unsigned DEFAULT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by bigint(19) unsigned NOT NULL,
  updated_by bigint(19) unsigned NOT NULL,
  PRIMARY KEY (id),
  KEY fk_comment_post (post_id),
  KEY fk_comment_user (user_id),
  CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (id),
  CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE photos (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  url varchar(255) NOT NULL,
  thumbnail_url varchar(255) NOT NULL,
  album_id bigint(19) unsigned DEFAULT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by bigint(19) unsigned DEFAULT NULL,
  updated_by bigint(19) unsigned DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_album (album_id),
  CONSTRAINT fk_album FOREIGN KEY (album_id) REFERENCES albums (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
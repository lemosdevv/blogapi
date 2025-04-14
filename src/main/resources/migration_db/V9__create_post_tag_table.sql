CREATE TABLE post_tag (
  id bigint(19) unsigned NOT NULL AUTO_INCREMENT,
  post_id bigint(19) unsigned NOT NULL,
  tag_id bigint(19) unsigned NOT NULL,
  PRIMARY KEY (id),
  KEY fk_posttag_post_id (post_id),
  KEY fk_posttag_tag_id (tag_id),
  CONSTRAINT fk_posttag_post_id FOREIGN KEY (post_id) REFERENCES posts (id),
  CONSTRAINT fk_posttag_tag_id FOREIGN KEY (tag_id) REFERENCES tags (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
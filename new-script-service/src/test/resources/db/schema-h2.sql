DROP TABLE IF EXISTS step_definition;
DROP TABLE IF EXISTS script_version;
DROP TABLE IF EXISTS script;

CREATE TABLE script (
  id varchar(64) NOT NULL,
  name varchar(255) NOT NULL,
  description text,
  status varchar(32) NOT NULL,
  current_version_id varchar(64) DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE script_version (
  id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  version_no int NOT NULL,
  version_status varchar(32) NOT NULL,
  description text,
  published_at datetime DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_script_version_no (script_id, version_no)
);

CREATE TABLE step_definition (
  id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  parent_step_id varchar(64) DEFAULT NULL,
  step_type varchar(32) NOT NULL,
  name varchar(255) NOT NULL,
  sort_no int NOT NULL,
  request_method varchar(16) DEFAULT NULL,
  request_url text,
  request_config longtext,
  assertion_config longtext,
  extractor_config longtext,
  enabled tinyint NOT NULL DEFAULT 1,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

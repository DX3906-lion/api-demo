DROP TABLE IF EXISTS step_execution_snapshot;
DROP TABLE IF EXISTS flow_execution_record;
DROP TABLE IF EXISTS execution_task;
DROP TABLE IF EXISTS execution_plan_instance;
DROP TABLE IF EXISTS execution_plan_case;
DROP TABLE IF EXISTS execution_plan;
DROP TABLE IF EXISTS step_definition;
DROP TABLE IF EXISTS case_field_value;
DROP TABLE IF EXISTS case_data_set;
DROP TABLE IF EXISTS script_field_default;
DROP TABLE IF EXISTS field_config;
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

CREATE TABLE field_config (
  id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  step_id varchar(64) NOT NULL,
  field_scope varchar(32) NOT NULL,
  field_path varchar(1000) DEFAULT NULL,
  field_key varchar(255) DEFAULT NULL,
  stable_field_key varchar(500) DEFAULT NULL,
  field_name varchar(255) DEFAULT NULL,
  data_type varchar(64) DEFAULT NULL,
  required tinyint NOT NULL DEFAULT 0,
  array_flag tinyint NOT NULL DEFAULT 0,
  sensitive tinyint NOT NULL DEFAULT 0,
  description text,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE script_field_default (
  id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  field_config_id varchar(64) NOT NULL,
  default_value longtext,
  value_source varchar(64) DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_script_field_default (script_version_id, field_config_id)
);

CREATE TABLE case_data_set (
  id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  name varchar(255) NOT NULL,
  description text,
  status varchar(32) NOT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE case_field_value (
  id varchar(64) NOT NULL,
  case_data_set_id varchar(64) NOT NULL,
  field_config_id varchar(64) NOT NULL,
  value longtext,
  value_source varchar(64) DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_case_field_value (case_data_set_id, field_config_id)
);

CREATE TABLE execution_plan (
  id varchar(64) NOT NULL,
  system_id varchar(64) DEFAULT NULL,
  plan_name varchar(255) NOT NULL,
  env_id varchar(64) DEFAULT NULL,
  engine_version varchar(16) DEFAULT NULL,
  run_mode varchar(32) NOT NULL,
  schedule_cron varchar(128) DEFAULT NULL,
  case_run_mode varchar(32) NOT NULL,
  max_concurrency int DEFAULT NULL,
  failure_strategy varchar(32) DEFAULT NULL,
  retry_count int DEFAULT NULL,
  status varchar(32) NOT NULL,
  description text,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE execution_plan_case (
  id varchar(64) NOT NULL,
  plan_id varchar(64) NOT NULL,
  case_id varchar(64) NOT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  env_id varchar(64) DEFAULT NULL,
  order_no int NOT NULL,
  enabled char(1) NOT NULL DEFAULT '1',
  config_json longtext,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_execution_plan_case (plan_id, case_id)
);

CREATE TABLE execution_plan_instance (
  id varchar(64) NOT NULL,
  plan_id varchar(64) NOT NULL,
  trigger_type varchar(32) NOT NULL,
  trigger_time datetime NOT NULL,
  triggered_by varchar(64) DEFAULT NULL,
  env_id varchar(64) DEFAULT NULL,
  status varchar(32) NOT NULL,
  total_count int NOT NULL DEFAULT 0,
  success_count int NOT NULL DEFAULT 0,
  failed_count int NOT NULL DEFAULT 0,
  skipped_count int NOT NULL DEFAULT 0,
  duration_ms bigint DEFAULT NULL,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  summary_json longtext,
  error_message longtext,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE execution_task (
  id varchar(64) NOT NULL,
  plan_instance_id varchar(64) DEFAULT NULL,
  plan_id varchar(64) DEFAULT NULL,
  case_id varchar(64) DEFAULT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  env_id varchar(64) DEFAULT NULL,
  engine_version varchar(16) DEFAULT NULL,
  execution_id varchar(64) DEFAULT NULL,
  status varchar(32) NOT NULL,
  order_no int DEFAULT NULL,
  retry_count int NOT NULL DEFAULT 0,
  max_retry_count int NOT NULL DEFAULT 0,
  executor_node_id varchar(64) DEFAULT NULL,
  dispatch_time datetime DEFAULT NULL,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  duration_ms bigint DEFAULT NULL,
  error_message longtext,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE flow_execution_record (
  id varchar(64) NOT NULL,
  task_id varchar(64) DEFAULT NULL,
  plan_id varchar(64) DEFAULT NULL,
  plan_instance_id varchar(64) DEFAULT NULL,
  case_id varchar(64) DEFAULT NULL,
  script_id varchar(64) NOT NULL,
  script_version_id varchar(64) NOT NULL,
  env_id varchar(64) DEFAULT NULL,
  execution_type varchar(32) NOT NULL,
  status varchar(32) NOT NULL,
  trace_id varchar(128) DEFAULT NULL,
  duration_ms bigint DEFAULT NULL,
  env_snapshot_json longtext,
  variable_snapshot_json longtext,
  final_variable_snapshot_json longtext,
  result_summary_json longtext,
  error_code varchar(64) DEFAULT NULL,
  error_message longtext,
  triggered_by varchar(64) DEFAULT NULL,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE step_execution_snapshot (
  id varchar(64) NOT NULL,
  execution_id varchar(64) NOT NULL,
  step_id varchar(64) NOT NULL,
  step_order_no int DEFAULT NULL,
  step_name varchar(255) DEFAULT NULL,
  step_type varchar(32) DEFAULT NULL,
  status varchar(32) NOT NULL,
  resolved_method varchar(16) DEFAULT NULL,
  resolved_url longtext,
  resolved_query_json longtext,
  resolved_request_headers_json longtext,
  resolved_request_cookies_json longtext,
  resolved_request_body longtext,
  response_status_code int DEFAULT NULL,
  response_headers_json longtext,
  response_cookies_json longtext,
  response_body longtext,
  response_content_type varchar(255) DEFAULT NULL,
  response_time_ms bigint DEFAULT NULL,
  raw_input_snapshot_json longtext,
  resolved_field_value_json longtext,
  extracted_variables_json longtext,
  assert_result_json longtext,
  execution_log longtext,
  error_code varchar(64) DEFAULT NULL,
  error_message longtext,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  created_time datetime NOT NULL,
  updated_time datetime NOT NULL,
  created_by varchar(64) DEFAULT NULL,
  updated_by varchar(64) DEFAULT NULL,
  deleted tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

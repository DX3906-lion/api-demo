-- V2__seed_demo_data.sql
-- 演示数据：仅用于本地联调，禁止用于生产环境

INSERT INTO script (
  id, name, description, status, current_version_id,
  created_time, updated_time, created_by, updated_by, deleted
) VALUES (
  'script_demo_001', 'demo-http-script', '本地演示脚本（草稿）', 'DRAFT', 'script_ver_demo_001',
  NOW(), NOW(), 'system', 'system', 0
);

INSERT INTO script_version (
  id, script_id, version_no, version_status, description, published_at,
  created_time, updated_time, created_by, updated_by, deleted
) VALUES (
  'script_ver_demo_001', 'script_demo_001', 1, 'DRAFT', '初始草稿版本', NULL,
  NOW(), NOW(), 'system', 'system', 0
);

INSERT INTO step_definition (
  id, script_id, script_version_id, parent_step_id, step_type, name, sort_no,
  request_method, request_url, request_config, assertion_config, extractor_config, enabled,
  created_time, updated_time, created_by, updated_by, deleted
) VALUES (
  'step_demo_001', 'script_demo_001', 'script_ver_demo_001', NULL, 'API_STEP', 'demo-http-step', 1,
  'POST', 'http://localhost:18080/api/demo',
  '{"headers":{"Content-Type":"application/json"}}',
  '{"type":"status_code","expected":200}',
  '{"jsonPath":"$.code","var":"respCode"}',
  1,
  NOW(), NOW(), 'system', 'system', 0
);

INSERT INTO field_config (
  id, script_id, script_version_id, step_id, field_scope, field_path, field_key, stable_field_key,
  field_name, data_type, required, array_flag, sensitive, description,
  created_time, updated_time, created_by, updated_by, deleted
) VALUES
(
  'field_demo_001', 'script_demo_001', 'script_ver_demo_001', 'step_demo_001',
  'REQUEST_BODY', '$.amount', 'amount', 'request.body.amount',
  'amount', 'NUMBER', 1, 0, 0, '交易金额',
  NOW(), NOW(), 'system', 'system', 0
),
(
  'field_demo_002', 'script_demo_001', 'script_ver_demo_001', 'step_demo_001',
  'REQUEST_BODY', '$.currency', 'currency', 'request.body.currency',
  'currency', 'STRING', 1, 0, 0, '币种',
  NOW(), NOW(), 'system', 'system', 0
);

INSERT INTO script_field_default (
  id, script_id, script_version_id, field_config_id, default_value, value_source,
  created_time, updated_time, created_by, updated_by, deleted
) VALUES
(
  'sfd_demo_001', 'script_demo_001', 'script_ver_demo_001', 'field_demo_001',
  '100.00', 'LITERAL', NOW(), NOW(), 'system', 'system', 0
),
(
  'sfd_demo_002', 'script_demo_001', 'script_ver_demo_001', 'field_demo_002',
  'CNY', 'LITERAL', NOW(), NOW(), 'system', 'system', 0
);

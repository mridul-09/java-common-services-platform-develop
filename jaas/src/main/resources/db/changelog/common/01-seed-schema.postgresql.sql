-- liquibase formatted sql

-- changeset mokshesh:01-seed-schema-1
CREATE TABLE IF NOT EXISTS facilities
(
  id               BIGINT  NOT NULL,
  created_at       BIGINT  NOT NULL,
  created_by       BIGINT,
  modified_at      BIGINT  NOT NULL,
  modified_by      BIGINT,
  archived         BOOLEAN NOT NULL,
  name             VARCHAR(150),
  organisations_id BIGINT  NOT NULL,
  CONSTRAINT facilities_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-2
CREATE TABLE IF NOT EXISTS organisation_facilities_mapping
(
  facilities_id    BIGINT NOT NULL,
  organisations_id BIGINT NOT NULL,
  created_at       BIGINT NOT NULL,
  created_by       BIGINT,
  CONSTRAINT organisation_facilities_mapping_pkey PRIMARY KEY (facilities_id, organisations_id)
);

-- changeset mokshesh:01-seed-schema-3
CREATE TABLE IF NOT EXISTS users
(
  id                         BIGINT       NOT NULL,
  created_at                 BIGINT       NOT NULL,
  created_by                 BIGINT,
  modified_at                BIGINT       NOT NULL,
  modified_by                BIGINT,
  archived                   BOOLEAN      NOT NULL,
  blocked                    BOOLEAN,
  blocked_at                 BIGINT,
  department                 VARCHAR(255),
  email                      VARCHAR(255),
  employee_id                VARCHAR(45),
  failed_login_attempts      BIGINT,
  first_name                 VARCHAR(255),
  is_password_reset_required BOOLEAN,
  is_system_user             BOOLEAN,
  last_name                  VARCHAR(255),
  organisations_id           BIGINT,
  password                   TEXT,
  password_updated_at        BIGINT,
  username                   VARCHAR(255) NOT NULL,
  verified                   BOOLEAN      NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-4
CREATE TABLE IF NOT EXISTS permissions
(
  id          BIGINT  NOT NULL,
  created_at  BIGINT  NOT NULL,
  created_by  BIGINT,
  modified_at BIGINT  NOT NULL,
  modified_by BIGINT,
  archived    BOOLEAN NOT NULL,
  description TEXT,
  method      VARCHAR(15),
  name        VARCHAR(255),
  path        TEXT,
  services_id VARCHAR(255),
  CONSTRAINT permissions_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-5
CREATE TABLE IF NOT EXISTS role_permissions_mapping
(
  permissions_id BIGINT NOT NULL,
  roles_id       BIGINT NOT NULL,
  created_at     BIGINT NOT NULL,
  created_by     BIGINT,
  CONSTRAINT role_permissions_mapping_pkey PRIMARY KEY (permissions_id, roles_id)
);

-- changeset mokshesh:01-seed-schema-6
CREATE TABLE IF NOT EXISTS organisations
(
  id          BIGINT  NOT NULL,
  created_at  BIGINT  NOT NULL,
  created_by  BIGINT,
  modified_at BIGINT  NOT NULL,
  modified_by BIGINT,
  archived    BOOLEAN NOT NULL,
  is_master   BOOLEAN NOT NULL,
  name        VARCHAR(255),
  CONSTRAINT organisations_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-7
CREATE TABLE IF NOT EXISTS organisation_services_mapping
(
  organisations_id BIGINT       NOT NULL,
  services_id      VARCHAR(255) NOT NULL,
  created_at       BIGINT       NOT NULL,
  created_by       BIGINT,
  CONSTRAINT organisation_services_mapping_pkey PRIMARY KEY (organisations_id, services_id)
);

-- changeset mokshesh:01-seed-schema-8
CREATE TABLE IF NOT EXISTS services
(
  id                  VARCHAR(255) NOT NULL,
  archived            BOOLEAN      NOT NULL,
  created_at          BIGINT       NOT NULL,
  created_by          BIGINT,
  login_page          TEXT,
  modified_at         BIGINT       NOT NULL,
  modified_by         BIGINT,
  name                VARCHAR(45),
  registration_page   TEXT,
  reset_password_page TEXT,
  CONSTRAINT services_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-9
CREATE TABLE IF NOT EXISTS scopes
(
  id              BIGINT  NOT NULL,
  created_at      BIGINT  NOT NULL,
  created_by      BIGINT,
  modified_at     BIGINT  NOT NULL,
  modified_by     BIGINT,
  archived        BOOLEAN NOT NULL,
  name            VARCHAR(255),
  order_tree      INTEGER NOT NULL,
  scope_groups_id BIGINT  NOT NULL,
  CONSTRAINT scopes_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-10
CREATE TABLE IF NOT EXISTS user_facilities_mapping
(
  facilities_id BIGINT NOT NULL,
  users_id      BIGINT NOT NULL,
  created_at    BIGINT NOT NULL,
  created_by    BIGINT,
  CONSTRAINT user_facilities_mapping_pkey PRIMARY KEY (facilities_id, users_id)
);

-- changeset mokshesh:01-seed-schema-11
CREATE TABLE IF NOT EXISTS scope_groups
(
  id          BIGINT  NOT NULL,
  created_at  BIGINT  NOT NULL,
  created_by  BIGINT,
  modified_at BIGINT  NOT NULL,
  modified_by BIGINT,
  archived    BOOLEAN NOT NULL,
  name        VARCHAR(90),
  order_tree  INTEGER NOT NULL,
  services_id VARCHAR(255),
  CONSTRAINT scope_groups_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-12
CREATE TABLE IF NOT EXISTS password_policies
(
  id                                          BIGINT NOT NULL,
  created_at                                  BIGINT NOT NULL,
  created_by                                  BIGINT,
  modified_at                                 BIGINT NOT NULL,
  modified_by                                 BIGINT,
  allow_password_similar_to_username_or_email BOOLEAN,
  maximum_password_age                        INTEGER,
  minimum_lowercase_characters                INTEGER,
  minimum_numeric_characters                  INTEGER,
  minimum_password_history                    INTEGER,
  minimum_password_length                     INTEGER,
  minimum_special_characters                  INTEGER,
  minimum_uppercase_characters                INTEGER,
  organisations_id                            BIGINT,
  CONSTRAINT password_policies_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-13
CREATE TABLE IF NOT EXISTS organisation_settings
(
  id                              BIGINT NOT NULL,
  created_at                      BIGINT NOT NULL,
  created_by                      BIGINT,
  modified_at                     BIGINT NOT NULL,
  modified_by                     BIGINT,
  auto_unblock_after              INTEGER,
  max_failed_login_attempts       INTEGER,
  organisations_id                BIGINT,
  access_token_expiration         INTEGER,
  refresh_token_expiration        INTEGER,
  password_expiration             INTEGER,
  password_reset_token_expiration INTEGER,
  registration_token_expiration   INTEGER,
  session_idle_timeout            INTEGER,
  CONSTRAINT organisation_settings_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-14
CREATE TABLE IF NOT EXISTS roles
(
  id          BIGINT  NOT NULL,
  created_at  BIGINT  NOT NULL,
  created_by  BIGINT,
  modified_at BIGINT  NOT NULL,
  modified_by BIGINT,
  archived    BOOLEAN NOT NULL,
  name        VARCHAR(90),
  services_id VARCHAR(255),
  CONSTRAINT roles_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-15
CREATE TABLE IF NOT EXISTS role_scope_groups_mapping
(
  roles_id        BIGINT NOT NULL,
  scope_groups_id BIGINT NOT NULL,
  created_at      BIGINT NOT NULL,
  created_by      BIGINT,
  CONSTRAINT role_scope_groups_mapping_pkey PRIMARY KEY (roles_id, scope_groups_id)
);

-- changeset mokshesh:01-seed-schema-16
CREATE TABLE IF NOT EXISTS user_roles_mapping
(
  roles_id   BIGINT NOT NULL,
  users_id   BIGINT NOT NULL,
  created_at BIGINT NOT NULL,
  created_by BIGINT,
  CONSTRAINT user_roles_mapping_pkey PRIMARY KEY (roles_id, users_id)
);

-- changeset mokshesh:01-seed-schema-17
CREATE TABLE IF NOT EXISTS password_history
(
  id         BIGINT NOT NULL,
  created_at BIGINT NOT NULL,
  password   TEXT,
  users_id   BIGINT,
  CONSTRAINT password_history_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-18
CREATE TABLE IF NOT EXISTS tokens
(
  token      VARCHAR(32) NOT NULL,
  expiration BIGINT,
  type       VARCHAR(32) NOT NULL,
  users_id   BIGINT,
  CONSTRAINT tokens_pkey PRIMARY KEY (token)
);

-- changeset mokshesh:01-seed-schema-19
CREATE TABLE IF NOT EXISTS user_audits
(
  id               BIGINT NOT NULL,
  action           VARCHAR(255),
  details          TEXT,
  diff_data        JSONB,
  new_data         JSONB,
  old_data         JSONB,
  organisations_id BIGINT,
  severity         VARCHAR(255),
  triggered_at     BIGINT,
  triggered_by     BIGINT NOT NULL,
  CONSTRAINT user_audits_pkey PRIMARY KEY (id)
);

-- changeset mokshesh:01-seed-schema-20
ALTER TABLE organisation_facilities_mapping
  ADD CONSTRAINT fk2gl9pgd5pfxlb4i7iit6ofwxj FOREIGN KEY (facilities_id) REFERENCES facilities (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-21
ALTER TABLE user_facilities_mapping
  ADD CONSTRAINT fkhqdskp1q4u2ffb61h8a2bln4s FOREIGN KEY (facilities_id) REFERENCES facilities (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-22
ALTER TABLE users
  ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

-- changeset mokshesh:01-seed-schema-23
ALTER TABLE users
  ADD CONSTRAINT ukps8jn3qjcop4ptb756hvqnad3 UNIQUE (employee_id, organisations_id, archived);

-- changeset mokshesh:01-seed-schema-24
ALTER TABLE users
  ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);

-- changeset mokshesh:01-seed-schema-25
ALTER TABLE permissions
  ADD CONSTRAINT fk969n0rlcq9vy1eitcbvvjfpmd FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-26
ALTER TABLE permissions
  ADD CONSTRAINT fkswqlowwcdv4nsgr00xfgdi4ky FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-27
ALTER TABLE permissions
  ADD CONSTRAINT uk4stge73ntans3a1km7sxgtmwb UNIQUE (name, services_id, archived);

-- changeset mokshesh:01-seed-schema-28
ALTER TABLE role_permissions_mapping
  ADD CONSTRAINT fk3n65q9h4enr99aybq0uvgw4nh FOREIGN KEY (permissions_id) REFERENCES permissions (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-29
ALTER TABLE role_permissions_mapping
  ADD CONSTRAINT fkkkvnsag455fp16hldwhdd0oih FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-32
ALTER TABLE organisation_services_mapping
  ADD CONSTRAINT fk7411dafrebkafo7uqmifox83t FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-33
ALTER TABLE organisation_services_mapping
  ADD CONSTRAINT fkcnr64kn9vae0vitedqlbn16ny FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-34
ALTER TABLE services
  ADD CONSTRAINT fknaje90ybl9ehmwiiomsiyl4nl FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-35
ALTER TABLE services
  ADD CONSTRAINT fksije8l9h0jqqw6dsokn93138s FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-36
ALTER TABLE services
  ADD CONSTRAINT uk_h4rqgjwnqidx6mvj4i22dxwxe UNIQUE (name);

-- changeset mokshesh:01-seed-schema-37
ALTER TABLE scopes
  ADD CONSTRAINT fk6c6wtx3ljdmyj836tu23qau1b FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-38
ALTER TABLE scopes
  ADD CONSTRAINT fkj2yyceojntnrncaxj1v6n6y0k FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-39
ALTER TABLE scopes
  ADD CONSTRAINT ukrbhy5v58w1gtysg0h9smgo0no UNIQUE (name, scope_groups_id, archived);

-- changeset mokshesh:01-seed-schema-40
ALTER TABLE user_facilities_mapping
  ADD CONSTRAINT fk6tjdtc3xhdpasrv9gdx851e7a FOREIGN KEY (users_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-41
ALTER TABLE user_facilities_mapping
  ADD CONSTRAINT fk9qn8tnlfjlqiypuhes39v215e FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-42
ALTER TABLE scope_groups
  ADD CONSTRAINT fk8ra8xi8snu51d98gc2xabb74p FOREIGN KEY (services_id) REFERENCES services (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-43
ALTER TABLE scope_groups
  ADD CONSTRAINT fklvdjibyg9wmoitx5ocytjg5ib FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-44
ALTER TABLE scope_groups
  ADD CONSTRAINT fkmldcj5wwbfmvow0wa2ghkeonl FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-45
ALTER TABLE scope_groups
  ADD CONSTRAINT ukoffla5dgtnyn1bckb80v8n5x1 UNIQUE (name, services_id, archived);

-- changeset mokshesh:01-seed-schema-46
ALTER TABLE password_policies
  ADD CONSTRAINT fk8wi3r4u8890lmkb4p25ghkr9w FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-47
ALTER TABLE password_policies
  ADD CONSTRAINT fk9kecghr4v0emfdu8pvql3g25t FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-48
ALTER TABLE password_policies
  ADD CONSTRAINT fkh9auym8pmhf3ch4yy12upn367 FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-49
ALTER TABLE password_policies
  ADD CONSTRAINT uk_i31gwblxtrvf9xw9020ve5hpg UNIQUE (organisations_id);

-- changeset mokshesh:01-seed-schema-50
ALTER TABLE organisation_settings
  ADD CONSTRAINT fk9fpebw9gn54vgfg2tymthuefr FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-51
ALTER TABLE organisation_settings
  ADD CONSTRAINT fkb3ipeaqo0ljm8qurd50vmmkpj FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-52
ALTER TABLE organisation_settings
  ADD CONSTRAINT fkfbkx8ijpw9bs1kix5pvxjtjpa FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-53
ALTER TABLE organisation_settings
  ADD CONSTRAINT uk_e569e2kfei3fndvp2h698faw6 UNIQUE (organisations_id);

-- changeset mokshesh:01-seed-schema-54
ALTER TABLE roles
  ADD CONSTRAINT fkbxp1330h4b9f7n4h5sr5ul5xy FOREIGN KEY (services_id) REFERENCES services (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-55
ALTER TABLE roles
  ADD CONSTRAINT fkkbv9sinbix1n1w3dj8tcor7qe FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-56
ALTER TABLE roles
  ADD CONSTRAINT fkq6ium4se7bjk3mfbj3qm1gvy FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-57
ALTER TABLE roles
  ADD CONSTRAINT uksclfxide2pa64qcuo9wkr5pq UNIQUE (name, services_id, archived);

-- changeset mokshesh:01-seed-schema-58
ALTER TABLE role_scope_groups_mapping
  ADD CONSTRAINT fkbfy11wy2vbi8hqhq6jet86may FOREIGN KEY (roles_id) REFERENCES roles (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-59
ALTER TABLE role_scope_groups_mapping
  ADD CONSTRAINT fkk2is6m5dna657d3uclany49fi FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-60
ALTER TABLE role_scope_groups_mapping
  ADD CONSTRAINT fkokhjmfg0slwu0h8wnnavlfh12 FOREIGN KEY (scope_groups_id) REFERENCES scope_groups (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-61
ALTER TABLE user_roles_mapping
  ADD CONSTRAINT fkd0c4akn0iryn8umt2w81gp2a8 FOREIGN KEY (roles_id) REFERENCES roles (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-62
ALTER TABLE user_roles_mapping
  ADD CONSTRAINT fkkqduk9rn1l3w105ijhej346hm FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-63
ALTER TABLE user_roles_mapping
  ADD CONSTRAINT fklw04i55g1ydtct714ydlapc1u FOREIGN KEY (users_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-64
CREATE INDEX idxb9xt56fyg8luuosihexhda3ke ON password_history (users_id);

-- changeset mokshesh:01-seed-schema-65
CREATE INDEX idxgx5w2mh7k08opon3a4xb1sasg ON tokens (type, token);

-- changeset mokshesh:01-seed-schema-66
CREATE INDEX idxa8enhqqf54anje6jcvv2ymnh5 ON user_audits (organisations_id);

-- changeset mokshesh:01-seed-schema-67
CREATE INDEX idxrf922wo0pk10bldoh9si309v8 ON user_audits (organisations_id, triggered_by);

-- changeset mokshesh:01-seed-schema-68
ALTER TABLE facilities
  ADD CONSTRAINT fkhg9n34m9myy6mjamcrn7ugwa0 FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-69
ALTER TABLE facilities
  ADD CONSTRAINT fkjptojq3hke4impn3f21pf3yrq FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-70
ALTER TABLE facilities
  ADD CONSTRAINT fkrrysj47j1e2v6odntbcqq40o FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-71
ALTER TABLE organisation_facilities_mapping
  ADD CONSTRAINT fkjwvivng5u15onxn9insejjt35 FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-72
ALTER TABLE organisation_facilities_mapping
  ADD CONSTRAINT fkqu53hfqyey8j5h6l3t4l81edc FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-73
ALTER TABLE users
  ADD CONSTRAINT fk3aqdj6u2t0by2dj24m141utm FOREIGN KEY (modified_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-74
ALTER TABLE users
  ADD CONSTRAINT fkc294ua1x64t818kxosc1amivn FOREIGN KEY (organisations_id) REFERENCES organisations (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-75
ALTER TABLE users
  ADD CONSTRAINT fkibk1e3kaxy5sfyeekp8hbhnim FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-76
ALTER TABLE permissions
  ADD CONSTRAINT fk6td375738330a4j5gjbkg1260 FOREIGN KEY (services_id) REFERENCES services (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-77
ALTER TABLE role_permissions_mapping
  ADD CONSTRAINT fkmaqgtoh5r4r90m8rqyjj24uot FOREIGN KEY (roles_id) REFERENCES roles (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-78
ALTER TABLE organisation_services_mapping
  ADD CONSTRAINT fk5hcuc2h4ncne96wmaxw8qmok1 FOREIGN KEY (services_id) REFERENCES services (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset mokshesh:01-seed-schema-79
ALTER TABLE scopes
  ADD CONSTRAINT fkdwwb499l4abd3prbqafauwjty FOREIGN KEY (scope_groups_id) REFERENCES scope_groups (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
